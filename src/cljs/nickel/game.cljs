(ns nickel.game
  (:require [nickel.board :as board]
            [nickel.pathfinding :refer [shortest-paths]]))

(def initial-state
  (let [board (board/build-board)]
    {:board board
     :highlight-position nil
     :player-position [0 0]
     :enemy-positions (list
                        [18 18]
                        [6 14]
                        [17 11])
     }))

(defn set-player-position [state coord]
  (if (board/coord-visitable? (:board state) coord)
    (assoc state :player-position coord)
    state))

(defn set-highlight-position [state coord]
  (if (board/coord-visitable? (:board state) coord)
    (assoc state :highlight-position coord)
    (assoc state :highlight-position nil)))

(defn view-state [state]
  (let [{:keys [board highlight-position player-position]} state
        paths (shortest-paths board player-position)
        highlighted-coords (if (nil? highlight-position)
                             []
                             (conj (get paths highlight-position) highlight-position))]
    (assoc state :highlights (map-indexed
                               (fn [index position]
                                 {:position position
                                  :in-range? (< index 8)})
                               (reverse highlighted-coords)))))
