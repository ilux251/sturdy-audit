(ns sturdy-audit.events
  (:require
   [re-frame.core :as re-frame]
   [sturdy-audit.db :as db]
   [com.stronganchortech.pouchdb-fx :as pouchdb-fx]))

(re-frame/reg-event-db
 ::initialize-db
 (fn [_ _]
   db/default-db))

(re-frame/reg-event-db
 ::set-input
 (fn [db [input _]]
   (assoc db :input input)))

(re-frame/reg-event-fx
 ::remove-item
 (fn [cofx [_ doc]]
   {:pouchdb
    {:db "todos"
     :method :remove
     :doc doc
     :success ::load-from-pouch}}))

(re-frame/reg-event-fx
 ::load-from-pouch
 (fn [cofx event]
   {:pouchdb
    {:db "todos"
     :method :all-docs
     :options {:include_docs :true}
     :success ::load-from-pouch-success}}))

(re-frame/reg-event-db
 ::load-from-pouch-success
 (fn [db [event args]]
   (let [docs (:rows args)]
    (assoc db :docs docs))))
