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

(defn get-accumulator-value [executed-lines program-lines]
  (loop [executed-so-far executed-lines
         accumulator 0
         next-command-line 0]
    (let [executed-now (update-in executed-so-far [next-command-line] inc)
          current-program-line (nth program-lines next-command-line)
          accumulator-now
          (+ accumulator (if (= (:op current-program-line) "acc") (:val current-program-line) 0))
          new-next-command-line
          (+ next-command-line
             (cond
             (= (:op current-program-line) "nop") 1
             (= (:op current-program-line) "acc") 1
             (= (:op current-program-line) "jmp") (:val current-program-line)))]
      ;(println current-program-line accumulator-now new-next-command-line executed-now)
      (cond (> (executed-now next-command-line) 1)
            (- accumulator)
            (= new-next-command-line (count executed-lines))
            accumulator-now
            :else
            (recur executed-now accumulator-now new-next-command-line))
      )
    )
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file-content (slurp "input.txt")
        lines (str/split file-content #"\n")
        program-lines (vec (map get-program-line lines))
        executed-lines (apply assoc {} (interleave (range (count program-lines)) (repeat 0)))]
    ; First part return negative answer that's filtered on the second round
    (println "Part 1: Accumulator value is" (- (get-accumulator-value executed-lines program-lines)))
    (println
      "Part 2:"
      (filter
        pos?
        (for [line (map-indexed vector program-lines)
          :let [op (-> line second :op)
                index (-> line first)
                inv-op (cond (= op "jmp") "nop" (= op "nop") "jmp")]
          :when (or (= op "jmp") (= op "nop"))]
      (get-accumulator-value
        executed-lines
        (assoc program-lines index {:op inv-op :val (-> line second :val)})
      )))

    )))
