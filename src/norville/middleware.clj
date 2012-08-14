(ns norville.middleware)

(defn make-url [{:keys [host port uri scheme query-string]}]
  (let [https ()]
    (format "%s://%s:%s%s%s"
            (or scheme "http")
            host port (or uri "/")
            (if query-string
              (format "?%s" query-string)
              ""))))

(defn wrap-make-request [client]
  (fn [req]
    (client (merge req {:url (make-url
                              (merge (-> req :cfg :dst)
                                     (select-keys (:ring req)
                                                  [:uri :query-string])))
                        :method (-> req :ring :request-method)
                        :body (-> req :ring :body)
                        :throw-exceptions false
                        :as :stream}))))
