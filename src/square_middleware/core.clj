(ns square-middleware.core
  (:use compojure.core)
  (:require [compojure.handler :as handler]))

(def window-size 10)
(defn full-window? [r] (= window-size (count r)))

(defn avg [s]
  (/ (reduce + s) (count s)))

(defn variance [s mean]
  (avg
   (map (fn [x] (let [d (- x mean)] (* d d))) s)))


(def responses (atom []
                     :validator #(<= (count %) window-size)))

(defn log-stats [responses]
  (let [a (double (avg responses))
        v (double (variance responses a))]
    (println (str "Average: " a " Variance: " v))))

(add-watch
 responses
 :responses-change
 (fn [_ _ _ new-value]
   (when (full-window? new-value)
     (log-stats new-value))))

(defn add-response [responses new-response]
  (let [v (if (full-window? responses)
            (subvec responses 1)
            responses)]
    (conj v new-response)))

(defn trunc-to-hundreds [n]
  (-> n (quot 100) (* 100)))

(defn record-response [new-response]
  (swap! responses add-response (trunc-to-hundreds new-response)))

(def statuses (mapcat #(apply range %) [[200 207] [300 307] [400 417] [500 505]]))
(def rand-status #(rand-nth statuses))

(defn create-response [id]
  {:status (rand-status)
   :body (str "<h1>Got request from " id "</h1>")})

(defroutes main-routes
  (ANY "/locations/:id" [id] (create-response id)))

(defn wrap-status-logger [handler]
  (fn [request]
    (let [response (handler request)]
      (record-response (:status response))
      response)))

(def app
  (->
   (handler/api main-routes)
   (wrap-status-logger)))
