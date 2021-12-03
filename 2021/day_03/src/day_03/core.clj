(ns day-03.core
  (:gen-class))

(defn get-combinations
  [indices observations]
  (for [index indices
        observation observations]
    [index (subs observation index (inc index))]
    )
  )

(defn get-gamma-rate
  [bit-frequencies bit-length]
  (let [gamma-string (clojure.string/join
                       "" (map
                            (fn [index]
                              (if (> (bit-frequencies [index "1"] 0)
                                     (bit-frequencies [index "0"] 0)) 1 0))
                            (range bit-length)))]
    (Integer/parseInt gamma-string 2)))

(defn get-epsilon-rate
  [gamma-rate mask]
  (->> gamma-rate
        bit-not
        (bit-and mask)))

(defn get-mask
  [bit-length]
  (dec (int (Math/pow 2 bit-length))))

(defn get-power-consumption
  [observations]
  (let [bit-length (count (first observations))
        freqs (frequencies (get-combinations (range bit-length) observations))
        gamma-rate (get-gamma-rate freqs bit-length)
        mask (get-mask bit-length)
        epsilon-rate (get-epsilon-rate gamma-rate mask)
        ]
    (* gamma-rate epsilon-rate)
    )
  )

(defn get-oxygen-generator-rating
  [observations bit-length]
  (loop [remaining observations
         index 0]
    (let [bit-frequencies (frequencies (map (fn [x] (subs x index (inc index))) remaining))
          max-value (if (>= (bit-frequencies "1" 0) (bit-frequencies "0" 0)) "1" "0")
          next-remaining (filter (fn [x] (= (subs x index (inc index)) max-value)) remaining)]
      (if
        (or (>= index bit-length) (= (count next-remaining) 1))
          (Integer/parseInt (first next-remaining) 2)
          (recur next-remaining (inc index))
      )
    )
  ))

(defn get-co2-scrubber-rating
  [observations bit-length]
  (loop [remaining observations
         index 0]
    (let [bit-frequencies (frequencies (map (fn [x] (subs x index (inc index))) remaining))
          max-value (if (<= (bit-frequencies "0" 0) (bit-frequencies "1" 0)) "0" "1")
          next-remaining (filter (fn [x] (= (subs x index (inc index)) max-value)) remaining)]
      (if
        (or (>= index bit-length) (= (count next-remaining) 1))
        (Integer/parseInt (first next-remaining) 2)
        (recur next-remaining (inc index))
        )
      )
    ))

(defn get-life-support-rating
  [observations]
  (let [bit-length (count (first observations))
        oxygen-generator-rating (get-oxygen-generator-rating observations bit-length)
        co2-scrubber-rating (get-co2-scrubber-rating observations bit-length)
        ]
    (println oxygen-generator-rating)
    (println co2-scrubber-rating)
    (* oxygen-generator-rating co2-scrubber-rating)
  ))

(defn -main
      "Advent of code 2021 day 3"
      [& args]
      (let [file-content (slurp "input.txt")
            lines (clojure.string/split file-content #"\n")]
           ;; Part 1
           (println "Part 1: " (get-power-consumption lines))
           ;; Part 2
           (println "Part 2:" (get-life-support-rating lines))
           ))
