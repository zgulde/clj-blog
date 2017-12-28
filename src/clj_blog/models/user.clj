(ns clj-blog.models.user
  (:require [clojure.java.jdbc :as jdbc]
            [clj-blog.db.core :as db]))

(defn find-all []
  (-> (db/from :users)
      (db/select :*)
      (db/run)))

(defn create [user]
  (:generated_key (first (jdbc/insert! db/blog-db :users user))))

(defn fst []
  (-> (db/from :users)
      (db/select :*)
      (db/fst)))

(defn seed []
  (for [email ["zach@codeup.com" "ryan@codeup.com"
               "luis@codeup.com" "fernando@codeup.com"
               "justin@codeup.com"]]
    (create {:email email})))
