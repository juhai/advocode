(ns day-01.core
  (:gen-class))

(defn count-increase
  [depths]
  (count
   (filter (fn [x] (> (second x) (first x)))
           (partition 2 1 depths))))

(defn count-increase-in-threes
  [depths]
  (let [sums-of-three (map (partial reduce +) (partition 3 1 depths))]
    (println sums-of-three)
    (count
     (filter (fn [x] (> (second x) (first x)))
             (partition 2 1 sums-of-three)))))

(defn -main
  "Advent of code 2021 day "
  [& args]
  (let [file-content (slurp "input.txt")
        lines (clojure.string/split file-content #"\n")
        depths (map clojure.edn/read-string lines)]
    ;; Part 1
    (println (count-increase depths))
    ;; Part 2
    (println (count-increase-in-threes depths))))

