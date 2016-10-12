(ns nickel.core
  (:require [nickel.state :as game]
            [reagent.core :as reagent]))

(def display-width 640)

(defn coords-to-style [x y]
  (let [increment (/ display-width game/board-size)]
    {:width increment
     :height increment
     :bottom (* y increment)
     :left (* x increment)}))

(defn tile-class [content x y]
  (str "tile tile-" content))

(defn entity-component [x y]
  [:div {:className "entity"
         :style (coords-to-style x y)}])

(defn cell-component [cell-content x y]
  [:div {:className "cell"}
   [:div {:className (tile-class cell-content x y)
          :on-mouse-enter #(game/set-highlight-position [x y])
          :on-click #(game/set-player-position [x y])}]])

(defn row-component [y row-content]
  [:div
   {:className "row"}
   (map-indexed (fn [x cell-content]
                  ^{:key (str "cell-x" x ",y" y)}
                  [cell-component cell-content x y])
                row-content)])

(defn board-component [state]
  [:div
   {:id "board"
    :on-mouse-leave #(game/set-highlight-position nil)}
   (apply entity-component (:player state))
   (map-indexed (fn [y row-content]
                  ^{:key (str "row-" y)}
                  [row-component y row-content])
                (:board state))])

(defn home-page []
  [:div
   [:h2 "Some Game"]
   [:p "it is the shit!"]
   [board-component @game/state]
   [:a {:on-click game/reset} "reset"]])

(defn init! []
  (reagent/render [home-page] (.getElementById js/document "app")))
