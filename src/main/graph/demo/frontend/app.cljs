(ns graph.demo.frontend.app
  (:require [reagent.dom :as dom]
            [reagent.core :as r]))

(defn counter 
  []
  (let [state  (r/atom {:counter 0})
        change (fn [event f]
                 (.preventDefault event)
                 (swap! state update-in [:counter] f))]
    (fn []
      [:h2 "Counter"
       [:div
        [:button {:on-click #(change % dec)} "-"]
        [:span (:counter @state)]
        [:button {:on-click #(change % inc)} "+"]]])))

(defn start!
  []
  (dom/render [counter]
              (.getElementById js/document "root")))

(defn init
  []
  (start!))
