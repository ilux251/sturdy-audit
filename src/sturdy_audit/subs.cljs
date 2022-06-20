(ns sturdy-audit.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::input
 (fn [db]
   (:input db)))

(re-frame/reg-sub
 ::docs
 (fn [db]
   (:docs db)))