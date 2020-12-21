(ns day-15.core-test
  (:require [clojure.test :refer :all]
            [day-15.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= (get-initial-memory '(1, 2, 3))
           {1 0, 2 1, 3 2}))))
