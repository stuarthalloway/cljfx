(ns x2-sensitive-to-map-key-order
  (:require [cljfx.api :as fx]))

(fx/on-fx-thread
  (fx/create-component
   {:fx/type :text-input-dialog
    :header-text "Set application modal before show"
    :modality :application-modal
    :showing true}))
;; displays dialog

(fx/on-fx-thread
  (fx/create-component
   {:fx/type :text-input-dialog
    :header-text "Set application modal after show"
    :showing true
    :modality :application-modal}))
;; displays dialog *and*
;; UI thread throws java.lang.IllegalStateException: Cannot set modality once stage has been set visible
