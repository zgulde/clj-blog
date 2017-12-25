(ns clj-blog.db.seeder
  (:require [clj-blog.models.user :as user]
            [clj-blog.models.article :as article]
            [clojure.java.jdbc :as jdbc]))

(def blog-db {:dbtype "mysql"
              :dbname "blog_db"
              :user "zach"
              :useSSL false})

(defn truncate-all []
  (jdbc/db-do-commands blog-db
    ["SET FOREIGN_KEY_CHECKS = 0"
     "TRUNCATE articles"
     "TRUNCATE users"
     "SET FOREIGN_KEY_CHECKS = 1"]))

(defn seed "run all the seeders" []
  (truncate-all)
  (user/seed)
  (article/seed))

(defn -main []
  (println "Running seeders...")
  (seed)
  (println "Finished running seeders!"))
