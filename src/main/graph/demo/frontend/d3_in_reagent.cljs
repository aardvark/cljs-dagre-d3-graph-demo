(ns graph.demo.frontend.d3-in-reagent
  (:require [reagent.core :as reagent]
            ["d3" :as d3]))

(defn container-enter 
  [atom]
  (-> (.select d3 (str "#" (:id @atom) " svg"))
      (.append "g")
      (.attr "class" "container")))

(defn container-did-mount [ratom]
  (container-enter ratom))

(defn svg-did-mount [ratom]
  (container-did-mount ratom))

(defn svg-render [ratom]
  [:svg {:width (:width @ratom)
         :height (:height @ratom)}])


(defn lone-svg [ratom]
  (reagent/create-class
   {:reagent-render #(svg-render ratom)
    :component-did-mount #(svg-did-mount ratom)
    :component-did-update #(svg-render ratom)}))

(defn wrap-in-reagent 
  [state-atom {:keys [render mount update]}]
  (reagent/create-class
   {:reagent-render #(render state-atom)
    :component-did-mount #(mount state-atom)
    :component-did-update #(update state-atom)}))