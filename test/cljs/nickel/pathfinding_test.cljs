(ns nickel.pathfinding-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [nickel.pathfinding :as p]))

; example board
; [ A # # # ]
; [ B C D E ]
; [ # F # G ]
; [ H I # J ]

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

(def example-coords #{A B C D E F G H I J})

(deftest shorten-paths-test
  (testing "shortening nil values"
    (let [paths {B (list A) C nil}
          result (p/shorten-paths paths B #{C})]
      (is (= (get result B) (list A)))
      (is (= (get result C) (list B A)))))
  (testing "shortening longer paths"
    (let [paths {B (list A) C (list G E D)}
          result (p/shorten-paths paths B #{C})]
      (is (= (get result B) (list A)))
      (is (= (get result C) (list B A)))))
  (testing "keeping shorter or equal paths"
    (let [paths {B (list A) C (list F) D (list E G)}
          result (p/shorten-paths paths B #{C D})]
      (is (= (get result B) (list A)))
      (is (= (get result C) (list F)))
      (is (= (get result D) (list E G))))))

(deftest dijkstra-test
  (testing "retrieving shortest paths from source"
    (let [result (p/shortest-paths example-coords D)]
      (is (= (get result A) [D C B A]))
      (is (= (get result B) [D C B]))
      (is (= (get result C) [D C]))
      (is (= (get result D) [D]))
      (is (= (get result E) [D E]))
      (is (= (get result F) [D C F]))
      (is (= (get result G) [D E G]))
      (is (= (get result H) [D C F I H]))
      (is (= (get result I) [D C F I]))
      (is (= (get result J) [D E G J]))))

  (testing "retrieving shortest paths with unreacheable nodes"
    (let [unreacheable-coords (disj example-coords G)
          result (p/shortest-paths unreacheable-coords D)]
      (is (= (get result A) [D C B A]))
      (is (= (get result B) [D C B]))
      (is (= (get result C) [D C]))
      (is (= (get result D) [D]))
      (is (= (get result E) [D E]))
      (is (= (get result F) [D C F]))
      ; G is no longer a node
      (is (= (get result H) [D C F I H]))
      (is (= (get result I) [D C F I]))
      (is (= (get result J) nil)))))
