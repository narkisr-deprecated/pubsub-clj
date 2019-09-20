(ns pubsub.core
  (:require
   [pubsub.connection :refer (channel-provider channel-builder creds-provider topic)]
   [pubsub.publisher :refer (publisher publish message)]))

(defn produce []
  (let [channel (channel-provider (channel-builder "10.147.17.46:8085"))
        creds (creds-provider)
        t (topic "foo" "bar")
        p (publisher t channel creds)]
    (publish p (message "hello"))
    (.shutdown p)))
