(ns day-04.core-test
  (:require [clojure.test :refer :all]
            [day-04.core :refer :all]))

(deftest get-column-test
  (testing "get-column works"
    (is (= (list 0 1) (get-column 0 (list (list 0 1) (list 1 2)))))
    (is (= (list 1 2) (get-column 1 (list (list 0 1) (list 1 2)))))
    )
  )

(deftest get-row-test
  (testing "get-row works"
    (is (= (list 0 1 2) (get-row "0 1 2")))
    (is (= (list 0 1 2) (get-row " 0 1 2 ")))
    (is (= (list 0 1 2 3 4) (get-row "0 1 2 3 4")))
    (is (=
          (list (list 0 2) (list 1 3))
          (map-indexed get-column (repeat 2 (list (list 0 1) (list 2 3))))))
    )
  )
