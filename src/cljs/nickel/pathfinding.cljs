(ns nickel.pathfinding
  (:require [nickel.board :refer [coord-visitable?]]
            [clojure.set :refer [intersection]]))

(def visitable-coords (memoize (fn [board]
  (let [board-size (count board)]
    (set (for [x (range board-size)
               y (range board-size)
               :when (coord-visitable? board [x y])]
           [x y]))))))

(defn neighbours [[x y]]
  #{
    [(+ x 1) y]
    [(- x 1) y]
    [x (+ y 1)]
    [x (- y 1)]
    })

(defn visitable-neighbours [board position]
  (intersection
    (visitable-coords board)
    (neighbours position)))

(defn closest-node [paths nodes]
  (let [node-length #(if (seq? (get paths %))
                        (count (get paths %))
                        js/Infinity)]
    (apply min-key node-length nodes)))

(defn shorten-paths [paths node neighbours]
  (reduce
    (fn [paths neighbour]
      (let [current-path (get paths neighbour)
            alternate-path (conj (get paths node) node)]
        (if (or (nil? current-path)
                (< (count alternate-path) (count current-path)))
          (assoc paths neighbour alternate-path)
          paths)))
    paths
    neighbours))

(defn dijkstra [board source]
  (loop [nodes (visitable-coords board)
         paths (assoc (zipmap nodes (repeat nil)) source (list))]
    (if (or (empty? nodes)
            (every? nil? (vals (select-keys paths nodes))))
      paths
      (let [node (closest-node paths nodes)
            neighbours (visitable-neighbours board node)]
        (recur
          (disj nodes node)
          (shorten-paths paths node neighbours))))))

(def shortest-paths (memoize dijkstra))
