(defproject com.draines/norville "1.0.1"
  :description "Great Barrier Reef"
  :url "https://github.com/drewr/norville"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[log4j/log4j "1.2.17"]
                 [org.clojure/clojure "1.4.0"]
                 [ring/ring-jetty-adapter "1.1.1"]
                 [clj-http "0.5.3"]
                 [cheshire "4.0.1"]
                 [org.clojure/tools.logging "0.2.3"]
                 [commons-io "2.4"]
                 [compojure "1.1.1"]
                 [ring-mock "0.1.3"]]
  :main norville.core)
