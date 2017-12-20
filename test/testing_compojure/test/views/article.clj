(ns testing-compojure.test.views.article
  (:use clojure.test
        testing-compojure.views.article))

(deftest test-article-views
  (testing "it renders the title and content of a single article"

    (let [article {:title "some title" :content "article content"}
          html (render article)]
      (is (.contains html (:title article)))
      (is (.contains html (:content article)))))

  (testing "it renders all the tags for an article"
    (let [tags ["one" "two" "three"]
          article {:title "" :content "" :tags tags}
          html (render article)]
      (is (every? #(.contains html %) tags))))

  )
