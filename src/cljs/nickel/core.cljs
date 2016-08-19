(ns nickel.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]))

(defn build-board [n]
  (partition n (repeatedly (* n n) #(rand-int 2))))

(defonce state
  (reagent/atom {:board (build-board 30)}))

(defn cell-component [cell-content]
  [:div
   {:className "cell"}
   [:div {:className (str "tile-" cell-content)}]])

(defn row-component [row-index row-content]
  [:div
   {:className "row"}
   (map-indexed (fn [cell-index cell-content]
                  ^{:key (str "cell-" row-index cell-index)}
                  [cell-component cell-content])
                row-content)])

(defn board-component [board]
  [:div
   {:className "board"}
   (map-indexed (fn [row-index row-content]
                  ^{:key (str "row-" row-index)}
                  [row-component row-index row-content])
                board)])

(defn reset []
  (swap! state assoc :board (build-board 30)))

(defn home-page []
  [:div
   [:h2 "Some Game"]
   [:p "it is the shit!"]
   [board-component (:board @state)]
   [:a {:on-click reset} "reset"]])

(defn init! []
  (reagent/render [home-page] (.getElementById js/document "app")))
