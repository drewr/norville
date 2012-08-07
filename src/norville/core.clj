(ns norville.core
  (:require [ring.adapter.jetty :as jetty]
            [clj-http.client :as http]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]))

(def server (atom nil))

(defn make-url [{:keys [host port uri scheme query-string]}]
  (let [https ()]
    (format "%s://%s:%s%s%s"
            (or scheme "http")
            host port (or uri "/")
            (if query-string
              (format "?%s" query-string)
              ""))))

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

(defn make-proxy-handler [cfg]
  (fn [srcreq]
    (let [dstreq {:url (make-url (merge (:dst cfg)
                                        (select-keys srcreq
                                                     [:uri :query-string])))
                  :method (:request-method srcreq)
                  :body (:body srcreq)
                  :throw-exceptions false}]
      (log/info (stringy-ring-req srcreq) (stringy-clj-http-req dstreq))
      (http/request dstreq))))

(defn serve-config! [cfg]
  (when-not @server
    (reset! server
            (jetty/run-jetty #((make-proxy-handler cfg) %)
                             (merge {:join? false} (:src cfg))))))

(defn serve!
  ([]
     (serve! {}))
  ([overrides]
     (let [cfgname (:config overrides "norville.clj")
           cfg (io/resource cfgname)]
       (if-not cfg
         (throw (Exception. (format "can't find config [%s]" cfgname))))
       (let [m (-> cfg io/reader java.io.PushbackReader. read)]
         (serve-config! (merge-with merge m overrides))))))

(defn stop! []
  (.stop @server))

(defn start! []
  (.start @server)
  @server)

(defn -main [& args]
  (let [opts (apply hash-map (map read-string args))]
    (serve! opts)))
