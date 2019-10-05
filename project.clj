(defproject pubsub-clj "0.2.0"
  :description "Google pubsub in Clojure"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
        [org.clojure/clojure "1.10.1"]
        [com.taoensso/timbre "4.10.0"]
        [com.google.cloud/google-cloud-pubsub "1.90.0"]]

  :plugins [
     [lein-cljfmt "0.6.3"]
     [lein-ancient "0.6.15" :exclusions [org.clojure/clojure]]
     [lein-tag "0.1.0"]
     [lein-set-version "0.3.0"] ]

  :repl-options {:init-ns pubsub.core})
