(ns nickel.core
  (:require [nickel.game :as game]
            [nickel.board :as board]
            [reagent.core :as reagent]))

(defonce state
  (reagent/atom game/initial-state))

(defn reset []
  (reset! state game/initial-state))

(def display-width 640)
(def board-size (board/size (:board @state)))

(defn update-state! [f & args]
  (apply swap! state f args))

(defn coords-to-style [x y]
  (let [increment (/ display-width board-size)]
    {:width increment
     :height increment
     :bottom (* y increment)
     :left (* x increment)}))

(defn tile-class [texture]
  (str "tile tile-" texture))

(defn entity-component [x y]
  [:div {:className "entity"
         :style (coords-to-style x y)}])

(defn cell-component [cell-content x y]
  [:div {:className "cell"}
   [:div {:className (tile-class cell-content)
          :on-mouse-enter #(update-state! game/set-highlight-position [x y])
          :on-click #(update-state! game/set-player-position [x y])}]])

(defn row-component [y row-content]
  [:div
   {:className "row"}
   (map-indexed (fn [x cell-content]
                  ^{:key (str "cell-x" x ",y" y)}
                  [cell-component cell-content x y])
                row-content)])

(defn highlight-component [{:keys [position in-range?]}]
  ^{:key (str "highlight-" position)}
  [:div {:className (str "tile-highlight tile-highlight-"
                         (if in-range? "in-range" "not-in-range"))
         :style (apply coords-to-style position)}])

(defn board-component [{:keys [player-position board highlights]}]
  [:div
   {:id "board"
    :on-mouse-leave #(update-state! game/set-highlight-position nil)}
   (apply entity-component player-position)
   (map #(highlight-component %1) highlights)
   (map-indexed (fn [y row-content]
                  ^{:key (str "row-" y)}
                  [row-component y row-content])
                board)])

(defn home-page []
  [:div
   [:h2 "Some Game"]
   [:p "it is the shit!"]
   [board-component (game/view-state @state)]
   [:a {:on-click reset} "reset"]])


(defn init! []
  (reagent/render [home-page] (.getElementById js/document "app")))
