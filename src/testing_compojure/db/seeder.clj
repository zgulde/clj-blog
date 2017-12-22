(ns testing-compojure.db.seeder
  (:require [testing-compojure.models.user :as user]))

(defn seed []
  (user/seed)
  (article/seed))
