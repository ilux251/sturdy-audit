(ns sturdy-audit.events
  (:require
   [re-frame.core :as re-frame]
   [sturdy-audit.db :as db]
   [com.stronganchortech.pouchdb-fx :as pouchdb-fx]
   [jtk-dvlp.re-frame.readfile-fx]
   [hickory.core :as h]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-fx
 :set-file-content
 (fn [{:keys [db]} [_ data]]
   {:db (assoc db :uploaded-file-content (-> data
                                             first
                                             :content))}))

(defn get-inner-text
  [element]
  (-> element
      first
      h/as-hickory
      :content
      first))

(defn map-audit
  [audit]
  (let [identnummer (get-inner-text (.getElementsByTagName audit "Identnummer"))
        bezeichnung (get-inner-text (.getElementsByTagName audit "Bezeichnung"))
        datum (get-inner-text (.getElementsByTagName audit "Datum"))]
    {:identnummer identnummer
     :bezeichnung bezeichnung
     :datum datum}))

(re-frame/reg-cofx
 :parse-and-get-audit
 (fn [coeffects _]
   (let [xml-data (-> coeffects :db :uploaded-file-content)
         parsed-xml-data (-> (js/DOMParser.)
                             (.parseFromString xml-data "text/xml"))
         audits (.getElementsByTagName parsed-xml-data "Prüfung")
         mapped-audits (map map-audit audits)]
     (assoc coeffects :parsed-audit-data mapped-audits))))

(re-frame/reg-event-fx
 ::import-to-couchdb
 [(re-frame/inject-cofx :parse-and-get-audit)]
 (fn [{:keys [_ parsed-audit-data]} _]
   {:pouchdb {:db "audit"
              :method :post
              :doc {:audits parsed-audit-data}
              :success ::load-from-pouch}}))

(re-frame/reg-event-db
 :error
 (fn [_ [_ error]]
   (println error)))

(re-frame/reg-event-fx
 ::upload-files
 (fn [_ [_ files]]
   {:readfile 
    {:files files
     :on-success [:set-file-content]
     :on-error [:error]}}))

(re-frame/reg-event-fx
 ::remove-item
 (fn [_ [_ doc]]
   {:pouchdb
    {:db "audit"
     :method :remove
     :doc doc
     :success ::load-from-pouch}}))

(re-frame/reg-event-fx
 ::load-from-pouch
 (fn [_ _]
   {:pouchdb
    {:db "audit"
     :method :all-docs
     :options {:include_docs true
               :descending true
               :limit 1}
     :success ::load-from-pouch-success}}))

(re-frame/reg-event-db
 ::load-from-pouch-success
 (fn [db [_ data]]
   (println (count (:rows data)))
   (let [docs (-> data
                  :rows
                  first
                  :doc
                  :audits)]
     (assoc db :audits docs))))