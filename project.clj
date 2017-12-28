(defproject clj-blog "0.0.1"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.2"]
                 [hiccup "1.0.5"]
                 [ring-server "0.4.0"]
                 [org.clojure/data.json "0.2.6"]
                 [defun "0.3.0-RC1"]
                 [mysql/mysql-connector-java "6.0.5"]
                 [org.clojure/java.jdbc "0.7.3"]
                 [markdown-clj "1.0.1"]]
  :main clj-blog.core
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler clj-blog.server/app
         :init clj-blog.server/init
         :destroy clj-blog.server/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.5.1"]]}}
  :aliases {"migrate" ["run" "db:migrate"]
            "seed" ["run" "db:seed"]
            "refresh" ["run" "db:refresh"]})
