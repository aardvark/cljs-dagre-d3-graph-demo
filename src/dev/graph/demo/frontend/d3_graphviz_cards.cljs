(ns graph.demo.frontend.d3-graphviz-cards
  (:require
   [reagent.core :as r] ; needed for dc/defcard-rg
   [devcards.core :as dc]
   ["d3-graphviz" :as d3-graphviz]
   ))

(dc/defcard-doc
  "# d3-graphviz examples")

(defn render [props]
  [:div {:id (:id @props)}])

(defn container-enter
  [props]
  (let [graphviz d3-graphviz
        g (.graphviz graphviz (str "#" (:id @props)))
        g (.zoom g false)]
    (.renderDot g
                (str "digraph {" (:graph @props) "}"))))

(defn did-mount [props]
  (container-enter props))

(dc/defcard-rg graph-abca
  "A -> B -> C -> A graph rendered through d3-graphviz"
  (fn [data-atom _]
    (r/create-class
     {:reagent-render #(render data-atom)
      :component-did-mount #(container-enter data-atom)
      :component-did-update #(render data-atom)}
     )
    )
  (atom {:id "graph1" :graph "a -> b -> c -> a"})
  {:inspect-data true :history true})

(defn atom-input [value]
  [:textarea {:value @value
              :on-change #(reset! value (-> % .-target .-value))}])

(defn container-enter2
  [props]
  (let [props @props
        id (:id props)
        graph (:graph props)
        type (:type props "digraph")
        graphviz d3-graphviz
        g (.graphviz graphviz (str "#" id))
        g (.zoom g false)]
    (.renderDot g
                (str type " { " @graph " }"))))

(defn graph-component 
  [div-id type graph]
  (let [g (fn [div-id type graph]
            (let [graphviz d3-graphviz
                  g (.graphviz graphviz (str "#" div-id))
                  g (.zoom g false)]
              (.renderDot g
                          (str type " { " graph " }"))))]

    (r/create-class
     {:display-name (str "graph-" div-id)

      :component-did-mount
      (fn [this]
        (g div-id type graph))

      :component-did-update
      (fn [this old-argv]
        (let [[div-id type graph] (rest (r/argv this))]
          (g div-id type graph)))

      :reagent-render
      (fn [div-id type graph]
        [:div {:id div-id}])})))


(dc/defcard-rg dynamic-graph
  "Graph with structure controlled from input"
  (fn [data-atom _]
    [:div
     [:label "Graph code:"]
     [:br]
     [atom-input (:graph @data-atom)]
     [:br]
     [:label "Graph: "]
     [graph-component (:id @data-atom) "digraph" @(:graph @data-atom)]])
    
  (r/atom {:id "graph2" :graph (r/atom "a")})
  {:inspect-data true :history true})
