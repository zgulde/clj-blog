(ns clj-blog.test.query-builder
  (:use clojure.test
        clj-blog.db.core))

(deftest query-builder-test

  (testing "(from)"
    (is (= {:from "foo"}
           (from :foo)))
    (is (= {:from "table t"}
           (from [:table :t]))))

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

  (testing "(search)"
    (let [q (-> (from :foo)
                (search :col "asdf"))]
      (is (= '("col") (:select q)))
      (is (= '(("col" "like" "?"))) (:where q))
      (is (= '("%asdf%") (:params q))))
    (let [q (-> (from :table)
                (search [:col :c] "qwer"))]
      (is (= '("col c") (:select q)))
      (is (= '(("col" "like" "?"))) (:where q))
      (is (= '("%qwer%") (:params q)))))

  (testing "(->query)"
    ;;
    (let [parameter "a value"
          q (-> (from :foo)
                (select :bar :baz)
                (where :some_col := parameter)
                (limit 15)
                (offset 3))
          sql "SELECT bar, baz FROM foo WHERE some_col = ? LIMIT 15 OFFSET 3"]
      (is (= [sql parameter]
             (->query q))))
    ;;
    (let [q (-> (from [:atable :a])
                (select :col_one [:col_two :two])
                (join [:anothertable :at] :at.atable_id :a.id)
                (join :foo :at.foo_id :foo.id))
          sql (str "SELECT col_one, col_two two "
                   "FROM atable a "
                   "JOIN anothertable at ON at.atable_id = a.id "
                   "JOIN foo ON at.foo_id = foo.id")
          query (->query q)]
      (is (= [sql] query)))
    ;;
    (let [q (-> (from :table)
                (select [:foo :f] :bar :baz :pony [:quux :q])
                (groupby :foo :bar :baz)
                (orderby :baz :pony :quux))
          query (->query q)
          sql "SELECT foo f, bar, baz, pony, quux q FROM table GROUP BY foo, bar, baz ORDER BY baz, pony, quux"]
      (is (= [sql] query))))
;;
)
