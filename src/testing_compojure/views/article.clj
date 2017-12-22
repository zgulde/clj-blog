(ns testing-compojure.views.article
  (:require [hiccup.core :refer [html]]
            [clojure.string :refer [join]]))

(defn render [{title :title content :content tags :tags}]
  (html
    [:div {:class "article col-md-6"}
     [:h2 {:class "text-center"} title]
     (when (seq tags)
       [:ul (for [tag tags]
              [:li tag])])
     [:p content]]))

(defn render-all [articles] (join (map render articles)))

(defn form
  "Renders the form to create a post, optionally including prefilled values for
  the title and content"
  ([] (form "" ""))
  ([title content]
   (html
     [:form {:method "POST" :action "/posts"}
      [:div.form-group
       [:label {:for "title"} "Title"]
       [:input#name.form-control {:name "title" :value title}]]
      [:div.form-group
       [:label {:for "content"} "Content"]
       [:textarea#content.form-control content]]])))
