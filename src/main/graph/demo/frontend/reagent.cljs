(ns graph.demo.frontend.reagent
  (:require [graph.demo.frontend.graph :as graph]
            [reagent.core :as reagent]))

(defn as-component
  [state-atom {:keys [render mount update]}]
  (reagent/create-class
   {:reagent-render (fn [this]
                      (comment
                        (println "reagent-render:" this))
                      (render state-atom))
    :component-did-mount (fn [this]
                           (comment
                             (println "component-did-mount:" this)
                             (println "component-did-mount.state:" state-atom))
                           (mount state-atom))
    :component-did-update (fn [this old-argv old-state snapshot]
                            (comment
                              (println "component-did-update:" this)
                              (println "component-did-update.old-argv:" old-argv)
                              (println "component-did-update.old-state:" old-state)
                              (println "component-did-update.snapshot:" snapshot))
                            (update state-atom))}))

(defn graph 
  [state-atom]
  [as-component
   state-atom
   {:render graph/graph-render-r
    :mount graph/graph-did-mount
    :update graph/graph-did-update}])