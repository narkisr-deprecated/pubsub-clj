(ns pubsub.core
  (:require
   [taoensso.timbre :refer (refer-timbre)]
   [pubsub.connection :refer (create-channel)]
   [pubsub.topic :refer (create-topic get-topic)]
   [pubsub.publish :refer (publisher publish message)]
   [pubsub.subscribe :refer (subscriber await- create-subscription get-subscription)]))

(refer-timbre)

(def project "planets")

(def topic "moon")

(def subscription "astroids")

(defn channel []
  (create-channel "10.8.4.102:8085"))

(defn initialize
  "Create topic and subscription"
  [c project topic sub]
  (let [t (get-topic c project topic)
        s  (get-subscription c project sub)]
    (when-not t
      (create-topic c project topic))
    (when-not s
      (create-subscription c project topic sub))))

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
    (reset! consumer
            (subscriber c project subscription
                        (fn [id data] (info "processing" id "with" data))))))

(comment
  (initialize (channel) project topic subscription)
  (subscribe)
  (future (await- @consumer))
  (produce))
