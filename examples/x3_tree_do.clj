(ns x3-tree-do
  (:require [cljfx.api :as fx])
  (:import [javafx.scene.control TreeItem]))

(defn- ->tree-item [x]
  (cond
    (string? x) {:fx/type :tree-item :value x}
    (seqable? x) {:fx/type :tree-item :value x :children (map ->tree-item x)}
    :else {:fx/type :tree-item :value x}))

(defn tree-view
  [data]
  {:fx/type :tree-view
   :cell-factory {:fx/cell-type :tree-cell
                  :describe (fn [x]
                              {:text (str x)})}
   :root (->tree-item data)})

(defn path-to-root
  [item]
  (sequence
   (comp (take-while identity)
         (map #(.getValue ^TreeItem %)))
   (iterate #(.getParent ^TreeItem %) item)))

(defn tree-do
  [data f]
  (let [sel (atom nil)]
    (fx/on-fx-thread
     (fx/create-component
      {:fx/type :stage
       :showing true
       :title "tree-do"
       :scene {:fx/type :scene
               :root {:fx/type :v-box
                      :children [(assoc (tree-view data)
                                   :on-selected-item-changed
                                   #(reset! sel (path-to-root %)))
                                 {:fx/type :button
                                  :on-action (fn [_] (f @sel))
                                  :text "Do!"}]}}}))))

(tree-do
 [1 [2 3 [4 5 6]]]
 prn)







