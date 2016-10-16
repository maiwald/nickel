(ns nickel.board)

(defn build-board []
  (into [] (reverse
             [[ 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ]
              [ 0 1 0 1 1 1 1 1 1 1 1 0 1 1 1 1 1 1 1 0 ]
              [ 0 1 1 1 0 0 0 0 0 0 1 1 1 0 0 0 0 0 0 0 ]
              [ 0 1 0 1 0 0 1 1 1 1 1 0 1 0 0 1 1 1 1 0 ]
              [ 0 0 0 1 1 1 1 0 0 1 0 0 1 1 1 1 0 1 0 0 ]
              [ 0 0 1 1 0 0 1 0 0 1 0 1 1 0 0 1 0 1 0 0 ]
              [ 0 1 1 1 1 0 1 0 0 1 0 1 1 1 0 1 0 1 0 0 ]
              [ 0 1 0 1 1 0 1 1 1 1 0 0 1 1 0 1 1 1 1 0 ]
              [ 0 1 0 1 1 0 1 1 1 1 0 0 1 1 0 1 1 1 1 0 ]
              [ 0 0 0 0 0 0 1 0 0 1 0 0 0 0 0 0 0 0 0 0 ]
              [ 0 1 0 1 1 1 1 1 1 1 1 0 1 1 1 1 1 1 1 0 ]
              [ 0 1 1 1 0 0 0 0 0 0 1 1 1 0 0 0 0 0 0 0 ]
              [ 1 1 0 1 0 0 1 1 1 1 1 0 1 0 0 1 1 1 1 0 ]
              [ 1 0 0 1 1 1 1 0 0 1 0 0 1 1 1 1 0 1 0 0 ]
              [ 1 0 1 1 0 0 1 0 0 1 0 1 1 0 0 1 0 1 0 0 ]
              [ 1 0 1 1 1 0 1 0 0 1 0 1 1 1 0 1 0 1 0 0 ]
              [ 1 0 1 1 1 0 1 0 0 1 0 0 0 1 0 1 0 1 0 0 ]
              [ 1 0 0 1 1 0 1 0 0 1 0 0 0 1 0 1 0 1 1 0 ]
              [ 1 0 0 1 1 0 1 1 1 1 0 0 1 1 0 1 1 1 1 0 ]
              [ 1 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 ]
              ])))

(defn size [board]
  (count board))

(defn get-tile [board [x y]]
  (get-in board [y x]))

(defn set-tile [board [x y] value]
  (assoc-in board [y x] value))

(defn coord-visitable? [board coord]
  (= 1 (get-tile board coord)))

(defn map-cells [f board]
  (map-indexed
    (fn [y row]
      (map-indexed
        (fn [x cell]
          (f (get-tile board [x y]) x y))
        row))
    board))
