(ns nickel.test
  (:require [cljs.test :refer-macros [run-all-tests]])
  (:import nickel.pathfinding-test))

(defn run []
  (enable-console-print!)
  (run-all-tests))
