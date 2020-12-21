(ns day-15.core
  (:gen-class))

(defn get-initial-memory [data]
  "Get initial values for the memory"
  (apply hash-map (interleave data (map list (rest (range)))))
  )

(defn get-answer [memory previous-answer]
  (if (= (count (memory previous-answer)) 1)
    0
    (- (first (memory previous-answer)) (second (memory previous-answer))))
  )


(defn update-memory [memory current-number iteration]
  (update memory
          current-number
          #(doall (take 2 (conj % iteration)))
    ))

(defn process-numbers [data target-count]
  (loop [memory (get-initial-memory data)
         current-number (last data)
         iteration-count (inc (count data))]
    (let [next-number (get-answer memory current-number)]
      (if (= iteration-count target-count)
        next-number
        (let [updated-memory (update-memory memory next-number iteration-count)]
          (recur
            updated-memory
            next-number
            (inc iteration-count))
      )))
  ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [test-data [[0, 3, 6], [1, 3, 2], [2, 1, 3], [1, 2, 3], [2, 3, 1], [3, 2, 1], [3, 1, 2]]
        part1-data [15, 5, 1, 4, 7, 0]]
    (doall
      (for [data test-data]
        (println "Test round" (process-numbers data 2020)))
      )
    (println "Official result for part I is" (process-numbers part1-data 2020))
    (println "Official result for part II is" (process-numbers part1-data 30000000))
    )

  )
