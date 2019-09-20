(ns pubsub.publish
  "Publishing messages"
  (:import
   com.google.cloud.pubsub.v1.Publisher
   com.google.protobuf.ByteString
   com.google.pubsub.v1.PubsubMessage))

(defn publisher [t channel creds]
  (-> (Publisher/newBuilder t)
      (.setChannelProvider channel)
      (.setCredentialsProvider creds)
      (.build)))

(defn message [s]
  (-> (PubsubMessage/newBuilder)
      (.setData (ByteString/copyFromUtf8 s))
      (.build)))

(defn publish
  "returns an ApiFuture"
  [p m]
  (.publish p m))


