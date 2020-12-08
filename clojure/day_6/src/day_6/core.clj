(ns day-6.core
  (:gen-class)
  (:require [clojure.string :as str]))

(defn get-yes-answer-count [groups]
  (reduce +
          (->> groups
               (map #(str/replace % #"\n" ""))
               (map #(set %))
               (map count)
               ))
  )

(defn get-matching-count [map required-count]
  (count (filter #(= (second %) required-count) map))
  )

(defn get-yes-answer-count-part-2 [groups]
  (let [answers-per-group (map #(str/split % #"\n") groups)
        group-counts (map count answers-per-group)
        joined-answers (map #(str/join "" %) answers-per-group)
        char-maps (map frequencies joined-answers)
        valid-maps (map get-matching-count char-maps group-counts)
        ]
    (println valid-maps)
    (println char-maps)
    (reduce + valid-maps)
    )
  )


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file-content (slurp "input.txt")
        groups (str/split file-content #"\n\n")]
    (println "Part 1: Total yes men count is"
             (get-yes-answer-count groups))
    (println "Part 2: Total yes men count is"
             (get-yes-answer-count-part-2 groups))

    ))
