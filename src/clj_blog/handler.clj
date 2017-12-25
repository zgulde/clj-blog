(ns clj-blog.handler
  (:require [compojure.core :refer [defroutes routes GET]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [hiccup.page :refer [html5]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [clojure.java.io :as io]
            [markdown.core :as md]
            [clj-blog.routes.home :refer [home-routes]]
            [clj-blog.views.layout :as layout]
            [clj-blog.views.article :as article-views]
            [clj-blog.models.article :as article-model]))

(defn init []
  (println "clj-blog is starting"))

(defn destroy []
  (println "clj-blog is shutting down"))

(defn show-404 []
  (layout/master
    [:h1.text-center "404 Page Not Found"]
    [:p "Try going elsewhere!"]))

(defn show-posts []
  (layout/master "Check out all the Posts"
    [:h1.text-center "Posts index page"]
    (article-views/render-all (article-model/find-all))))

(defn show-post [id]
  (layout/master (str "Viewing Post #" id)
    [:h1 (str "post #" id)]))

(defn show-markdown [path]
  (let [filepath (str "resources/md/" path ".md")]
    (when (.exists (io/file filepath))
     (layout/master "title" (md/md-to-html-string (slurp filepath))))))

(defroutes app-routes
  (route/resources "/")
  (route/not-found (show-404)))

(defroutes post-routes
  (GET "/posts" [] (show-posts))
  (GET "/posts/:id" [id] (show-post id)))

(defroutes markdown-routes
  (GET "/:file" [file] (show-markdown file)))

(def app
  (-> (routes home-routes
              post-routes
              markdown-routes
              app-routes)
      (handler/site)
      (wrap-base-url)))
