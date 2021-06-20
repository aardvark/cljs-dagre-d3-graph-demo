(ns graph.demo.frontend.d3-in-reagent-cards
  (:require
   [graph.demo.frontend.d3-in-reagent :as subject]
   [reagent.core] ; needed for dc/defcard-rg
   [devcards.core :as dc]))

(dc/defcard-doc
  "# Markdown
   d3-in-reagent examples")

(declare lone-svg)

(defonce lone-svg-atom
  (let [a (atom {:height 20 :width 20 :id "test-mount"})]
    a))

(dc/defcard-rg lone-svg
  "Single svg without any internals"
  (fn [data-atom _] [:div#test-mount [subject/lone-svg data-atom]])
  lone-svg-atom
  {:inspect-data true :history true})
