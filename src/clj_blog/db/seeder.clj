(ns clj-blog.db.seeder
  (:require [clj-blog.models.user :as user]
            [clj-blog.models.article :as article]
            [clj-blog.db.core :as db]
            [clojure.java.jdbc :as jdbc]))

(defn truncate-all []
  (jdbc/db-do-commands db/blog-db
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
