(ns clj-blog.test.models
  (:require [clj-blog.models.article :as article]
            [clj-blog.models.user :as user]
            [clj-blog.db.migration :as migration]
            [clj-blog.db.seeder :as seeder])
  (:use clojure.test))

(deftest article-model-test
  (migration/migrate)
  (seeder/seed)
  (testing "insert a record and retrieve it"
    ; (let [article {:title "test title" :content "test article content"}
    ;       new-id (article/create article)])
    )
  (migration/migrate))
