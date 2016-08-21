(ns nickel.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]))

(def board-size 20)
(def board-width 640)

(defn build-board []
  (mapv vec (partition board-size (repeatedly (* board-size board-size) #(rand-int 2)))))

(def initial-state {:board (build-board)
                    :player [0 0]})

(defonce state (reagent/atom initial-state))

(defn set-player-position [x y]
  (swap! state assoc :player [x y]))

(defn reset []
  (reset! state initial-state))

(defn coords-to-style [x y]
  (let [increment (/ board-width board-size)]
    {:width increment
     :height increment
     :top (* y increment)
     :left (* x increment)}))

(defn entity-component [x y]
  [:div {:className "entity"
         :style (coords-to-style x y)}])

(defn cell-component [cell-content x y]
  [:div {:className "cell"}
   [:div {:className (str "tile tile-" cell-content)
          :on-click #(set-player-position x y)}]])

(defn row-component [y row-content]
  [:div
   {:className "row"}
   (map-indexed (fn [x cell-content]
                  ^{:key (str "cell-" x y)}
                  [cell-component cell-content x y])
                row-content)])

(defn board-component [state]
  [:div
   {:id "board"}
   [entity-component
    (first (:player state))
    (second (:player state))]
   (map-indexed (fn [y row-content]
                  ^{:key (str "row-" y)}
                  [row-component y row-content])
                (:board state))])

(defn home-page []
  [:div
   [:h2 "Some Game"]
   [:p "it is the shit!"]
   [board-component @state]
   [:a {:on-click reset} "reset"]])

(defn init! []
  (reagent/render [home-page] (.getElementById js/document "app")))
