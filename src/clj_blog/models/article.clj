(ns clj-blog.models.article
  (:require [clojure.java.jdbc :as jdbc]
            [clj-blog.db.core :as db]
            [clj-blog.models.user :as user]))

(def with-users
  (-> (db/from [:articles :a])
      (db/join [:users :u] :a.user_id :u.id)
      (db/select :a.title :a.content :u.email [:u.id :user_id])))

(defn find-all []
  (db/run with-users))

(defn fst []
  (db/fst with-users))

(defn by-id [id]
  (-> with-users
      (db/where :a.id := id)
      (db/fst)))

(defn create [article]
  (:generated_key (first (jdbc/insert! db/blog-db :articles article))))

(defn seed []
  (let [articles [["title 1" "here is some text content"]
                  ["title 2" "and some more text content"]
                  ["title 3" "so much more content..."]]
        user (user/fst)
        user-id (:id user)]
    (for [[title content] articles]
      (create {:title title :content content :user_id user-id}))))
