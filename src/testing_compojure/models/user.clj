(ns testing-compojure.models.user
  (:require [clojure.java.jdbc :as jdbc]
            [testing-compojure.db.core :as db]))

(defn find-all []
  (db/query [(db/select "*")
             (db/from "users")]))

(defn create [user]
  (jdbc/insert! db/blog-db :users user))

(defn fst []
  (first (db/query [(db/select "*")
              (db/from "users")
              (db/limit "1")])))

(defn seed []
  (doseq [email ["zach@codeup.com" "ryan@codeup.com"
                 "luis@codeup.com" "fernando@codeup.com"
                 "justin@codeup.com"]]
    (create {:email email})))
