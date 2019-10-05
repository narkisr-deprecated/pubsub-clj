(ns pubsub.publish
  "Publishing messages"
  (:require
   [pubsub.connection :refer (creds-provider)])
  (:import
   com.google.pubsub.v1.ProjectTopicName
   com.google.cloud.pubsub.v1.Publisher
   com.google.protobuf.ByteString
   com.google.pubsub.v1.PubsubMessage))

(defn publisher
  ([project topic]
   (-> (Publisher/newBuilder (ProjectTopicName/of project topic))
       (.build)))
  ([c project topic]
   (-> (Publisher/newBuilder (ProjectTopicName/of project topic))
       (.setChannelProvider c)
       (.setCredentialsProvider (creds-provider))
       (.build))))

(defn message [s]
  (-> (PubsubMessage/newBuilder)
      (.setData (ByteString/copyFromUtf8 s))
      (.build)))

(defn publish
  "returns an ApiFuture"
  [p m]
  (.publish p m))
