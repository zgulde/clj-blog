(ns testing-compojure.db.core
  (:require [clojure.java.jdbc :as j]))

(def blog-db {:dbtype "mysql"
              :dbname "blog_db"
              :user "zach"
              :useSSL false})

(defn query [sql-parts & params]
  (let [sql (clojure.string/join " " sql-parts)
        args (if params
               (apply conj [sql] params)
               [sql])]
    ; (println args)))
    (j/query blog-db args)))

(defn select [& args] (str "SELECT " (clojure.string/join ", " args)))
(defn from [& args] (str "FROM " (clojure.string/join ", " args)))
(defn join [table rel1 rel2] (str "JOIN " table " ON " rel1 " = " rel2))
(defn groupby [& args] (str "GROUP BY " (clojure.string/join ", " args)))
(defn orderby [& args] (str "ORDER BY " (clojure.string/join ", " args)))
(defn where [& args] (str "WHERE " (clojure.string/join " " args)))
(defn limit [n] (str "LIMIT " n))
(defn offset [n] (str "OFFSET " n))
