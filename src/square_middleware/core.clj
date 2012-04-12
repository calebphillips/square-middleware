(ns square-middleware.core
  (:use [compojure.core :only [ANY]])
  (:require [compojure.handler :as handler])
  (:require [square-middleware.middleware :as mw]))

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
