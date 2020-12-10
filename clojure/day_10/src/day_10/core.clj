(ns day-10.core
  (:gen-class)
  (:require [clojure.string :as str]))

(defn get_jolts [lines]
  (let [sorted-lines (sort lines)
        final-lines (concat [0] sorted-lines [(+ 3 (last sorted-lines))])]
    (frequencies (map (fn [x y] (- y x)) final-lines (rest final-lines)))
    )
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file-content (slurp "input.txt")
        lines (vec (map #(Long/parseLong %) (str/split file-content #"\n")))
        jolts (get_jolts lines)]
    (println "Part 1:" jolts (* (jolts 3) (jolts 1)))
    ))
