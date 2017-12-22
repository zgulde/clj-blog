(ns testing-compojure.views.article
  (:require [hiccup.core :refer [html]]))

(defn render [{title :title content :content tags :tags}]
  (html
    [:div {:class "article col-md-6"}
     [:h2 {:class "text-center"} title]
     (when (seq tags)
       [:ul (for [tag tags]
              [:li tag])])
     [:p content]]))

(defn render-all [] )

(defn form []
  (html
    [:form {:method "POST" :action "/posts"}
     [:div {:class "form-group"}]]))
