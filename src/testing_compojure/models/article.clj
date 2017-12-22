(ns testing-compojure.models.article
  (:require [clojure.java.jdbc :as jdbc]
            [testing-compojure.db.core :refer :all]
            [testing-compojure.models.user :as user]))

(defn find-all []
  (query [(select "*")
             (from "articles")]))

(defn create [article]
  (jdbc/insert! blog-db :articles article))

(defn seed []
  (let [articles [["title 1" "here is some text content"]
                  ["title 2" "and some more text content"]
                  ["title 3" "so much more content..."]]
        user (user/fst)
        user-id (:id user)]
    (doseq [[title body] articles]
      (create {:title title :body body :user_id user-id}))))

; (query
;   [(select "*")
;    (from "articles a")
;    (join "users u" "a.user_id" "u.id")])

; (query
;   [(select "*")
;    (from "users u")
;    (join "articles a" "a.user_id" "u.id")
;    (where "u.id = 1")])
