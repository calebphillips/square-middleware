(ns square-middleware.core
  (:use [compojure.core :only [defroutes ANY]])
  (:require [compojure.handler :as handler]
            [square-middleware.middleware :as mw]
            [ring.adapter.jetty :as ring])
  (:gen-class))

(def statuses (mapcat #(apply range %) [[200 207] [300 307] [400 417] [500 505]]))
(def rand-status #(rand-nth statuses))

(defn create-response [id]
  {:status (rand-status)
   :body (str "<h1>Got request from " id "</h1>")})

(defroutes main-routes
  (ANY "/locations/:id" [id] (create-response id)))

(def app
  (->
   (handler/api main-routes)
   (mw/wrap-status-logger)))

(def ssl-port 8443)

;; Found this here: http://practice.kokonino.net/posts/ring-with-ssl-only
(defn remove-non-ssl-connectors [server]
  (doseq [c (filter identity (.getConnectors server))]
    (when (not= ssl-port (.getPort c))
      (.removeConnector server c))))

(defn -main []
  (ring/run-jetty
   (var app)
   {:ssl? true
    :ssl-port ssl-port
    :keystore "./keystore"
    :configurator remove-non-ssl-connectors}))
