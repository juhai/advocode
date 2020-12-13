(ns day-10.core
  (:gen-class)
  (:require [clojure.string :as str]))

(defn get-jolts [lines]
  (let [sorted-lines (sort lines)
        final-lines (concat [0] sorted-lines [(+ 3 (last sorted-lines))])]
    (frequencies (map (fn [x y] (- y x)) final-lines (rest final-lines)))
    )
  )

(defn get-combinations-inverse [lines]
  (loop [work-lines (drop-last lines)
         current (-> lines butlast last)
         jolts {(last lines) 1}]
    (if (empty? work-lines)
      jolts
      (let [branch-count
            (reduce + (filter some? (map jolts (range (inc current) (+ current 4)))))]
        (recur
          (drop-last work-lines)
          (-> work-lines butlast last)
          (assoc jolts current branch-count)
          )
    )
  )))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file-content (slurp "input.txt")
        lines (vec (map #(Long/parseLong %) (str/split file-content #"\n")))
        jolts (get-jolts lines)]
    (println "Part 1:" jolts (* (jolts 3) (jolts 1)))
    (let [sorted-lines (sort lines)
          final-lines (concat [0] sorted-lines [(+ 3 (last sorted-lines))])
          combinations (get-combinations-inverse final-lines)]
      (println "Part 2 answer=" (combinations 0) combinations)
    )))
