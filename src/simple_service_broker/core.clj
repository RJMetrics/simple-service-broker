(ns simple-service-broker.core
  (:require [clj-http.client :as client]))

(defn make-service-broker
  [routes]
  (fn [& {:keys [resource-name url-params request-options]}]
    "nope"))
