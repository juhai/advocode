(ns day-7.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def bag-desc-re #"([a-z]+ [a-z]+) bag[s]?")

(defn get-map-for-single-item [matches]
  (let [just-colors (map second matches)
        outer-color (first just-colors)
        inner-colors (rest just-colors)]
    (apply assoc {} (interleave inner-colors (map (fn [c] [c]) (repeat outer-color))))
    ))

(defn get-inside-out-map [colour-matches]
  (apply (partial merge-with into) (map get-map-for-single-item colour-matches))
  )

(defn get-matches [inside-out-map]
  (loop [matches (set nil)
         iteration 0
         queue (seq (inside-out-map "shiny gold"))]
    (if (empty? queue)
      matches
      (let [first-bag (first queue)
            next-bags (inside-out-map first-bag)]
        (recur (set (concat matches [first-bag]))
               (inc iteration)
               (seq (set (concat (rest queue) next-bags))))))
    )
  )

(defn get-color-maps [lines]
  (let [lines-filtered (filter #(not (str/includes? % "contains no other bags")) lines)
        colours (map (partial re-seq bag-desc-re) lines-filtered)
        inside-out-map (get-inside-out-map colours)
        matching-colours (get-matches inside-out-map)
        ]
    matching-colours
  ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file-content (slurp "input.txt")
        color-maps (get-color-maps (str/split file-content #"\n"))]
    (println (count color-maps) "bags can contain shiny gold bag")
    ))
