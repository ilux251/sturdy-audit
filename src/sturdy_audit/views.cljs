(ns sturdy-audit.views
  (:require
   [re-frame.core :as re-frame]
   [sturdy-audit.subs :as subs]
   [sturdy-audit.events :as events]
   ))

(defn create-doc 
  [{:keys [doc]}]
  [:div
   [:div (:title doc)]
   [:div (:text doc)]
   [:button {:on-click #(re-frame/dispatch [::events/remove-item doc])} "X"]])

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])
        docs (re-frame/subscribe [::subs/docs])]
    [:div
     [:h1
      "Hello from " @name]
     [:input {:type :text :value "Text" :on-change #(re-frame/dispatch [::events/set-input (-> % .-target .-value)])}]
     [:button {:on-click (fn [e]
                           (re-frame/dispatch
                            [:pouchdb
                             {:db "todos"
                              :method :post
                              :doc {:title "Title" :text "Text"}
                              :success ::events/load-from-pouch}]))} "Create Todo"]
     (map create-doc @docs)
     ]))