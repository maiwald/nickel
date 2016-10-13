(ns nickel.game
  (:require [nickel.board :as board]
            [nickel.pathfinding :refer [shortest-paths]]))

(def initial-state
  {:board (board/build-board)
   :highlight-position nil
   :player-position [0 0]})

(defn set-player-position [state coord]
  (if (board/coord-visitable? (:board state) coord)
    (assoc state :player-position coord)
    state))

(defn set-highlight-position [state coord]
  (assoc state :highlight-position coord))

(defn view-state [state]
  (let [{:keys [board highlight-position player-position]} state
        paths (shortest-paths board player-position)]))
