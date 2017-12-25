(ns clj-blog.views.layout
  (:require [hiccup.page :refer [html5 include-css include-js]]))

(def bootstrap-cdn
  {:css "//netdna.bootstrapcdn.com/bootstrap/3.1.1/css/bootstrap.min.css"
   :js "//netdna.bootstrapcdn.com/bootstrap/3.1.1/js/bootstrap.min.js"})
(def jquery-cdn "//code.jquery.com/jquery-2.2.4.min.js")

(defn master [title & body]
  (html5
    [:head
     [:meta {:charset "utf-8"}]
     [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
     [:title title]
     (include-css "/css/screen.css")
     (include-css (:css bootstrap-cdn))]
    [:body body
     (include-js (:js bootstrap-cdn))
     (include-js jquery-cdn)]))
