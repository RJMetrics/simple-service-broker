(ns simple-service-broker.core
  (:require [clj-http.client :as client]))

(def default-request-options {:method :get
                              :socket-timeout 500
                              :conn-timeout 5000})


(defn- parse-into-url
  "Take a URL formatted as a compojure route (e.g. \"/client/:id\") and parse
  the corresponding keywords in params into the URL.
  (parse-int-url \"/client/:id/table/:tid/query\" {:id 1 :tid 2}) => \"/client/1/table/12/query\""
  [url url-params]
  ;; Essentially split the string by / but save the /, then check if each item
  ;; in the url as a keyword matches an item in the url-params and sub them in
  (when-not (nil? url)
    (let [split-url (map #(apply str %) (partition-by #(= \/ %) url))]
      (reduce #(str %1
                    (or ((keyword (subs %2 1)) url-params) %2))
              ""
              split-url))))

(defn make-service-broker
  [routes]
  (fn [& {:keys [resource-name url-params request-options]}]
    (client/request (merge default-request-options
                           request-options
                           {:url (parse-into-url (resource-name routes)
                                                 url-params)}))))
