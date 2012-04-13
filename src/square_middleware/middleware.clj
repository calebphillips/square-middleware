(ns square-middleware.middleware)

(def window-size 10)
(defn full-window? [r] (= window-size (count r)))

(defn avg [s]
  (/ (reduce + s) (count s)))

(defn variance [s mean]
  (avg
   (map (fn [x] (let [d (- x mean)] (* d d))) s)))

(def status-codes
  "Our current list of status codes.  Will not be allowed to
   grow past our reporting window size"
  (atom []
        :validator #(<= (count %) window-size)))

(defn trunc-to-hundreds
  "Ex: 203 => 200, 501 => 500"
  [n]
  (-> n (quot 100) (* 100)))

(defn fmt [n]
  (format "%.2f" (double n)))

(defn log-stats [status-codes]
  (let [truncd (map trunc-to-hundreds status-codes)
        a (avg truncd)
        v (variance truncd a)]
    (println (str "Average: " (fmt a) " Variance: " (fmt v)))))

;; Watch the status-codes atom for changes and once the list has
;; reached the window size we want to report on, log the stats
(add-watch
 status-codes
 :status-codes-change
 (fn [_ _ _ new-value]
   (when (full-window? new-value)
     (log-stats new-value))))

(defn add-status
  "Add the new status to the list of status, sliding the window
  forward as necessary"
  [status-codes new-status]
  (let [v (if (full-window? status-codes)
            (subvec status-codes 1)
            status-codes)]
    (conj v new-status)))

(defn record-status [new-status]
  (swap! status-codes add-status new-status))

(defn wrap-status-logger
  "Middleware that records the response's status code and returns
  the response unchanged."
  [handler]
  (fn [request]
    (let [response (handler request)]
      (record-status (:status response))
      response)))
