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
    (let [user-id (:id (user/fst))
          article {:title "test title"
                   :content "test article content"
                   :user_id user-id}
          new-id (article/create article)
          new-article (article/by-id new-id)]
      (for [col [:title :content :user_id]]
        (is (= (col article) (col new-article))
            (str col " was inserted correctly")))))

  (migration/migrate))
