(ns graph.demo.frontend.app
  (:require [rum.core :as rum]
            ["d3" :as d3]
            ["dagre-d3" :as dagreD3]))

(comment
  {:nodes [["A" {:label "A start"}] ["B"] ["C"] ["D"]]
   :edges [["A" "A" {:label "Self"}]
           ["A" "B" {:label "A to B"}]
           ["A" "C" {:label "A to C"}]
           ["A" "C"]]}
)


(defn add-nodes! [graph nodes]
  "Given a dagreD3 `graph` object and list of node definitions in `nodes` 
   add all nodes to the given graph and return mutated graph instance back"
  (loop [h (first nodes)
         r (rest nodes)]
    (let [[id props] h]
      (.setNode graph id (clj->js (or props {}))))
    (if (empty? r) graph
        (recur (first r) (rest r)))))


(defn add-edges! 
  "Given a dagreD3 `graph` object and list of edge definitions in `xs` 
   add all edges to the given graph and return mutated graph instance back"
  [graph xs]
  (loop [h (first xs)
         r (rest xs)]
    (let [[a b props] h]
      (.setEdge graph a b (clj->js (or props {}))))
    (if (empty? r) graph
        (recur (first r) (rest r)))))


(defn add-graph
  "Create dagreD3 graph from `graph-def` and bound it to the element with `el-id`"
  [graph-def el-id]
  (let [g (-> dagreD3
              (aget "graphlib")
              (aget "Graph")
              (new)
              (.setGraph (clj->js {}))
              (.setDefaultEdgeLabel (clj->js {}))

              (add-nodes! (:nodes graph-def))
              (add-edges! (:edges graph-def)))

        render (.render dagreD3)

        svg (.select d3 (str "#" el-id))]

    (.call svg render g)

    (let [svg-width (.attr svg "width")
          graph-width (aget (.graph g) "width")
          xCenterOffset (/ (- svg-width graph-width) 2)]
      (.attr svg
             "transform"
             (str "translate(" xCenterOffset ", 20)"))
      (.attr svg "height" (+ 40 (aget (.graph g) "height"))))))


(rum/defc div-svg
  [id]  
  [:div 
   [:p (str "Graph with id: " id)]
   [:svg {:id id :width 320 :height 400}]])


(defn ^:dev/after-load init []
  (println "Hello updated World")

  (rum/mount [(div-svg "graph")
              (div-svg "other-graph")]
             (.getElementById js/document "root"))

  (add-graph {:nodes [["A" {:label "A start"}] ["B"] ["C"] ["D"]]
              :edges [["A" "A" {:label "Self"}]
                      ["A" "B" {:label "A to B"}]
                      ["A" "C" {:label "A to C"}]
                      ["A" "D"]]}
             "graph")
  (add-graph {:nodes [["A"] ["B"] ["C"]]
              :edges [["A" "B"]]}
             "other-graph")
  
  )