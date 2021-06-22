(ns graph.demo.frontend.graph-cards
  (:require
   [graph.demo.frontend.graph :as graph]
   [graph.demo.frontend.d3-in-reagent :as d3-in-reagent]
   [graph.demo.frontend.reagent :as graph-rg]
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

(declare wrapper)
(dc/defcard-rg wrapper
  "Checking that wrapper graph-rg/graph works correctly"

  (fn [data-atom _]
    [graph-rg/graph data-atom])

  (reagent.core/atom {:div-id "wrapper"
                      :graph-def {:nodes [["A"] ["B"]]
                                  :edges [["A" "B"]]}})
  {:inspect-data true :history true})

(declare custom-style-graph)
(dc/defcard-rg custom-style-graph
  "Styles passed in properties are applied to graph"

  (fn [data-atom _]
    [graph-rg/graph data-atom])

  (reagent.core/atom {:div-id "custom-style-graph"
                      :graph-def {:nodes [["A" {:label "a"}] ["B" {:style "fill: #afa"}]]
                                  :edges [["A" "B" {:style "stroke: #f66; stroke-width: 3px; stroke-dasharray: 5, 5;"
                                                    :arrowheadStyle "fill: #f66"}]]}})
  {:inspect-data true :history true})



(declare interactive-nodes)
(dc/defcard-rg interactive-nodes
  "Nodes can be added and removed interactively"
  (fn [data-atom _]
    [:div
     [:button {:onClick
               (fn [] (swap! data-atom
                             update-in
                             [:graph-def :nodes]
                             (fn [oldv args]
                               (if (= ["C"] (last oldv))
                                 [["A"] ["B"]]
                                 [["A"] ["B"] ["C"]]))))
               :style {:margin-bottom "10px"}}
      "Add/Remove \"C\" Node"]
     [graph-rg/graph data-atom
      data-atom]])
  (reagent.core/atom {:div-id "interactive"
                      :graph-def {:nodes [["A"] ["B"]]
                                  :edges [["A" "B"]]}})
  {:inspect-data true :history true})

(declare big-graph)
(dc/defcard-rg big-graph
  "When given graph is bigger than defaults svg h/w values, svg will be scaled to match"
  (fn [data-atom _]
    [graph-rg/graph data-atom])
  (reagent.core/atom {:div-id "big-graph"
                      :graph-def {:nodes [["A"] ["B"] ["C"] ["D"] ["E"]]
                                  :edges [["A" "B"] ["A" "C"] ["B" "C"] 
                                          ["B" "D"] ["C" "E"] ["D" "E"]]}})
  {:inspect-data true :history true})


