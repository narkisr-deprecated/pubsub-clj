(defproject pubsub-clj "0.1.0"
  :description "Google pubsub in Clojure"
  :license  {:name "Apache License, Version 2.0" :url "http://www.apache.org/licenses/LICENSE-2.0.html"}
  :dependencies [
        [org.clojure/clojure "1.10.1"]
        [com.google.cloud/google-cloud-pubsub "1.90.0"]]
  :repl-options {:init-ns pubsub.core})
