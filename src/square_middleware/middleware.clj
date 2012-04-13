(ns square-middleware.middleware)

(def window-size 10)
(defn full-window? [r] (= window-size (count r)))

(defn avg [s]
  (/ (reduce + s) (count s)))

(defn variance [s mean]
  (avg
   (map (fn [x] (let [d (- x mean)] (* d d))) s)))

(def status-codes (atom []
                     :validator #(<= (count %) window-size)))

(defn trunc-to-hundreds [n]
  (-> n (quot 100) (* 100)))

(defn log-stats [status-codes]
  (let [truncd (map trunc-to-hundreds status-codes)
        a (avg truncd)
        v (variance truncd a)]
    (println (str "Average: " a " Variance: " v))))

(add-watch
 status-codes
 :status-codes-change
 (fn [_ _ _ new-value]
   (when (full-window? new-value)
     (log-stats new-value))))

(defn add-status [status-codes new-status]
  (let [v (if (full-window? status-codes)
            (subvec status-codes 1)
            status-codes)]
    (conj v new-status)))

(defn record-status [new-status]
  (swap! status-codes add-status new-status))

(defn wrap-status-logger [handler]
  (fn [request]
    (let [response (handler request)]
      (record-status (:status response))
      response)))
