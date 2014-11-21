(ns om-infinite-scroll.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))
            ; [sablono.core :as html :refer-macros [html]]

(enable-console-print!)
(println "hello")


(defn panel-item [{:keys [n]} owner]
  (reify
    om/IRender
    (render [_] 
      (let [name (str n "Banh mi selfies shabby chic disrupt polaroid roof party. Portland High Life brunch sustainable, plaid Kickstarter pickled four loko 3 wolf moon whatever.")]
        (dom/li #js {:className "is-item"}
                (dom/div #js {} (:id data))
                (dom/div #js {} name))))))

; DOM read + set-state
; (defn handleScroll [owner e]
;   (let [node (om/get-node owner)]
;         (om/set-state! owner :scrollTop (.-scrollTop node))))

; DOM read + set-state
(defn handleScroll [owner e]
  (let [node (om/get-node owner)
        scrollTop (.-scrollTop node)]
    (when-not (zero? scrollTop) 
      (set! (.-scrollTop node) 0)
      (om/update-state! owner :start-entry (partial + 10)))))

; DOM read
; (defn handleScroll [owner e]
;   (let [node (om/get-node owner)]
;         (println (.-scrollTop node))))

; Set-state only
; (defn handleScroll [owner e]
;   (om/set-state! owner :scrollTop 1))

; Nothing
; (defn handleScroll [owner e])

(def n-show 40)

(defn panel [data owner]
  (reify
    om/IDidMount
    (did-mount [_]
      (.addEventListener (om/get-node owner)
                         "mousewheel" 
                         (fn [e] 
                           (println "Mouse wheel " (.-wheelDeltaY e))
                           (om/update-state! owner 
                                             :start-entry 
                                             #(max 0 (+ (- (.-wheelDeltaY e)) 
                                                        %))))
                         false))
    om/IRenderState
    (render-state [this {:keys [start-entry]}]
      (dom/div #js {:className "is-panel"}
               (apply dom/ol #js {:className "is-content"}
                      (take n-show
                            (drop start-entry 
                                  (map (fn [n] 
                                         (om/build panel-item 
                                                   {:n n} 
                                                   {:react-key n})) 
                                       (range 1 1000)))))))))

(om/root panel {:text "Hello world!"} {:target js/document.body})

    ; om/IShouldUpdate
    ; (should-update [this _ nextState]
    ;   false
    ;   )
      ; (let [prevState (om/get-render-state owner)]
        ; (not= (:scrollTop nextState) (:scrollTop prevState))
