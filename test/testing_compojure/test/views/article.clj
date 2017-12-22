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

  (testing "it renders multiple articles w/ the render-all fn"
    (let [articles [{:title "a title" :content "a very nice blog post"}
                    {:title "another title" :content "here is another one!"}]
          html (render-all articles)]
      (is (string? html))
      (is (every? (fn [{title :title content :content}]
                    (and (.contains html title) (.contains html content)))
                  articles))))

  (testing "it renders a form for creating an article"
    (let [title "test title"
          content "test content"
          html (form title content)]
      (is (and (.contains html title)
               (.contains html content)))))

  )
