(ns pubsub.core
  (:require
   [pubsub.connection :refer (create-channel topic)]
   [pubsub.topic :refer (create-topic get-topic)]
   [pubsub.publish :refer (publisher publish message)]))

(defn produce []
  (let [creds (creds-provider)
        t (topic "foo" "bar")
        c (create-channel "10.8.4.102:8085")
        pub (publisher c t)]
    (try
      (let [t (get-topic c "foo" "bar")]
        (when-not t
          (create-topic c "foo" "bar")))
      (.get (publish pub (message "hello")))
      (finally
        (.shutdown pub)))))

(comment
  (produce))

