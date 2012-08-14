(defproject norville "0.1.0-SNAPSHOT"
  :description "Great Barrier Reef"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[log4j/log4j "1.2.17"]
                 [org.clojure/clojure "1.4.0"]
                 [ring/ring-jetty-adapter "1.1.1"]
                 [clj-http "0.5.3"]
                 [cheshire "4.0.1"]
                 [org.clojure/tools.logging "0.2.3"]]
  :main norville.core)
