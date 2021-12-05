(ns day-05.core-test
  (:require [clojure.test :refer :all]
            [day-05.core :refer :all])
  (:import (day_05.core Point Line)))

(deftest a-test
  (testing "Get points on a line"
    (is (=
          (get-points-in-line (Line. (Point. 0 0) (Point. 2 2)))
          (list (Point. 0 0) (Point. 1 1) (Point. 2 2))))
    (is (=
          (get-points-in-line (Line. (Point. 2 2) (Point. 0 0)))
          (list (Point. 2 2) (Point. 1 1) (Point. 0 0))))
    (is (=
          (get-points-in-line (Line. (Point. 2 0) (Point. 0 2)))
          (list (Point. 2 0) (Point. 1 1) (Point. 0 2))))
    ))
