(ns square-middleware.core)

(defn update-cma [{current-avg :avg i :i} new-data-pt]
  (let [sample-num (inc i)
        new-avg (+ current-avg
                   (/ (- new-data-pt current-avg)
                      sample-num))]
    {:i sample-num :avg new-avg}))

(def current-cma (atom {:i 0 :avg 0}))

(swap! current-cma update-cma 200)

