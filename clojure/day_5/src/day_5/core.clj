(ns day-5.core
  (:gen-class))

(require '[clojure.string :as str])

(defn get-row-count [lines]
  (count lines))

(defn to-integer-foo [b-string]
    (Integer/parseInt (str/replace (str/replace b-string #"[BR]" "1") #"[FL]" "0") 2)
  )

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [file-content (slurp "input.txt")
        lines (str/split file-content #"\n")]
    (println "Maximum seat ID is"
             (apply max (map to-integer-foo lines))
    )))
