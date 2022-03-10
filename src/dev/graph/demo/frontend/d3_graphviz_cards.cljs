(ns graph.demo.frontend.d3-graphviz-cards
  (:require
   [reagent.core :as reagent] ; needed for dc/defcard-rg
   [devcards.core :as dc]
   ["d3" :as d3]
   ["d3-graphviz" :as d3-graphviz]
   [graph.demo.frontend.d3-in-reagent :as d3-in-reagent]
   ))

(dc/defcard-doc
  "# d3-graphviz examples")

(defn render [props]
  [:div#graph])

(defn container-enter
  [props]
  (let [graphviz d3-graphviz]
    (.renderDot (.graphviz graphviz "#graph")
                "digraph {a -> b; b -> c; c -> a}")))

(defn did-mount [props]
  (container-enter props))

(dc/defcard-rg graph-abc
  "A -> B -> C -> A graph rendered through d3-graphviz"
  (fn [data-atom _]
    (reagent/create-class
     {:reagent-render #(render data-atom)
      :component-did-mount #(did-mount data-atom)
      :component-did-update #(render data-atom)}
     )
    )
  (atom {:id "graph"})
  {:inspect-data true :history true})
