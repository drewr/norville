(ns norville.middleware
  (:require [norville.routes :as routes]))

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
                        :headers (dissoc (-> req :ring :headers)
                                         "content-length")
                        :throw-exceptions false
                        :as :stream}))))

(defn wrap-explode-uri [client key]
  (fn [req]
    (client (assoc req key (routes/params (-> req :ring :request-method)
                                          (-> req :ring :uri))))))
