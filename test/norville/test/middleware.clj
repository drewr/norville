(ns norville.test.middleware
  (:use [clojure.test])
  (:use [norville.middleware] :reload))

(deftest t-explode
  (let [client (wrap-explode-uri (fn [req] req) :foo)
        expl (client {:ring {:request-method :get :uri "/blip/bop/1"}})]
    (is (= (:foo expl) {:id "1", :type "bop", :index "blip"}))))
