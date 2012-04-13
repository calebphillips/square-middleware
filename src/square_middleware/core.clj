(ns square-middleware.core
  (:use [compojure.core :only [defroutes GET PUT DELETE context]])
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [square-middleware.middleware :as mw]
            [ring.adapter.jetty :as ring]
            [ring.middleware.basic-authentication :as auth])
  (:gen-class))

;; Generate a vector of the status codes from
;; http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
;; I removed 401 and 404 because they are legitimately returned by the
;; specified functionality of the service (when basic auth fails and
;; when a non-existent resource or unsupported verb is requested.
(def statuses
  (remove #{401 404}
          (mapcat #(apply range %)
                  [[200 207] [300 307] [400 417] [500 505]])))

(def rand-status #(rand-nth statuses))

(defn create-response [location_id]
  {:status (rand-status)
   :body (str "<h1>Got request with location_id = " location_id "</h1>")})

(defroutes routes
  (context "/locations/:location_id" [location_id]
           (GET "/" [] (create-response location_id))
           (PUT "/" [] (create-response location_id))
           (DELETE "/" [] (create-response location_id)))
  (route/not-found "Resource not found"))

(defn authenticated? [username password]
  (and
   (= "fred" username)
   (= "flintstone" password)))

(def app
  (->
   (handler/api routes)
   (auth/wrap-basic-authentication authenticated?)
   (mw/wrap-status-logger)))

(def ssl-port 8443)

(defn remove-non-ssl-connectors
  "Get rid of any connectors other than the one we configured on ssl-port.
   Found this idea here: http://practice.kokonino.net/posts/ring-with-ssl-only
   Could also have redirected all non-HTTPS to the ssl-port."
  [server]
  (doseq [c (filter identity (.getConnectors server))]
    (when (not= ssl-port (.getPort c))
      (.removeConnector server c))))

(defn -main
  "Starts a jetty instance to run our application"
  []
  (ring/run-jetty
   (var app)
   {:ssl? true
    :ssl-port ssl-port
    :keystore "./keystore"
    :configurator remove-non-ssl-connectors}))
