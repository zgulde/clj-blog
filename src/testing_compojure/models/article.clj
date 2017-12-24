(ns testing-compojure.models.article
  (:require [clojure.java.jdbc :as jdbc]
            [testing-compojure.db.core :as db]
            [testing-compojure.models.user :as user]))

(def with-users
  (-> (db/from [:articles :a])
      (db/join [:users :u] :a.user_id :u.id)
      (db/select :a.title :a.body :u.email)))

(defn find-all []
  (db/run with-users))

(defn fst []
  (db/fst with-users))

(defn create [article]
  (jdbc/insert! db/blog-db :articles article))

(defn seed []
  (let [articles [["title 1" "here is some text content"]
                  ["title 2" "and some more text content"]
                  ["title 3" "so much more content..."]]
        user (user/fst)
        user-id (:id user)]
    (doseq [[title body] articles]
      (create {:title title :body body :user_id user-id}))))
