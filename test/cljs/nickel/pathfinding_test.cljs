(ns nickel.pathfinding-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [nickel.board :refer [set-tile]]
            [nickel.pathfinding :as p]))

(def example-board
  (into [] (reverse
             [[ 1 0 0 0 ]     ; [ A # # # ]
              [ 1 1 1 1 ]     ; [ B C D E ]
              [ 0 1 0 1 ]     ; [ # F # G ]
              [ 1 1 0 1 ]]))) ; [ H I # J ]

(def A [0 3])
(def B [0 2])
(def C [1 2])
(def D [2 2])
(def E [3 2])
(def F [1 1])
(def G [3 1])
(def H [0 0])
(def I [1 0])
(def J [3 0])

(deftest visitable-neighbours-test
  (testing "retrieving visitable neighbours"
    (is (= (p/visitable-neighbours example-board D)
           #{C E}))
    (is (= (p/visitable-neighbours example-board C)
           #{B F D}))
    (is (= (p/visitable-neighbours example-board E)
           #{D G}))
    (is (= (p/visitable-neighbours example-board J)
           #{G}))))

(deftest shorten-paths-test
  (testing "shortening nil values"
    (let [paths {B (list A) C nil}
          result (p/shorten-paths paths B #{C})]
      (is (= result
             {B (list A) C (list B A)}))))
  (testing "shortening longer paths"
    (let [paths {B (list A) C (list G E D)}
          result (p/shorten-paths paths B #{C})]
      (is (= result
             {B (list A) C (list B A)})))))
  (testing "keeping shorter or equal paths"
    (let [paths {B (list A) C (list F) D (list E G)}
          result (p/shorten-paths paths B #{C D})]
      (is (= result
             {B (list A) C (list F) D (list E G)}))))

(deftest dijkstra-test
  (testing "retrieving shortest paths from source"
    (let [result (p/dijkstra example-board D)]
      (is (= result
             {A (list B C D)
              B (list C D)
              C (list D)
              D '()
              E (list D)
              F (list C D)
              G (list E D)
              H (list I F C D)
              I (list F C D)
              J (list G E D)}))))

  (testing "retrieving shortest paths with unreacheable nodes"
    (let [unreacheable-board (set-tile example-board G 0)
          result (p/dijkstra unreacheable-board D)]
      (is (= result
             {A (list B C D)
              B (list C D)
              C (list D)
              D '()
              E (list D)
              F (list C D)
              ; G (list E D) no longer a node
              H (list I F C D)
              I (list F C D)
              J nil})))))
