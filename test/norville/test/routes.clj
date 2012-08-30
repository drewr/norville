(ns norville.test.routes
  (:use [clojure.test])
  (:use [norville.routes] :reload))

(deftest t-routes
  (is (= (params :get "/foo/bar/baz")
         {:id "baz"
          :type "bar"
          :index "foo"}))
  (is (= (params :put "/foo/bar/baz")
         {:id "baz"
          :type "bar"
          :index "foo"}))
  (is (= (params :get "/_cluster/health")
         {})))
