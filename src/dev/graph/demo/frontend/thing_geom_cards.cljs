(ns graph.demo.frontend.thing-geom-cards
  (:require
   [reagent.core] ; needed for dc/defcard-rg
   [devcards.core :as dc]

     ;; geom imports
   [thi.ng.math.core :as m]
   [thi.ng.geom.core :as g]
   [thi.ng.geom.circle :as c]
   [thi.ng.geom.vector :as v]
   [thi.ng.geom.line :as l]
   [thi.ng.geom.svg.core :as svg]
   [thi.ng.geom.svg.adapter :as adapt]
   [thi.ng.color.core :as col]))


(defn labeled-dot
  [p label] (list (c/circle p 3) (svg/text (m/+ p 10 0) label)))

;; This scene defines 2 circles and their intersection points
(def scene
  (let [c1    (c/circle 50 150 50)
        c2    (c/circle 250 150 50)
        c3    (c/circle 150 150 100)
        [a b] (g/intersect-shape c1 c3)
        [c d] (g/intersect-shape c2 c3)]
    [:svg
     {:width 300 :height 300}
     (svg/group
      {:fill "yellow"}
      ;; these circles inherit all attributes from parent group
      c1 c2
      ;; we can use metadata to override specific attribs per shape
      ;; here we also demonstrate automatic color attrib conversion
      (with-meta c3 {:fill (col/rgba 0 1 1 0.25) :stroke (col/hsva 0 1 1)}))
     (svg/group
      {:fill "#000"
       :font-family "Arial, sans-serif"
       :font-size 10}
      (mapcat labeled-dot [a b c d] ["A" "B" "C" "D"]))]))

(dc/defcard-doc
  "# thi.ng/geom sandbox")

(def pi 3.14159)

(defn hline
  [y x d]
  (l/line2 [x y] [d y]))

(defn vline
  [x y d]
  (l/line2 [x y] [x d]))

(defn start-p 
  [line]
  (first (:points line)))

(defn linesf
  [x0 dx d labelx labely lf col]
  (reduce (fn [acc [label n]]
            (let [_step (fn [x0 dx]
                         (fn [n] (+ x0 (* n dx))))
                  step (_step x0 dx)
                  line (lf (step n) 15 d)
                  label (svg/text (m/+ (start-p line) labelx labely) label)]
              (merge-with concat acc {:labels [label] :lines [line]})))
          {:labels '() :lines '()}
          (map (fn [a b] [a b]) col (range (count col)))))


(defn between
  ([a b]
   (let [a (start-p a)
         b (start-p b)]
     (m/+ a (m/div (m/- b a) 2))))
  ([[a b]] (between a b)))

(defn grid
  [cols rows celldef]
  (let [dx (:dx celldef)
        dy (:dy celldef)
        y0 25
        x0 25
        
        lines (linesf x0 dy (* dx (inc (count cols))) -12 5 hline rows)
        columns (linesf y0 dx (* dy (inc (count rows))) (/ dx 4) -2 vline cols)
        
        dot-lines (map (fn [x] (hline (:y (between x)) 15 120))
                       (partition 2 1 (:lines lines)))
        dot-columns (map (fn [x] (vline (:x (between x)) 15 120))
                         (partition 2 1 (:lines columns)))
        
        intersec (g/intersect-line (first (:lines lines)) (first dot-columns))]

  ;; celldef dx dy == cell size
    (svg/group {}
     (svg/group
      {:stroke "black"}
      ;;hlines
      (vals lines)

      ;;vlines
      (vals columns)
      (print intersec)
      (with-meta (c/circle (:p intersec) (/ dy 2.5)) {:fill "white"})
      (svg/text (m/+ (:p intersec) -6 5) "E1")
      )
               

     ;; dotted grid
     ;; usable to debug alignment
     (if (get celldef :grid false)
       (svg/group
        {:stroke "blue" :stroke-dasharray "1 1 1"}

      ;;between hlines
        dot-lines
        dot-columns

      ;;between vlines
        )))))

(dc/defcard
 "line grid fn"
 (dc/reagent
  (adapt/all-as-svg
    [:svg {:width 400 :height 300}
     (grid (map str (range 13))
           (map str (range 1 7))
           {:dx 25 :dy 20 :grid false})])))


(dc/defcard
 "WIP"
 (dc/reagent
  (adapt/all-as-svg
   [:svg
    {:width 300 :height 300}
    (svg/group
     {:stroke "black"}
     (svg/line [0 0] [300 300])
     (svg/line [0 300] [300 0])
     (svg/circle [150 150] 100 {:fill (col/rgba 0 1 1 0.25)})
     (svg/arc [150 150] 50 (* pi (/ 6 8)) (/ pi 4)  false false {:fill (col/rgba 0 0 0 0.0)}))])))

(dc/defcard
  "Circles svg example"
 (dc/reagent (adapt/all-as-svg scene)))
