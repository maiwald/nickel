(ns nickel.pathfinding
  (:require [clojure.set :refer [intersection]]))

(defn neighbours [[x y]]
  #{
    [(+ x 1) y]
    [(- x 1) y]
    [x (+ y 1)]
    [x (- y 1)]
    })

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

(defn ^boolean no-visitable-coords-left? [paths nodes]
  (or (empty? nodes)
      (every? nil? (map #(get paths %1) nodes))))

(defn format-result [paths]
  (reduce-kv (fn [m k v]
               (assoc m k (if (nil? v) v (-> (conj v k) reverse vec)))) {} paths))

(defn shortest-paths [visitable-coords source]
  (loop [nodes visitable-coords
         paths (assoc (zipmap nodes (repeat nil)) source (list))]
    (if (no-visitable-coords-left? paths nodes)
      (format-result paths)
      (let [node (closest-node paths nodes)
            neighbours (intersection visitable-coords (neighbours node))]
        (recur
          (disj nodes node)
          (shorten-paths paths node neighbours))))))
