(ns sturdy-audit.views
  (:require
   [re-frame.core :as re-frame]
   [sturdy-audit.subs :as subs]
   [sturdy-audit.events :as events]
   ))

(defn audit-view
  [audit]
  ^{:key (str (:identnummer audit) "-" (:datum audit))} 
  [:div {:class "audit"}
   [:div (:identnummer audit)]
   [:div (:bezeichnung audit)]
   [:div (:datum audit)]
   [:hr]])

(defn main-panel []
  (let [audits (re-frame/subscribe [::subs/get-audits])]
    [:div
     [:input {:type "file" :id "file-input" :on-change #(re-frame/dispatch [::events/upload-files (-> % .-target .-files)])}]
     [:button {:on-click #(re-frame/dispatch [::events/import-to-couchdb])} "Upload"]
     (map audit-view @audits)
     ]))

