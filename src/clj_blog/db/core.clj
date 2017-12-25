(ns clj-blog.db.core
  (:require [clojure.java.jdbc :as jdbc]
            [defun.core :refer [defun]]
            [clojure.string :as s]))

(def blog-db {:dbtype "mysql"
              :dbname "blog_db"
              :user "zach"
              :useSSL false})

(defn query [sql-parts & params]
  (let [sql (s/join " " sql-parts)
        args (if params
               (apply conj [sql] params)
               [sql])]
    (jdbc/query blog-db args)))

(defn from [table] {:from (if (vector? table)
                            (s/join " " (map name table))
                            (name table))})

(defn select [q & args]
  (update q :select
          (fn [current-selects]
            (apply concat current-selects
                   [(->> args
                         (map (fn [part] (if (vector? part)
                                           (s/join " " (map name part))
                                           part)))
                         (map name))]))))

(defn join [q table rel1 rel2]
  (let [table (if (vector? table) (s/join " " (map name table)) table)]
    (update q :join
            (fn [current-joins]
              (concat current-joins [(map name [table rel1 rel2])])))))

(defn where [q & args]
  (let [param (last args)
        expr (map name (concat (butlast args) [:?]))]
    (-> q
        (update :params #(concat % [(if (keyword? param) (name param) param)]))
        (update :where #(concat % [expr])))))

(defn orderby [q & args] (update q :order-by #(concat % (map name args))))
(defn groupby [q & args] (update q :group-by #(concat % (map name args))))

(defn limit [q n] (assoc q :limit n))
(defn offset [q n] (assoc q :offset n))

(defn- build-query-parts [{:keys [select from join where limit offset group-by order-by]}]
  (filter seq
          ["SELECT" (s/join ", " select)
           "FROM" from
           (when join
             (s/join " " (map (fn [[table rel1 rel2]]
                                (str "JOIN " table " ON " rel1 " = " rel2))
                              join)))
           (when where
             (str "WHERE " (s/join " " (map #(s/join " " %) where))))
           (when group-by
             (str "GROUP BY " (s/join ", " group-by)))
           (when order-by
             (str "ORDER BY " (s/join ", " order-by)))
           (when limit (str "LIMIT " limit))
           (when offset (str "OFFSET " offset))]))

(defn ->query
  "Transfroms a map representing a query into a vector of a SQL string and
  parameters, suitable for passing to clojure.java.jdbc/query"
  [q]
  (apply conj [(s/join " " (build-query-parts q))] (:params q)))

(defn run "Transform the query map and return the results of jdbc/query"
  [q]
  (jdbc/query blog-db (->query q)))

(defn fst "Apply a limit of 1 and return the first result" [q]
  (-> q
      (limit 1)
      (run)
      (first)))
