(ns pubsub.publish
  "Publishing messages"
  (:require
   [pubsub.connection :refer (create-channel creds-provider topic)])
  (:import
   com.google.cloud.pubsub.v1.Publisher
   com.google.protobuf.ByteString
   com.google.pubsub.v1.PubsubMessage))

(defn publisher
  [c t]
  (-> (Publisher/newBuilder t)
      (.setChannelProvider c)
      (.setCredentialsProvider (creds-provider))
      (.build)))

(defn message [s]
  (-> (PubsubMessage/newBuilder)
      (.setData (ByteString/copyFromUtf8 s))
      (.build)))

(defn publish
  "returns an ApiFuture"
  [p m]
  (.publish p

            m))


