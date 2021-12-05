(ns day-05.core)

(defrecord Point [x y])
(defrecord Line [^Point start-point ^Point end-point])

(defn map-to-point [point-spec]
  (map read-string (clojure.string/split point-spec #","))
  )

(defn map-to-line [line-from-file]
  (let [points (clojure.string/split line-from-file #" -> ")
        start-point (map-to-point (first points))
        end-point (map-to-point (second points))]
    (Line.
      (Point. (first start-point) (second start-point))
      (Point. (first end-point) (second end-point))))
  )

(defn x-equals? [^Line line]
  (= (-> line :start-point :x) (-> line :end-point :x))
  )

(defn y-equals? [^Line line]
  (= (-> line :start-point :y) (-> line :end-point :y))
  )

(defn abs [value]
  (if (>= value 0) value (- value))
  )

(defn get-points-in-line [^Line line]
  (cond
    (x-equals? line)
    (let [x (-> line :start-point :x) ;; Same x
          y-start (-> line :start-point :y)
          y-end (-> line :end-point :y)
          max-y (max y-start y-end)
          min-y (min y-start y-end)]
      (map (fn [y] (Point. x y)) (range min-y (inc max-y))))
    (y-equals? line)  ;; same y
    (let [y (-> line :start-point :y)
          x-start (-> line :start-point :x)
          x-end (-> line :end-point :x)
          max-x (max x-start x-end)
          min-x (min x-start x-end)]
      (map (fn [x] (Point. x y)) (range min-x (inc max-x))))
    :else  ;; diagonal
      (let [x-shift (if (> (-> line :end-point :x) (-> line :start-point :x)) 1 -1)
            y-shift (if (> (-> line :end-point :y) (-> line :start-point :y)) 1 -1)
            x-diff (abs (- (-> line :start-point :x) (-> line :end-point :x)))
            x-start (-> line :start-point :x)
            y-start (-> line :start-point :y)
            ]
        (map
          (fn[shift] (Point. (+ x-start (* shift x-shift)) (+ y-start (* shift y-shift))))
          (range (inc x-diff))
        )
      )
    )
  )

(defn get-overlaps [lines]
  (let [all-points-in-lines (apply concat (map get-points-in-line lines))
        point-counts (frequencies all-points-in-lines)
        ]
    (count (filter (fn [point] (>= (val point) 2)) point-counts))
    )
  )

(defn -main
  "Advent of code 2021 day 5"
  [& args]
  (let [file-content (slurp "input.txt")
        file-lines (clojure.string/split file-content #"\n")
        lines (map map-to-line file-lines)
        valid-lines
        (filter
          (fn [line] (or (x-equals? line) (y-equals? line)))
          lines)]
    ;; Part 1
    (println "Part 1: " (get-overlaps valid-lines))
    ;; Part 2
    (println "Part 2: " (get-overlaps lines))
    ))
