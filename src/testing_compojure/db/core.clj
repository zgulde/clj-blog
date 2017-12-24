(ns testing-compojure.db.core
  (:require [clojure.java.jdbc :as j]
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
    (j/query blog-db args)))

(defn from [table] {:from (name table)})

(defn select [q & args] (update q :select #(apply concat % [(map name args)])))

(defn join [q table rel1 rel2]
  (update q :join #(concat % [(map name [table rel1 rel2])])))

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
