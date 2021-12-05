(ns day-04.core)

(defrecord Bingo-Line [values])

(defrecord Board [idx bingo-rows bingo-columns])

(defn get-row
  [row-data]
  (map read-string (clojure.string/split (clojure.string/trim row-data) #" +"))
  )

(defn get-column
  [idx rows]
  (map (fn [row] (nth row idx)) rows)
  )

(defn get-board
  [idx board-def]
  (let [rows (map get-row (clojure.string/split board-def #"\n"))
        columns (map-indexed get-column (repeat (count rows) rows))
        ]
    (Board. idx (map set rows) (map set columns))
    )
  )

(defn is-winner?
  [^Board board]
  (or
    (some (fn [bingo-line] (empty? bingo-line)) (:bingo-rows board))
    (some (fn [bingo-line] (empty? bingo-line)) (:bingo-columns board))
  ))

(defn get-winning-boards
  [boards]
  (let [winning-boards (filter is-winner? boards)]
    (if (empty? winning-boards) nil winning-boards)))

(defn remove-number-from-line
  [number-to-remove bingo-line]
  (disj bingo-line number-to-remove)
  )

(defn remove-number-from-board
  [number-to-remove board]
  (Board.
    (:idx board)
    (map
      (partial remove-number-from-line number-to-remove)
      (:bingo-rows board))
    (map
      (partial remove-number-from-line number-to-remove)
      (:bingo-columns board))
    ))

(defn get-winner
  [numbers all-boards]
  (loop [remaining-numbers numbers
         boards all-boards
         last-called-number -1]
    (let [winning-board (get-winning-boards boards)]
      (if (empty? winning-board)
        (recur
          (rest remaining-numbers)
          (map (partial remove-number-from-board (first remaining-numbers)) boards)
          (first remaining-numbers))
        [(cons last-called-number remaining-numbers) (first winning-board)]
        )
      )
    )
  )

(defn get-total-score
  [last-called-number ^Board winner]
  (if (some empty? (:bingo-rows winner))
    (* last-called-number (reduce + (apply concat (:bingo-rows winner))))
    (* last-called-number (reduce + (apply concat (:bingo-columns winner))))
    )
  )

(defn get-score
  [numbers-and-tables]
  (let [numbers (map read-string (clojure.string/split (clojure.string/trim (first numbers-and-tables)) #","))
        boards (map-indexed get-board (rest numbers-and-tables))
        [remaining-numbers winner] (get-winner numbers boards)
        total-score (get-total-score (first remaining-numbers) winner)]
    total-score)
  )

(defn remove-boards
  [boards ^Board boards-to-be-removed]
  (let [board-indices-to-remove (set (map :idx boards-to-be-removed))]
    (filter (fn [board] (false? (contains? board-indices-to-remove (:idx board)))) boards)
    )
  )

(defn get-last-winner-score [numbers-and-tables]
  (let [all-numbers (map read-string (clojure.string/split (clojure.string/trim (first numbers-and-tables)) #","))
        all-boards (map-indexed get-board (rest numbers-and-tables))
       ]
    (loop [remaining-numbers all-numbers
           boards all-boards
           last-called-number -1]
      (let [winning-boards (get-winning-boards boards)]
        (cond
          (and (not (empty? winning-boards)) (= (count boards) 1))
          (get-total-score last-called-number (first winning-boards))
          (empty? winning-boards)
          (recur
            (rest remaining-numbers)
            (map (partial remove-number-from-board (first remaining-numbers)) boards)
            (first remaining-numbers))
          :else
          (recur
            (rest remaining-numbers)
            (map
              (partial remove-number-from-board (first remaining-numbers))
              (remove-boards boards winning-boards))
            (first remaining-numbers))
          )
        )
      )
    )
  )

(defn -main
  "Advent of code 2021 day 4"
  [& args]
  (let [file-content (slurp "input.txt")
        numbers-and-tables (clojure.string/split file-content #"\n\n")]
    ;; Part 1
    (println "Part 1: " (get-score numbers-and-tables))
    ;; Part 2
    (println "Part 2:" (get-last-winner-score numbers-and-tables))
    ))
