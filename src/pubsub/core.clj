(ns pubsub.core
  (:require
   [pubsub.connection :refer (create-channel)]
   [pubsub.topic :refer (create-topic get-topic)]
   [pubsub.publish :refer (publisher publish message)]
   [pubsub.subscribe :refer (subscriber await- create-subscription get-subscription)]))

(def project "planets")

(def topic "moon")

(def subscription "astroids")

(defn channel []
  (create-channel "10.8.4.102:8085"))

(defn initialize
  "Create topic and subscription"
  []
  (let [c (channel)
        t (get-topic c project topic)
        s  (get-subscription c project "test-sub")]
    (when-not t
      (create-topic c project topic))
    (when-not s
      (create-subscription c project topic "test-sub"))))

(defn produce []
  (let [c (channel)
        pub (publisher c project topic)]
    (try
      (.get (publish pub (message "hello")))
      (finally
        (.shutdown pub)))))

(def consumer (atom nil))

(defn subscribe []
  (let [c (channel)]
    (reset! consumer (subscriber c project "test-sub"))))

(comment
  (initialize)
  (subscribe)
  (future (await- @consumer))
  (produce))
