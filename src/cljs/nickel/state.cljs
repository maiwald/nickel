(ns nickel.state
  (:require [reagent.core :as reagent :refer [atom]]
            [nickel.board :as board]
            [nickel.pathfinding :refer [shortest-paths]]))

(def initial-state
  {:board (board/build-board)
   :highlight-position nil
   :player-position [0 0]})

(defonce state
  (reagent/atom initial-state))

(defn reset []
  (reset! state initial-state))

(def board-size
  (board/size (:board @state)))

(defn set-player-position [coord]
  (if (board/coord-visitable? (:board @state) coord)
    (swap! state assoc :player-position coord)))

(defn set-highlight-position [coord]
  (if (board/coord-visitable? (:board @state) coord)
    (swap! state assoc :highlight-position coord)))

(defn view-state []
  (let [{:keys [board highlight-position player-position]} @state
        paths (shortest-paths board player-position)]))
