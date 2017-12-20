(ns testing-compojure.routes.home
  (:require [compojure.core :refer :all]
            [testing-compojure.views.layout :as layout]))

(defn home []
  (layout/master "Home Page"
    [:h1.text-center "Hello World!"]
    [:p "This is the home page!"]))

(defroutes home-routes
  (GET "/" [] (home)))

