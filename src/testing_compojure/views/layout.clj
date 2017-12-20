(ns testing-compojure.views.layout
  (:require [hiccup.page :refer [html5 include-css]]))

(defn master [title & body]
  (html5
    [:head
     [:title title]
     (include-css "/css/screen.css")]
    [:body body]))
