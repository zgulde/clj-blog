(ns testing-compojure.test.db
  (:use clojure.test
        testing-compojure.db.core))

(deftest query-builder-test

  (testing "(from)"
    (is (= {:from "foo"}
           (from :foo))))

  (testing "(select)"
    (is (= {:select ["*"] :from "foo"}
           (select (from :foo) "*")))
    (let [base (from :foo)
          q (select base :baz :pony)]
      (is (= #{"baz" "pony"}
             (set (:select q)))))
    (let [base (from :foo)
          q (-> base
                (select :bar)
                (select :pony.quux "foo.quux as q")
                (select :another))]
      (is (= #{"bar" "pony.quux" "foo.quux as q" "another"}
             (set (:select q))))
      (is (= "foo" (:from q)))))

  (testing "(join)"
    (let [base (select (from :foo) :bar)
          q (-> base
                (join :baz :baz.foo_id :foo.id)
                (join :pony :pony.baz_id :baz.id))]
      (is (= #{"baz" "baz.foo_id" "foo.id"}
             (set (first (:join q)))))
      (is (= #{"pony" "pony.baz_id" "baz.id"}
             (set (second (:join q)))))))

  (testing "(where)"
    (let [base (select (from :foo) :bar)
          q (-> base
                (where :bar :< 42)
                (where :and :baz := :quux)
                (where :or :pony :like "some thing"))]
      (is (= [42 "quux" "some thing"]
             (:params q)))
      (let [[fst snd thrd] (:where q)]
        (is (= fst (list "bar" "<" "?")))
        (is (= snd (list "and" "baz" "=" "?")))
        (is (= thrd (list "or" "pony" "like" "?"))))))

  (testing "(orderby)"
    (let [q (orderby {} :foo :bar)]
      (is (= (list "foo" "bar")
             (:order-by q))))
    (let [base (from :foo)
          q (-> base
                (orderby :bar)
                (orderby :baz))]
      (is (= (list "bar" "baz")
             (:order-by q)))))
  (testing "(groupby)"
    (let [q (groupby {} :foo :bar)]
      (is (= (list "foo" "bar")
             (:group-by q))))
    (let [base (from :foo)
          q (-> base
                (groupby :bar)
                (groupby :baz))]
      (is (= (list "bar" "baz")
             (:group-by q)))))

  (testing "(limit)"
    (let [q (limit {} 5)]
      (is (= 5 (:limit q)))))
  (testing "(offset)"
    (let [q (offset {} 5)]
      (is (= 5 (:offset q)))))

  (testing "(->query)"
    (let [parameter "a value"
          q (-> (from :foo)
                (select :bar :baz)
                (where :some_col := parameter)
                (limit 15)
                (offset 3))
          sql "SELECT bar, baz FROM foo WHERE some_col = ? LIMIT 15 OFFSET 3"]
      (is (= [sql parameter]
             (->query q))))
    (let [q (-> (from :atable)
                (select :col_one "col_two as two")
                (join "anothertable at" :at.atable_id :atable.id)
                (join :foo :at.foo_id :foo.id))
          sql (str "SELECT col_one, col_two as two "
                   "FROM atable "
                   "JOIN anothertable at ON at.atable_id = atable.id "
                   "JOIN foo ON at.foo_id = foo.id")
          query (->query q)]
      (is (= [sql] query)))
    (let [q (-> (from :table)
                (select :one)
                (groupby :foo :bar)
                (orderby :baz :pony))
          query (->query q)
          sql "SELECT one FROM table GROUP BY foo, bar ORDER BY baz, pony"]
      (is (= [sql] query))))

  )
