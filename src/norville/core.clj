(ns norville.core
  (:refer-clojure :exclude [proxy])
  (:require [ring.adapter.jetty :as jetty]
            [clj-http.client :as http]
            [clojure.java.io :as io]
            [clojure.tools.logging :as log]
            [norville.middleware]))

(def server (atom nil))

(defn proxy [req]
  (http/request (assoc req :decompress-body false)))

(defn resolve-handler [h]
  (try
    (let [[ns f] (.split (str h) "/")]
      (log/debug "loading handler" (str h))
      (require (symbol ns))
      (resolve h))
    (catch Exception e
      (log/warn "could not load handler" h)
      (.printStackTrace e))))

(defn make-proxy-handler [cfg]
  (log/debug "making proxy handler")
  (let [fns (concat (remove nil? (map resolve-handler (:handlers cfg)))
                    [norville.middleware/wrap-make-request])
        wrap-proxy (reduce #(%2 %1) proxy fns)]
    (fn [req]
      (wrap-proxy {:ring req :cfg cfg}))))

(defn serve-config! [cfg]
  (when-not @server
    (reset! server
            (jetty/run-jetty (make-proxy-handler cfg)
                             (merge {:join? false} (:src cfg)))))
  (log/debug "norville started"))

(defn serve!
  ([]
     (serve! {}))
  ([overrides]
     (log/debug "starting norville")
     (let [cfgname (:config overrides "norville.clj")
           cfg (io/resource cfgname)]
       (if-not cfg
         (throw (Exception. (format "can't find config [%s]" cfgname))))
       (let [m (-> cfg io/reader java.io.PushbackReader. read)]
         (serve-config! (merge m overrides))))))

(defn stop! []
  (.stop @server))

(defn start! []
  (.start @server)
  @server)

(defn -main [& args]
  (let [opts (apply hash-map (map read-string args))]
    (serve! opts)))
