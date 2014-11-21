(ns om-infinite-scroll.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))
            ; [sablono.core :as html :refer-macros [html]]

(enable-console-print!)
(println "hello")

(def item-height 50)
(def n-show 40)

(defn panel-item [{:keys [n id]} owner]
  (reify
    om/IRender
    (render [_] 
      (let [name (str n "Banh mi selfies shabby chic disrupt polaroid roof party. Portland High Life brunch sustainable, plaid Kickstarter pickled four loko 3 wolf moon whatever.")]
        (dom/li #js {:style #js {:height item-height}
                     :className "is-item"}
                (dom/div #js {} id)
                (dom/div #js {} name))))))

(defn panel [data owner]
  (reify

    om/IInitState
    (init-state [_]
      {:current-entry 0
       :current-position 0})
    om/IDidMount
    (did-mount [_]
      (.addEventListener (om/get-node owner)
                         "mousewheel" 
                         (fn [e] 
                           (let [position (max 0 
                                               (- (om/get-state owner :current-position)
                                                  (.-wheelDeltaY e)))
                                 _ (println "Pos " position " " (.-wheelDeltaY e))
                                 current-entry (/ position item-height)]
                             (om/set-state! owner :current-position position)
                             (om/set-state! owner :current-entry current-entry)))
                         false))
    om/IRenderState
    (render-state [this {:keys [current-entry]}]
      (dom/div #js {:className "is-panel"}
               (apply dom/ol #js {:className "is-content"}
                      (take n-show
                            (drop current-entry 
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
