(ns day-9.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(defn check-if-sum-found [lines context-length index]
  (let [target-value (nth lines index)
        context (take context-length (drop (- index context-length) lines))
        target-context (filter (fn [x] (and (> x 0) (not= x (/ target-value 2))))
                               (map #(- target-value %) context))
        intersection (set/intersection (set context) (set target-context))
        ]
    ;(println context ":" target-value ":" target-context)
    {:index index :value target-value :result (>= (count intersection) 2)}
    )
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file-content (slurp "input.txt")
        lines (map #(Long/parseLong %) (str/split file-content #"\n"))
        context-length 25]
    (println (first (filter #(= (:result %) false)
                            (map (partial check-if-sum-found lines context-length)
                                 (range context-length (count lines))))))
    ))