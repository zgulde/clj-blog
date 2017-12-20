(ns testing-compojure.test.db
  (:use clojure.test
        testing-compojure.db.core))

(deftest query-builder-test

  (testing "SELECT"
    (is (= "SELECT *"
           (select "*")))
    (is (= "SELECT one o, two as the_number_two"
           (select "one o" "two as the_number_two"))))

  (testing "FROM"
    (is (= "FROM tablename"
           (from "tablename")))
    (is (= "FROM tableone, tabletwo"
           (from "tableone" "tabletwo"))))

  (testing "JOIN"
    (is (= "JOIN jtable j ON j.some_key = t.key"
           (join "jtable j" "j.some_key" "t.key"))))

  )
