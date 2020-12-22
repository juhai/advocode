(ns day-16.core
  (:gen-class)
  (:require [clojure.set :as set]
            [clojure.string :as str]))


(def test-rules-part2
  {
   :departure-class (set (concat (range 0 2) (range 4 20)))
   :row             (set (concat (range 0 6) (range 8 20)))
   :departure-seat  (set (concat (range 0 14) (range 16 20)))
   }
  )

(def my-part2-ticket [11, 12, 13])

(def other-part2-tickets
  [
   [3, 9, 18]
   [15, 1, 5]
   [5, 14, 9]
   ]
  )

(def test-rules {
                 :class (set (concat (range 1 4) (range 5 8)))
                 :row   (set (concat (range 6 12) (range 33 45)))
                 :seat  (set (concat (range 13 41) (range 45 51)))
                 })

(def rules
  {
   :departure-location (set (concat (range 50 689) (range 707 967)))
   :departure-station  (set (concat (range 33 341) (range 351 961)))
   :departure-platform (set (concat (range 42 80) (range 105 955)))
   :departure-track    (set (concat (range 46 929) (range 943 960)))
   :departure-date     (set (concat (range 42 465) (range 482 975)))
   :departure-time     (set (concat (range 25 596) (range 614 973)))
   :arrival-location   (set (concat (range 26 484) (range 494 963)))
   :arrival-station    (set (concat (range 31 902) (range 913 958)))
   :arrival-platform   (set (concat (range 35 722) (range 736 959)))
   :arrival-track      (set (concat (range 44 640) (range 661 961)))
   :class              (set (concat (range 45 392) (range 416 970)))
   :duration           (set (concat (range 46 168) (range 186 963)))
   :price              (set (concat (range 42 313) (range 335 970)))
   :route              (set (concat (range 36 370) (range 375 972)))
   :row                (set (concat (range 46 871) (range 877 973)))
   :seat               (set (concat (range 49 837) (range 846 962)))
   :train              (set (concat (range 50 443) (range 450 971)))
   :type               (set (concat (range 37 707) (range 715 953)))
   :wagon              (set (concat (range 45 675) (range 687 963)))
   :zone               (set (concat (range 40 199) (range 219 964)))
   }
  )

(def my-test-ticket [7, 1, 14])
(def my-real-ticket
  (map #(Integer/parseInt %) (str/split "151,139,53,71,191,107,61,109,157,131,67,73,59,79,113,167,137,163,149,127" #","))
  )

(def other-test-tickets
  [
   [7, 3, 47]
   [40, 4, 50]
   [55, 2, 20]
   [38, 6, 12]
   ])

(defn get-invalid-numbers [rules ticket]
  (let [all-rules (apply set/union (vals rules))
        ticket-set (set ticket)]
    (set/difference ticket-set all-rules))
  )

(defn is-valid-ticket [rules ticket]
  (let [all-rules (apply set/union (vals rules))
        ticket-set (set ticket)]
    (zero? (count (set/difference ticket-set all-rules)))
    ))

(defn get-answer [rules tickets]
  (flatten (map (partial get-invalid-numbers rules) tickets))
  )

(defn rules-that-match-all-fields [field-values-set rules]
  (into {} (filter #(empty? (set/difference field-values-set (val %))) rules))
  )

(defn resolve-field-indices [tickets rules]
  "Go through rules in random order and and recursively go through the rest of the rules for the remaining items in the tickets"
  (let [matches (rules-that-match-all-fields (map first tickets) rules)]
    (println "Matches" matches "for fields" (map first tickets))
    (if (empty? matches)
      nil
      (apply concat
             (map #(conj (resolve-field-indices (map rest tickets) (dissoc rules (key %))) %) matches))
      )
    )
  )

(defn resolve-fields-with-stats [order rule-stats]
  (let [num-fields (count rule-stats)]
    (loop [queue [{:keys [] :current 0}]]
      (let [current (first queue)
            current-field-index (nth order (current :current))
            matches (apply (partial disj (set (rule-stats current-field-index))) (set (current :keys)))
            num-matches (count matches)]
        (cond
          (zero? num-matches) (recur (rest queue))
          (= (dec num-fields) (current :current))
          (map second (sort-by first (map vector order (conj (current :keys) (first matches)))))
          :else
          (recur (concat (map (fn [m] {:keys (conj (current :keys) m) :current (inc (current :current))})
                              matches) (rest queue))))
        )
      ))
  )

(defn get-stats [tickets rules]
  (map count (for [n (range (count (first tickets)))
                   :let [nth-tickets (set (map (fn [t] (nth t n)) tickets))]]
               (rules-that-match-all-fields nth-tickets rules
                                            ))
       ))

(defn get-stats-rules [tickets rules]
  (apply (partial merge-with into)
         (for [n (range (count (first tickets)))
               [k v] rules
               :let [nth-tickets (set (map (fn [t] (nth t n)) tickets))]
               :when (not-empty (rules-that-match-all-fields nth-tickets {k v}))]
           {n [k]}
           ))
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!")
  (println test-rules)
  (println my-test-ticket)
  (println (first other-test-tickets))
  (println (reduce + (apply concat (get-answer test-rules other-test-tickets))))
  (println (filter (partial is-valid-ticket test-rules) other-test-tickets))

  (let [other-tickets
        (map
          (fn [line]
            (map (fn [value] (Integer/parseInt value)) (str/split line #",")))
          (str/split-lines (slurp "day-16-input.txt")))
        rule-stats (get-stats-rules (filter (partial is-valid-ticket rules) other-tickets) rules)
        optimised-order (map first (sort-by second (seq rule-stats)))
        field-order (resolve-fields-with-stats optimised-order rule-stats)
        my-ticket-check-code (reduce * (map first (filter
                                                    (fn [d] (str/starts-with? (second d) ":departure"))
                                                    (map vector my-real-ticket field-order))))]
    (println (reduce + (apply concat (get-answer rules other-tickets))))
    (println "Start of PART II")
    (println (get-stats (filter (partial is-valid-ticket rules) other-tickets) rules)
             (reduce * (get-stats (filter (partial is-valid-ticket rules) other-tickets) rules)))
    (println "Optimised order" optimised-order)
    (println rule-stats)
    (println "Part II real deal" field-order)
    (println "<My ticket check code>" my-ticket-check-code)
    )
  )
