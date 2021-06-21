(ns graph.demo.frontend.graph-cards
  (:require
   [graph.demo.frontend.graph :as graph]
   [graph.demo.frontend.d3-in-reagent :as d3-in-reagent]
   [reagent.core] ; needed for dc/defcard-rg
   [devcards.core :as dc]))

(dc/defcard-doc
  "# d3-dagre graph examples")

(declare graph-ab)
(dc/defcard-rg graph-ab
  "A -> B graph"
  (fn [data-atom _]
    [d3-in-reagent/wrap-in-reagent
     data-atom
     {:render graph/graph-render-r
      :mount graph/graph-did-mount
      :update graph/graph-did-update}])
  (atom {:div-id "ab"
         :graph-def {:nodes [["A"] ["B"]]
                     :edges [["A" "B"]]}})
  {:inspect-data true :history true})

(declare interactive-nodes)

(dc/defcard-rg interactive-nodes
  "Nodes can be added and removed interactively"
  (fn [data-atom _]
    [:div#wrapper
     [:button {:onClick 
               (fn [] (swap! data-atom
                             update-in
                             [:graph-def :nodes]
                             (fn [oldv args]
                               (conj oldv ["C"]))))}
      "Add Node"]
     [d3-in-reagent/wrap-in-reagent
      data-atom
      {:render graph/graph-render-r
       :mount graph/graph-did-mount
       :update graph/graph-did-update}]])
  (reagent.core/atom {:div-id "interactive"
         :graph-def {:nodes [["A"] ["B"]]
                     :edges [["A" "B"]]}})
  {:inspect-data true :history true})