(ns testing-compojure.scratch
  (:require [defun.core :refer [defun]]
            [markdown.core :as m]
            [clojure.java.io :as io]))

(defun fizzbuzz
  "does fizzbuzz for the given number"
  ([n :guard #(zero? (mod % 15))] "fizzbuzz")
  ([n :guard #(zero? (mod % 3))] "fizz")
  ([n :guard #(zero? (mod % 5))] "buzz")
  ([n] (str n)))

(def fizzbuzz-numbers
  [[3 "fizz"]
   [5 "buzz"]])

(defn fizzbuzz "does fizzbuzz for the given number" [n]
  (let [make-fb-number-fn (fn [n msg] #(when (zero? (mod % n)) msg))
        fizzbuzz-fns (map #(apply make-fb-number-fn %) fizzbuzz-numbers)
        msg (reduce (fn [msg f] (str msg (f n))) "" fizzbuzz-fns)]
    (if (seq msg) msg (str n))))

(doseq [n (range 1 101)]
  (println (fizzbuzz n)))

(defn make-lines
  "joins all the passed strings with a newline"
  [& lines]
  (apply (partial clojure.string/join "\n") lines))

(m/md-to-html-string (make-lines ["# Hello, World!"
                                  ""
                                  "Here is some markdown[^1]"
                                  ""
                                  "- one"
                                  "- two"
                                  "- three"
                                  ""
                                  "```clojure"
                                  "(def foo \"bar\")"
                                  "```"
                                  ""
                                  "[^1]: With some exceptions"])
                     :heading-anchors true :footnotes? true)

(into [] (filter even?) (range 1 11))

(def people
  [{:name "one" :sales 1}
   {:name "two" :sales 2}
   {:name "three" :sales 1}
   {:name "four" :sales 2}
   {:name "five" :sales 3}
   {:name "six" :sales 2}
   {:name "seven" :sales 2}])

(defn reduce-by [group-by-f reducer-f coll]
  (reduce-kv (fn [m k v] (assoc m k (reducer-f v)))
             {} (group-by group-by-f people)))

(reduce-by :sales #(map :name %) people)
