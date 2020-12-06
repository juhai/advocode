(ns day-3.core-test
  (:require [clojure.test :refer :all]
            [day-3.core :refer :all]))

(deftest test-get-map
  (testing "FIXME, I fail."
    (is (= (get-map [".#" "#."])
        {[0 1] 1, [1 0] 1}))
    ))

(deftest test-get-row-count
  (testing "Check that we get correct amount of rows"
    (is (= (get-row-count [1 2 3]) 3))
    ))

(deftest test-get-col-count
  (testing "Check that we get correct amount of cols"
    (is (= (get-col-count ["abcdef" "defghi" "ghijkl"]) 6))
    ))
