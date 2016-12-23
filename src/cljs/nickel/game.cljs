(ns nickel.game
  (:require [nickel.board :as board]
            [nickel.pathfinding :refer [shortest-paths]]))

(def player-movement-limit 9)

(defn coord-visitable? [{:keys [board enemy-positions]} coord]
  (and
    (board/is-path? board coord)
    (not (contains? enemy-positions coord))))

(defn player-can-reach? [{:keys [player-paths]} coord]
  (>= player-movement-limit (count (get player-paths coord))))

(defn reduce-player-paths [{:keys [board player-position] :as state}]
  (let [visitable-coords (board/filtered-coords board #(coord-visitable? state %1))]
    (assoc state :player-paths (shortest-paths visitable-coords player-position))))

(def initial-state
  (let [state {:board (board/build-board)
               :enemy-positions #{[13 15] [6 14] [17 11]}
               :highlight-position nil
               :player-paths nil
               :player-position [0 0]
               }]
    (reduce-player-paths state)
   ))

(defn reduce-enemy-positions [{:keys [enemy-positions] :as state}]
  (assoc state :enemy-positions enemy-positions))

(defn set-player-position [state coord]
  (if (and
        (coord-visitable? state coord)
        (player-can-reach? state coord))
    (->> (assoc state :player-position coord)
         reduce-enemy-positions
         reduce-player-paths)
    state))

(defn set-highlight-position [state coord]
  (if (coord-visitable? state coord)
    (assoc state :highlight-position coord)
    (assoc state :highlight-position nil)))

(defn view-state [state]
  (let [{:keys [highlight-position player-paths]} state
        highlighted-coords (get player-paths highlight-position [])]
    (assoc state :highlights (map-indexed
                               (fn [index position]
                                 {:position position
                                  :in-range? (> player-movement-limit index)})
                               highlighted-coords))))
