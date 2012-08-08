(ns norville.log
  [:require [clojure.tools.logging :as log]])

(defn stringy-ring-req [{:keys [scheme server-name server-port
                                uri query-string request-method]}]
  (let [qs (if query-string (format "?%s" query-string) "")]
    (format "%s %s://%s:%s%s%s"
            (-> request-method name .toUpperCase)
            scheme
            server-name
            server-port
            uri
            qs)))

(defn stringy-clj-http-req [req]
  (format "%s %s" (-> req :method name .toUpperCase) (:url req)))

(defn wrap-log [client]
  (fn [req]
    (log/info (stringy-ring-req (:ring req))
              (stringy-clj-http-req req))
    (client req)))

(defn wrap-debug [client]
  (fn [req]
    (client (assoc req :debug true :debug-body true))))
