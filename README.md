# simple-service-broker

The simple-service-broker is a super simple, reference implementation of a service broker compatible with [Sweet-Liberty](http://github.com/RJMetrics/sweet-liberty). This library exposes a single function: `make-service-broker`. This function takes a route configuration as a hash map. 


```Clojure
(def routing-table {:dogs {:get-single {:path "http://rest.api/dogs/:id"
                                        :method :get}
                           :put {:path "http://rest.api/dogs/:id"
                                 :method :put}}})

(def service-broker (make-service-broker routing-table))

;; The following will make a GET request to `http://rest.api/dogs/1"
;; A subset of the response might look like:
;;   `{:status 200
;;     :body "{'id':1, 'name':'Brodi', 'breed':'corgi'}"}
(service-broker :resource-name :dogs
                :action :get-single
                :url-params {:id 1}
                :request-options {})
                
;; The following will make a PUT request to `http://rest.api/dogs/1"
;; With a body like:
;;   `{'name': 'Lacy'}`
(service-broker :resource-name :dogs
                :action :put
                :url-params {:id 1}
                :request-options {:headers {}
                                  :body {:name 'Lacy'}})                

```

The interface of the service broker function (returned by `make-service-broker` in this library) is a critical requirement to compatibility with Sweet-Liberty, of course. Any number of service broker implementations are possible, but this interface must always be supported. The interface is defined as follows:

The argument list uses named arguments. These are:
- :resource-name
  - The name of the resource that is to be acted upon. This combined with the `action` argument should map to an HTTP method and a url template.
- :action
  - One of the actions supported by the resource. This is typically specified in a route definition.
- :url-params
  - A hash map of values to be substituted into the url template.
- :request-options
  - This is a configuration hash map of the type that could be passed to [clj-http.client](http://github.com/#######). This should support the following optional keys:
    - :headers
      - A hash map of HTTP headers to be included in the outgoing request.
    - :body
      - The body of the request
    - :method
      - Override for HTTP method

