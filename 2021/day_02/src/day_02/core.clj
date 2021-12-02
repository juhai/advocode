(ns day-02.core
  (:gen-class))

(def horisontal-order-directions {"forward" 1})
(def vertical-order-directions {"up" -1, "down" 1, "forward" 0})

(defn update-aim-and-position
  [current-aim-and-position order]
  (let [value (read-string (second order))
        up-down-forward (first order)
        new-aim (+ (:aim current-aim-and-position) (* value (vertical-order-directions up-down-forward)))
        ]
    (if (= up-down-forward "forward")
      {:aim new-aim,
       :depth (+ (:depth current-aim-and-position) (* value new-aim)),
       :position (+ (:position current-aim-and-position) value)}
      (assoc current-aim-and-position :aim new-aim)
      )
    )
  )

(defn get-position-with-aim
  [items]
  (reduce update-aim-and-position {:aim 0 :depth 0 :position 0} items))

(defn get-vertical-shift
  [items]
  (let [vertical-items (filter (fn [item] (contains? vertical-order-directions (first item))) items)]
    (reduce +
            (map (fn [item]
                   (* (vertical-order-directions (first item)) (read-string (second item))))
                 vertical-items))
    )
  )

(defn get-horisontal-shift
  [items]
  (let [horisontal-items (filter (fn [item] (contains? horisontal-order-directions (first item))) items)]
    (reduce +
            (map (fn [item]
                   (* (horisontal-order-directions (first item)) (read-string (second item))))
                 horisontal-items))
    )
  )

(defn -main
  "Advent of code 2021 day 2"
  [& args]
  (let [file-content (slurp "input.txt")
        lines (clojure.string/split file-content #"\n")
        items (map (fn [line] (clojure.string/split line #" ")) lines)
        vertical-shift (get-vertical-shift items)
        horisontal-shift (get-horisontal-shift items)
        position (get-position-with-aim items)
        ]
    (println "Part 1: " (* vertical-shift horisontal-shift))
    (println "Part 2: " (* (:depth position) (:position position)))
    ))

