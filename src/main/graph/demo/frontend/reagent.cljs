(ns graph.demo.frontend.reagent
  (:require [graph.demo.frontend.graph :as graph]
            [reagent.core :as reagent]))

(defn as-component
  [state-atom {:keys [render mount update]}]
  (reagent/create-class
   {:reagent-render (fn [this]
                      (render state-atom))
    :component-did-mount (fn [this]
                           (mount state-atom))
    :component-did-update (fn [this old-argv old-state snapshot]
                            (js/console.log this)
                            (update state-atom))}))

(defn graph 
  [state-atom]
  [as-component
   state-atom
   {:render graph/graph-render-r
    :mount graph/graph-did-mount
    :update graph/graph-did-update}])