(ns sturdy-audit.subs
  (:require
   [re-frame.core :as re-frame]))

(re-frame/reg-sub
 ::get-audits
 (fn [db]
   (:audits db)))