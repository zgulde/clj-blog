# clj-blog

Yet another blog application, written for me to practice clojure.

## Prerequisites

You will need [Leiningen][1] 1.7.0 or above installed. You can run

    lein deps

to have leiningen pull in the dependencies for you, or just run one of the
commands below and leiningen will figure out that it needs to go get the
dependencies.

Next run:

    cp src/clj_blog/env.example.clj src/clj_blog/env.clj

[1]: https://github.com/technomancy/leiningen

## Running

Run the tests:

    lein test

To start a web server for the application, run:

    lein ring server

To handle database things

    lein run db:migrate # drop tables if they exist, re-create them
    lein run db:seed

    lein run db:refresh # combination of the two above

