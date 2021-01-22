(ns x1-autocomplete
  (:require [cljfx.api :as fx])
  (:import [org.controlsfx.control.textfield TextFields]
          [javafx.util Callback StringConverter]
          [javafx.util.converter DefaultStringConverter]))

;; this example shows using fx/ext-on-instance-lifecycle to add
;; controls-fx autocomplete to 

;; OO silliness to register callback
(defn add-autocomplete-suggester
  [text-field f]
  (TextFields/bindAutoCompletion
   text-field
   (reify Callback
          (call
           [_ p]
           (f (.getUserText p))))
   (DefaultStringConverter.)))

(def *state
  (atom {:text ""}))

(defn text-input [{:keys [text]}]
  {:fx/type :text-field
   :on-text-changed #(swap! *state assoc :text %)
   :text text})

(defn root
  [{:keys [text]}]
  {:fx/type :stage
   :showing true
   :title "controlsfx autocomplete example"
   :scene {:fx/type :scene
           :root {:fx/type :v-box
                  ;; see https://github.com/cljfx/cljfx#extending-cljfx
                  :children [{:fx/type fx/ext-on-instance-lifecycle
                              :on-created (fn [node]
                                            (add-autocomplete-suggester
                                             node
                                             #(map (fn [n] (str % "-" n)) (range 50))))
                              :desc {:fx/type text-input
                                     :text text}}]}}})

(def renderer
  (fx/create-renderer
   :middleware (fx/wrap-map-desc assoc :fx/type root)))

(fx/mount-renderer *state renderer)


