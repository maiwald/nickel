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

(defn entity-component [entity-type [x y]]
  [:div {:className (str "entity entity-" entity-type)
         :style (coords-to-style x y)}])

(defn cell-class [cell-type]
  (let [texture-map {0 "wall"
                     1 "path"}]
  (str "tile tile-" (get texture-map cell-type))))

(defn cell-component [cell-type x y]
  [:div {:className "cell"}
   [:div {:className (cell-class cell-type)
          :on-mouse-enter #(update-state! game/set-highlight-position [x y])
          :on-click #(update-state! game/set-player-position [x y])}]])

(defn row-component [y row-content]
  [:div
   {:className "row"}
   (map-indexed (fn [x cell-type]
                  ^{:key (str "cell-x" x ",y" y)}
                  [cell-component cell-type x y])
                row-content)])

(defn highlight-component [{:keys [position ^boolean in-range?]}]
  ^{:key (str "highlight-" position)}
  [:div {:className (str "tile-highlight tile-highlight-"
                         (if in-range? "in-range" "not-in-range"))
         :style (apply coords-to-style position)}])

(defn board-component [{:keys [player-position enemy-positions board highlights]}]
  [:div
   {:id "board"
    :on-mouse-leave #(update-state! game/set-highlight-position nil)}
   [entity-component "player" player-position]
   (map-indexed (fn [index position]
                  ^{:key (str "enemy-" index)}
                  [entity-component "enemy" position])
                enemy-positions)
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
