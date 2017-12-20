(defproject testing-compojure "0.1.0-SNAPSHOT"
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
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler testing-compojure.handler/app
         :init testing-compojure.handler/init
         :destroy testing-compojure.handler/destroy}
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.5.1"]]}})
