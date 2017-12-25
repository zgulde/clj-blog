(ns clj-blog.db.migration
  (:require [clojure.java.jdbc :as jdbc]))

(def blog-db {:dbtype "mysql"
              :dbname "blog_db"
              :user "zach"
              :useSSL false})

(def create-users-table
  (jdbc/create-table-ddl :users
   [[:id :integer :unsigned :not :null :auto_increment]
    [:email "varchar(255)"]
    [:primary :key "(id)"]]))

(def create-articles-table
  (jdbc/create-table-ddl :articles
    [[:id :integer :unsigned :not :null :auto_increment]
     [:title "varchar(255)" :not :null]
     [:content :text :not :null]
     [:user_id :integer :unsigned :not :null]
     [:primary :key "(id)"]
     [:foreign :key "(user_id)" :references "users (id)"]]))

(defn migrate "run the migrations" []
  (jdbc/db-do-commands blog-db
   ["DROP TABLE IF EXISTS articles"
    "DROP TABLE IF EXISTS users"
    create-users-table
    create-articles-table]))

(defn -main []
  (println "Running migrations...")
  (migrate)
  (println "Finished running migrations!"))
