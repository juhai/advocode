(ns day-16.core-test
  (:require [clojure.test :refer :all]
            [day-16.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= (rules-that-match-all-fields [1 2 3]
           {:a (set [1 2]) :b (set [2 3 4])})))
    ))
