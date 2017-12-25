(ns clj-blog.core
  (:require [clj-blog.db.migration :refer [migrate]]
            [clj-blog.db.seeder :refer [seed]])
  (:gen-class))

(def logo
  (clojure.string/join
    "\n" ["      _  _       _     _             "
          "  ___| |(_)     | |__ | | ___   __ _ "
          " / __| || | ___ | '_ \\| |/ _ \\ / _` |"
          "| (__| || | ___ | |_) | | (_) | (_| |"
          " \\___|_|/ |     |_.__/|_|\\___/ \\__, |"
          "      |__/                     |___/ "]))

(defn -main [& argv]
  (println logo)
  (let [command (first argv)]
      (case command
          "db:migrate" (do (println "Running migration...")
                        (migrate)
                        (println "Successfully migrated!"))
          "db:seed" (do (println "Running seeder...")
                     (seed)
                     (println "Successfully seeded!"))
          "db:refresh" (do (println "Refreshing db...")
                           (migrate)
                           (seed)
                           (println "Refreshed!"))
          (do (println "Try one of:")
              (println "  - db:migrate")
              (println "  - db:seed")
              (println "  - db:refresh")))))
