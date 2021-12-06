(ns day-06.core)

(defrecord Fish [timer count])

(defn get-new-fish-count [fish]
  (let [mature-fish (filter (fn [f] (= (:timer f) 0)) fish)]
    (if (empty? mature-fish)
      0
      (reduce + (map :count mature-fish))
    )))

(defn age-fish [one-fish]
  (if (= (:timer one-fish) 0)
    (Fish. 6 (:count one-fish))
    (Fish. (-> one-fish :timer dec) (:count one-fish))
  ))

(defn update-fish-counts [current-fish-aged new-fish-count]
  (->> (concat current-fish-aged [(Fish. 8 new-fish-count)])
       (sort-by :timer)
       (partition-by :timer)
       (map (fn [fish-with-same-age]
              (Fish.
                (-> fish-with-same-age first :timer)
                (reduce + (->> fish-with-same-age (map :count))))))
       )
  )

(defn set-timer-and-spawn [current-fish _]
  (let [new-fish-count (get-new-fish-count current-fish)
        current-fish-aged (map age-fish current-fish)]
    (if (> new-fish-count 0)
      (let [new-fish (update-fish-counts current-fish-aged new-fish-count)]
        new-fish)
      current-fish-aged
    )
  ))

(defn get-fish-count [initial-fish days]
  (reduce + (map :count (reduce set-timer-and-spawn initial-fish (range days))))
  )

(defn -main
  "Advent of code 2021 day 6"
  [& args]
  (let [file-content (slurp "input.txt")
        initial-fish (map
                       (fn [f] (Fish. (read-string (first f)) (second f)))
                       (frequencies (clojure.string/split (clojure.string/trim file-content) #",")))]
    ;; Part 1
    (println "Part 1: " (get-fish-count initial-fish 80))
    ;; Part 2
    (println "Part 2: " (get-fish-count initial-fish 256))
    ))
