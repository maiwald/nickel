(ns nickel.pathfinding-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [nickel.pathfinding :as p :refer [shorten-paths]]))

(def example-board
  [[ 1 0 0 0 ]
   [ 1 1 0 1 ]
   [ 0 1 1 1 ]
   [ 1 1 0 1 ]])

(deftest foo-test
  (testing "foo"
    (is (= 1 1))
    (is true)))
