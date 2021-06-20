(ns graph.demo.frontend.graph-cards
  (:require
   [graph.demo.frontend.graph :as graph]
   [graph.demo.frontend.d3-in-reagent :as d3-in-reagent]
   [reagent.core] ; needed for dc/defcard-rg
   [devcards.core :as dc]))

(dc/defcard-doc
  "# Markdown
   d3-dagre graph examples")

(declare graph-ab)

(comment)
(dc/defcard-rg graph-ab
  (fn [data-atom _]
    [d3-in-reagent/wrap-in-reagent
     data-atom
     {:render graph/graph-render-r
      :mount graph/graph-did-mount
      :update graph/graph-did-update}])
  (atom {:div-id "ab"
         :graph-def {:nodes [["A"] ["B"] ["C"]]
                     :edges [["A" "B"]]}})
  {:inspect-data true :history true})