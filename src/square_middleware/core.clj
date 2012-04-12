(ns square-middleware.core
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]))

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
  (let [a (avg responses)
        v (variance responses a)]
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

(defn record-response [new-response]
  (swap! responses add-response new-response))

(defroutes main-routes
  (GET "/:id" [id] (str "<h1>Hello World Wide Web!</h1><h2>It's " id "</h2>")))

(defn wrap-count-status [handler]
  (fn [request]
    (let [response (handler request)]
      (record-response (:status response))
      response)))

(def app
  (->
   (handler/site main-routes)
   (wrap-count-status)))
