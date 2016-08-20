(ns nickel.core
  (:require [reagent.core :as reagent :refer [atom]]
            [reagent.session :as session]))

(defn build-board [n]
  (mapv vec (partition n (repeatedly (* n n) #(rand-int 2)))))

(defonce state
  (reagent/atom {:board (build-board 30)}))

(defn set-tile-value [x y value]
  (swap! state assoc-in [:board y x] value))

(defn reset []
  (swap! state assoc :board (build-board 30)))

(defn cell-component [cell-content x y]
  [:div {:className "cell"}
   [:div {:className (str "tile tile-" cell-content)
          :on-click #(set-tile-value x y (mod (inc cell-content) 2))}]])

(defn row-component [y row-content]
  [:div
   {:className "row"}
   (map-indexed (fn [x cell-content]
                  ^{:key (str "cell-" x y)}
                  [cell-component cell-content x y])
                row-content)])

(defn board-component [board]
  [:div
   {:className "board"}
   (map-indexed (fn [y row-content]
                  ^{:key (str "row-" y)}
                  [row-component y row-content])
                board)])

(defn home-page []
  [:div
   [:h2 "Some Game"]
   [:p "it is the shit!"]
   [board-component (:board @state)]
   [:a {:on-click reset} "reset"]])

(defn init! []
  (reagent/render [home-page] (.getElementById js/document "app")))
