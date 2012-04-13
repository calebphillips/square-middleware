(defproject square-middleware "1.0.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [compojure "1.0.1"]
                 [ring/ring-jetty-adapter "0.3.10"]
                 [ring-basic-authentication "1.0.0"]]
  :plugins [[lein-ring "0.6.3"]]
  :ring {:handler square-middleware.core/app}
  :main square-middleware.core)