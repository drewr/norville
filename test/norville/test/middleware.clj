(ns norville.test.middleware
  (:use [clojure.test])
  (:use [norville.middleware] :reload))

(def __key :foo)

(def client (wrap-explode-uri (fn [req] req) __key))

(defn t [[meth uri] shrapnel]
  (let [res (client {:ring {:request-method meth :uri uri}})
        bool (= shrapnel (__key res))]
    (if-not bool
      (println ">>>>" res))
    bool))

(deftest t-explode
  (is (t [:get  "/"                ] {}))
  (is (t [:get  "/blip/bop/1"      ] {:index "blip", :type "bop", :id "1"}))
  (is (t [:get  "/blip/_stats/get" ] {:index "blip"}))
  (is (t [:get  "/blip/bop"        ] nil))
  (is (t [:put  "/blip/bop/1"      ] {:index "blip", :type "bop", :id "1"}))
  (is (t [:post "/blip"            ] {:index "blip"}))
  (is (t [:put  "/blip"            ] {:index "blip"}))
  (is (t [:put  "/blip/_mapping"   ] {:index "blip"}))
  (is (t [:put  "/blip"            ] {:index "blip"}))
  (is (t [:get  "/_cluster/health" ] {}))
  (is (t [:get  "/_stats"          ] {}))
  )
