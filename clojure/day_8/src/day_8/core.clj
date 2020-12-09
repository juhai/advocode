(ns day-8.core
  (:gen-class)
  (:require [clojure.string :as str]))

(defn get-program-line [line-of-code]
  (let [op-and-val (str/split line-of-code #"\s")
        op (first op-and-val)
        value (Integer/parseInt (second op-and-val))]
    {:op op :val value}
    )
  )

(defn get-accumulator-value [lines]
  (let [program-lines (map get-program-line lines)
        executed-lines (apply assoc {} (interleave (range (count lines)) (repeat 0)))]
    (loop [executed-so-far executed-lines
           accumulator 0
           next-command-line 0]
      (let [executed-now (update-in executed-so-far [next-command-line] inc)
            current-program-line (nth program-lines next-command-line)
            accumulator-now
            (+ accumulator (if (= (:op current-program-line) "acc") (:val current-program-line) 0))
            new-next-command-line (+ next-command-line
                                     (cond
                                       (= (:op current-program-line) "nop") 1
                                       (= (:op current-program-line) "acc") 1
                                       (= (:op current-program-line) "jmp") (:val current-program-line)))
            ]
        ;(println current-program-line accumulator-now)
        (if (> (executed-now next-command-line) 1)
          accumulator
          (recur executed-now accumulator-now new-next-command-line))
        )
      )
    )
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file-content (slurp "input.txt")
        lines (str/split file-content #"\n")]
    (println "Part 1: Accumulator value is" (get-accumulator-value lines))
    ;(println "Part 2: )

    ))
