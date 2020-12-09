(ns day-7.core
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def bag-desc-re #"([a-z]+ [a-z]+) bag[s]?")
(def bag-desc-with-weight-re #"(\d+) ([a-z]+ [a-z]+) bag[s]?")

(defn get-map-for-single-item [matches]
  (let [just-colors (map second matches)
        outer-color (first just-colors)
        inner-colors (rest just-colors)]
    (apply assoc {} (interleave inner-colors (map (fn [c] [c]) (repeat outer-color))))
    ))

(defn get-inside-out-map [colour-matches]
  (apply (partial merge-with into) (map get-map-for-single-item colour-matches))
  )

(defn get-single-bag-map [key values]
  (let [bag-colour (second (first key))
        weights (map #(->> % second Integer/parseInt) values)
        inside-colours (map #(nth % 2) values)
        bag-weight-map (apply assoc {} (interleave inside-colours weights))
        ]
    ;(println "K:V" bag-colour weights inside-colours)
    {bag-colour bag-weight-map}
  ))

(defn count-bags [bag-to-bag-map bag-name]
  (let [bags-in-current-bag (keys (bag-to-bag-map bag-name))
        counts-in-current-bag (vals (bag-to-bag-map bag-name))]
    ;(println bags-in-current-bag counts-in-current-bag)
    (if (nil? bags-in-current-bag)
      1
      ; +1 here for the bag itself that contains other bags
      (+ 1 (reduce + (map (fn [name weight]
                       (* weight (count-bags bag-to-bag-map name)))
                     bags-in-current-bag
                     counts-in-current-bag))))
  ))

(defn get-bag-count [lines]
  (let [colour-keys (map (partial re-seq bag-desc-re) lines)
        colour-values (map (partial re-seq bag-desc-with-weight-re) lines)
        bag-to-bag-map (apply (partial merge-with into) (map get-single-bag-map colour-keys colour-values))]
    ;(println bag-to-bag-map)
    ; minus-1 to remove the shiny gold bag from computation
    (- (count-bags bag-to-bag-map "shiny gold") 1)
    ))

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
  (let [lines-filtered (filter #(not (str/includes? % "contain no other bags")) lines)
        colours (map (partial re-seq bag-desc-re) lines-filtered)
        inside-out-map (get-inside-out-map colours)
        matching-colours (get-matches inside-out-map)
        ]
    matching-colours
  ))

(defn get-other-bags [lines]
  (let [lines-filtered (filter #(not (str/includes? % "contain no other bags")) lines)
        bag-count (get-bag-count lines-filtered)]
    ;(println bag-count)
    bag-count
  ))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file-content (slurp "input.txt")
        color-maps (get-color-maps (str/split file-content #"\n"))
        other-bags (get-other-bags (str/split file-content #"\n"))]
    (println "Part 1:" (count color-maps) "bags can contain shiny gold bag")
    (println "Part 2: A single shiny gold bag contains" other-bags "bags")

    ))
