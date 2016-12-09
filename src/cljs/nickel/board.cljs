(ns nickel.board)

(defn build-board []
  (into [] (reverse
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

(defn size [board]
  (count board))

(defn get-tile [board [x y]]
  (get-in board [y x]))

(defn set-tile [board [x y] value]
  (assoc-in board [y x] value))

(defn ^boolean is-path? [board coord]
  (= 1 (get-tile board coord)))

(defn ^boolean is-wall? [board coord]
  (not (is-path? board coord)))

(defn filtered-coords [board filter-fn]
  (let [board-size (size board)]
    (set (for [x (range board-size)
               y (range board-size)
               :when ^boolean (filter-fn [x y])]
           [x y]))))

