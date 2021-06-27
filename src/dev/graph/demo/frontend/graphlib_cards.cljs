(ns graph.demo.frontend.graphlib-cards
  (:require
   [reagent.core] ; needed for dc/defcard-rg
   [devcards.core :as dc]
   [cljs.test :refer [testing is]]
   ["graphlib" :as graphlib]
   [graph.demo.frontend.graph :as graph]))

(dc/defcard-doc
  "# Graphlib interop
   There are some api calls on how to create graph using graphlib")

(defn dump-graph [g]
  (let [json (.-json graphlib)
        g-as-json (.write json g)]
    (js->clj g-as-json :keywordize-keys true)))

(dc/deftest graph-api
  (testing "Empty graph creation"
    (is (=
         (js->clj (.write (.-json graphlib) (new (.-Graph graphlib))) :keywordize-keys true)
         {:options {:directed true, :multigraph false, :compound false}, :nodes [], :edges []})))
  (testing "Creating graph with two nodes and one edge"
    (is (=
         (let [g (new (.-Graph graphlib))
               g (.setNode g "a")
               g (.setNode g "b")
               g (.setEdge g "a" "b" "a->b")
               json (.-json graphlib)
               g-as-json (.write json g)]
           (js->clj g-as-json :keywordize-keys true))

         {:options {:directed true, :multigraph false, :compound false}
          :nodes [{:v "a"} {:v "b"}]
          :edges [{:v "a" :w "b" :value "a->b"}]})))
  (testing "dump graph fn"
    (let [g (new (.-Graph graphlib))
          g (.setNode g "a")
          g (.setNode g "b")
          g (.setEdge g "a" "b" "a->b")]
      (is (= (let [json (.-json graphlib)
                   g-as-json (.write json g)]
               (js->clj g-as-json :keywordize-keys true))
             (dump-graph g))))))


(dc/defcard-doc
  "# API alignment
   Trying align js and cljs api calls to create identical graph objects")






(dc/deftest graph-likeness
  (testing "direct graplib calls and `graph/gagre-graph` generate kinda same graph"
    (is (=
         (let [g (new (.-Graph graphlib))
               g (.setNode g "a" (clj->js {:label "a" :shape "rect"}))
               g (.setNode g "b" (clj->js {:label "b" :shape "rect"}))
               g (.setEdge g "a" "b" "a->b")]
           (dump-graph g))
         (dump-graph (graph/dagre-graph {:nodes [["a"] ["b"]]
                                         :edges [["a" "b" "a->b"]]}))))))


(dc/defcard-doc
  "# Graph queries 
   g1 graph in next tests is created through graphlib as: "

  ;; TODO figure out why it is not working as it should. 
  ;; we should not wrap this in a do and don't need to actually move defition in the doc
  (do
    (defn g-graphlib []
      (let [g (new (aget graphlib "Graph"))
            _ (.setNode g "a" {:label "a"})
            _ (.setNode g "b" "b")
            _ (.setEdge g "a" "b" "a->b")]
        g))
    "")

  (dc/mkdn-pprint-source g-graphlib)

  "g2 graph is created through d3-dagre as:"
  (do
    (defn g-dagre []
      (graph/dagre-graph {:nodes [["a"] ["b"]]
                          :edges [["a" "b" "a->b"]]}))
    "")

  (dc/mkdn-pprint-source g-dagre))

(dc/deftest edge-node-query
  (testing "edges and nodes query"
    (let [g1 (g-graphlib)
          g2 (g-dagre)]
      (testing "node find"
        (is (= (.node g1 "a") {:label "a"}))
        (is (= (.node g2 "a") {:label "a" :shape "rect"})))
      (testing "node match"
        (is (= (.node g1 "a")
               (.node g2 "a"))))
      (testing "edge find"
        (is (= (.edge g1 "a" "b") "a->b")))
      (testing "edge match"
        (is (= (.edge g1 "a" "b")
               (.edge g2 "a" "b")))))))
