(ns day-3.core
  (:gen-class))

(require '[clojure.string :as str])

(defn get-map [lines]
  (into {}
        (for [line (map-indexed vector lines)
              c (map-indexed vector (seq (second line)))
              :let [row-idx (first line)
                    col-idx (first c)
                    col-data (second c)]
              :when (= col-data \#)]
          [[row-idx col-idx] 1]
        ))
  )

(defn get-row-count [lines]
  (count lines))

(defn get-col-count [lines]
  (count (first lines)))

(defn is-a-tree [index-map col-count col row]
  (index-map [row (mod col col-count)] 0))

(defn get-tree-count [index-map row-count col-count row-skip col-skip]
  (let [tree-count
        (reduce + (map (partial is-a-tree index-map col-count)
                  (take-nth col-skip (range))
                  (range 0 (inc row-count) row-skip)))]
  (println row-count col-count col-skip row-skip tree-count)
  tree-count
  ))

(defn -main
  "Solution to the day 3 exercises. Second output row gives answer to first part"
  [& args]
  (let [file-content (slurp "input.txt")
        lines (str/split file-content #"\n")
        index-map (get-map lines)]
    (println "All trees multiplied"
      (reduce * (map (partial get-tree-count index-map (get-row-count lines) (get-col-count lines))
                     '(1 1 1 1 2) '(1 3 5 7 1))
      ))
  ))
