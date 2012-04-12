(defproject square-middleware "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.2.1"]
                 [compojure "1.0.1"]]
  :plugins [[lein-ring "0.6.3"]]
  :ring {:handler square-middleware.core/app})