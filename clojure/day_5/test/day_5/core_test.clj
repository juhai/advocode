(ns day-5.core-test
  (:require [clojure.test :refer :all]
            [day-5.core :refer :all]))

(deftest a-test
  (testing "FIXME, I fail."
    (is (= (to-integer-foo "FFLL") 0))
    (is (= (to-integer-foo "FFLR") 1))
    (is (= (to-integer-foo "BFLR") 9))
    ))


(deftest b-test
  (testing "FIXME, I fail."
    (is (= (get-row-count "FFLL") 4))))
