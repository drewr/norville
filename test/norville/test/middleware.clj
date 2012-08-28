(ns norville.test.middleware
  (:use [clojure.test])
  (:use [norville.middleware] :reload))

(deftest t-explode
  (let [client (wrap-explode-uri (fn [req] req) :foo "/:bar/*")
        expl (client {:ring {:uri "/blip/bop/1/2/3"}})]
    (is (= (:foo expl)
           {:* "bop/1/2/3", :bar "blip"}))))
