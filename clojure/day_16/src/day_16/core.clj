(ns day-16.core
  (:gen-class)
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(def test-rules {
            :class (set (concat (range 1 4) (range 5 8)))
            :row (set (concat (range 6 12) (range 33 45)))
            :seat (set (concat (range 13 41) (range 45 51)))
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
   :price              (set (concat (range 42 312) (range 335 970)))
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
(def test-ticket
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
          (str/split-lines (slurp "day-16-input.txt")))]
    (println (reduce + (apply concat (get-answer rules other-tickets))))

    )
  )
