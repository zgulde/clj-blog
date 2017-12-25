(ns clj-blog.test.models
  (:requrie [clj-blog.models.articles :as article]
            [clj-blog.models.user :as user])
  (:use clojure.test
        clj-blog.db.migration
        clj-blog.db.seeder))

(deftest article-model-test
  (migrate)
  (testing "insert a record and retrieve it"
    (let [article {:title "test title" :content "test article content"}
          new-id (create-article)]))
  (migrate))
