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

(defn get-sum [lines target-value count-items]
  (let [less-or-equal (take-while #(<= % target-value) (reductions + (take count-items lines)))
        count-less-or-equal (count less-or-equal)
        ]
  (if (= (last less-or-equal) target-value)
    {:match true :values (take count-less-or-equal lines)}
    {:match false :values 'nil}
  )))

(defn find-target-sum [lines target-value]
  (loop [index 0
         work-lines lines]
    (let [sum-found (first (filter #(true? (:match %))
                           (map (partial get-sum work-lines target-value) (range 1 (count work-lines)))))]
      (if (:match sum-found)
        sum-found
        (recur (inc index) (rest work-lines)))
      ))
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file-content (slurp "input.txt")
        lines (vec (map #(Long/parseLong %) (str/split file-content #"\n")))
        context-length 25]
    (let [answer1 (first (filter #(= (:result %) false)
                                 (map (partial check-if-sum-found lines context-length)
                                      (range context-length (count lines)))))]
      (println "Part 1:" answer1)
      (let [match (find-target-sum (take (:index answer1) lines) (:value answer1))]
        (println "Part 2:" (+ (apply min (:values match)) (apply max (:values match))) match))

    )))