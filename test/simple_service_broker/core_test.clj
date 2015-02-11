(ns simple-service-broker.core-test
  (:require [midje.sweet :refer :all]
            [simple-service-broker.core :refer :all]
            [clj-http.fake :as fake]))

(def routing-table {:dogs {:get-single {:path "http://rest.api/dogs/:id"
                                        :method :get}
                           :put {:path "http://rest.api/dogs/:id"
                                 :method :put}}})

(def service-broker (make-service-broker routing-table))
(fact "get works" :dev
      (fake/with-fake-routes-in-isolation
        {"http://rest.api/dogs/1"
         {:get (fn [req] {:status 200
                          :headers {}
                          :body "{'id':1}"})}}

        (service-broker :resource-name :dogs
                        :action :get-single
                        :url-params {:id 1}
                        :request-options {})
        => (contains {:status 200
                      :body "{'id':1}"})))

(fact "put with text body works"
      (fake/with-fake-routes-in-isolation
        {"http://rest.api/dogs/1"
         {:put (fn [req] {:status 200
                          :headers {}
                          :body "{'id':1}"})}}

        (service-broker :resource-name :dogs
                        :action :put
                        :url-params {:id 1}
                        :request-options {:headers {}
                                          :body {:a 34}})
        => (contains {:status 200
                      :body "{'id':1}"})))


(fact "put with map body works"
      (fake/with-fake-routes-in-isolation
        {"http://rest.api/dogs/1"
         {:put (fn [req] {:status 200
                          :headers {}
                          :body "{'id':1}"})}}

        (service-broker :resource-name :dogs
                        :action :put
                        :url-params {:id 1}
                        :request-options {:headers {}
                                          :body "{\"a\":34}"})
        => (contains {:status 200
                      :body "{'id':1}"})))
