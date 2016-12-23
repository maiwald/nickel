(ns nickel.board
  (:require [clojure.set :refer [union]]))

(def test-board-proto
  (vec (reverse
         [[ 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ]
          [ 0 1 0 1 1 1 1 1 1 1 1 0 1 1 1 1 1 1 1 0 ]
          [ 0 1 1 1 0 0 0 0 0 0 1 1 1 0 0 0 1 0 0 0 ]
          [ 0 1 0 1 0 0 1 1 1 1 1 0 1 0 0 1 1 1 1 0 ]
          [ 0 0 0 1 1 1 1 0 0 1 0 0 1 1 1 1 0 1 0 0 ]
          [ 0 0 1 1 0 0 1 0 0 1 0 1 1 0 0 1 0 1 0 0 ]
          [ 0 1 1 1 1 0 1 0 0 1 1 1 1 1 0 1 0 1 0 0 ]
          [ 0 1 0 1 1 0 1 1 1 1 0 0 1 1 0 1 1 1 1 0 ]
          [ 0 1 0 1 1 0 1 1 1 1 0 0 1 1 0 1 1 1 1 0 ]
          [ 0 0 0 0 1 0 1 0 0 1 0 0 0 0 0 1 0 0 0 0 ]
          [ 0 1 0 1 1 1 1 1 1 1 1 0 1 1 1 1 1 1 1 0 ]
          [ 0 1 1 1 0 0 0 0 0 0 1 1 1 0 0 0 0 0 1 0 ]
          [ 1 1 0 1 0 0 1 1 1 1 1 0 1 0 0 1 1 1 1 0 ]
          [ 1 0 0 1 1 1 1 0 0 1 0 0 1 1 1 1 0 1 0 0 ]
          [ 1 0 1 1 0 0 1 0 0 1 0 1 1 0 0 1 0 1 0 0 ]
          [ 1 0 1 1 1 0 1 0 0 1 0 1 1 1 0 1 0 1 0 0 ]
          [ 1 0 1 1 1 0 1 1 0 1 0 0 0 1 0 1 0 1 0 0 ]
          [ 1 0 0 1 1 0 1 0 0 1 0 0 0 1 0 1 0 1 1 0 ]
          [ 1 0 0 1 1 1 1 1 1 1 0 0 1 1 1 1 1 1 1 0 ]
          [ 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ]
          ])))

(defn get-tile-value [board [x y]]
  (get-in board [y x]))

(defn convert-to-board [proto]
  (let [size (count proto)
        coords (set (for [x (range size) y (range size)] [x y]))]
    (reduce
      (fn [board coord]
        (let [target (case (get-tile-value test-board-proto coord)
                       0 :walls
                       1 :paths)]
          (update board target conj coord)))
      {:size size :paths #{} :walls #{}}
      coords)))

(defn build-board []
  (print (convert-to-board test-board-proto))
  (convert-to-board test-board-proto))

(defn size [board]
  (:size board))

(defn ^boolean is-path? [board coord]
  (contains? (:paths board) coord))

(defn ^boolean is-wall? [board coord]
  (contains? (:walls board) coord))

(defn filtered-coords [{:keys [paths walls]} filter-fn]
  (filter filter-fn (union paths walls)))
