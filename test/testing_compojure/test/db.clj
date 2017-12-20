(ns testing-compojure.test.db
  (:use clojure.test
        testing-compojure.db.core))

(deftest query-builder-test

  (testing "select"
    (is (= "SELECT *"
           (select "*")))
    (is (= "SELECT one o, two as the_number_two"
           (select "one o" "two as the_number_two"))))

  (testing "from"
    (is (= "FROM tablename"
           (from "tablename")))
    (is (= "FROM tableone, tabletwo"
           (from "tableone" "tabletwo"))))

  (testing "join"
    (is (= "JOIN jtable j ON j.some_key = t.key"
           (join "jtable j" "j.some_key" "t.key"))))

  (testing "groupby"
    (is (= "GROUP BY one"
           (groupby "one")))
    (is (= "GROUP BY one, two"
           (groupby "one" "two"))))

  (testing "orderby"
    (is (= "ORDER BY one"
           (orderby "one")))
    (is (= "ORDER BY one, two"
           (orderby "one" "two"))))

  (testing "where"
    (is (= "WHERE 1 = 1"
           (where "1 = 1")))
    (is (= "WHERE 1 = 1 and foo > bar"
           (where "1 = 1" "and foo > bar")))
    (is (= "WHERE 1 = 1 or foo > bar"
           (where "1 = 1" "or foo > bar"))))

  (is (= "LIMIT 5" (limit 5)))
  (is (= "OFFSET 5" (offset 5)))

  )
