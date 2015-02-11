(ns simple-service-broker.core-test
  (:require [midje.sweet :refer :all]
            [simple-service-broker.core :refer :all]
            [clj-http.fake :as fake]))


(fact "get works"
      (fake/with-fake-routes-in-isolation
        {"http://rest.api/dogs/1"
         {:get (fn [req] {:status 200
                          :headers {}
                          :body "{'id':1}"})}}

        ((make-service-broker {:dogs "http://rest.api/dogs/:id"})
         :resource-name :dogs
         :url-params {:id 1}
         :request-options {:method :get
                           :headers {}})
        => (contains {:status 200
                      :body "{'id':1}"})))

(fact "put with text body works"
      (fake/with-fake-routes-in-isolation
        {"http://rest.api/dogs/1"
         {:put (fn [req] {:status 200
                          :headers {}
                          :body "{'id':1}"})}}

        ((make-service-broker {:dogs "http://rest.api/dogs/:id"})
         :resource-name :dogs
         :url-params {:id 1}
         :request-options {:method :put
                           :headers {}
                           :body {:a 34}})
        => (contains {:status 200
                      :body "{'id':1}"})))


(fact "put with map body works"
      (fake/with-fake-routes-in-isolation
        {"http://rest.api/dogs/1"
         {:put (fn [req] {:status 200
                          :headers {}
                          :body "{'id':1}"})}}

        ((make-service-broker {:dogs "http://rest.api/dogs/:id"})
         :resource-name :dogs
         :url-params {:id 1}
         :request-options {:method :put
                           :headers {}
                           :body "{\"a\":34}"})
        => (contains {:status 200
                      :body "{'id':1}"})))
