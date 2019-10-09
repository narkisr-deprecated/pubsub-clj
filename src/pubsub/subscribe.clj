(ns pubsub.subscribe
  "Consuming messages"
  (:require
   [taoensso.timbre :refer (refer-timbre)]
   [pubsub.connection :refer (creds-provider)])
  (:import
   com.google.cloud.pubsub.v1.MessageReceiver
   com.google.cloud.pubsub.v1.Subscriber
   com.google.pubsub.v1.ProjectSubscriptionName
   com.google.pubsub.v1.ProjectTopicName
   com.google.pubsub.v1.ProjectName
   com.google.cloud.pubsub.v1.MessageReceiver
   com.google.cloud.pubsub.v1.SubscriptionAdminClient
   com.google.cloud.pubsub.v1.SubscriptionAdminSettings
   com.google.pubsub.v1.PushConfig))

(refer-timbre)

(defn sub-admin [c]
  (SubscriptionAdminClient/create
   (-> (SubscriptionAdminSettings/newBuilder)
       (.setTransportChannelProvider c)
       (.setCredentialsProvider (creds-provider))
       (.build))))

(defn get-subscription [c project sub-id]
  (try
    (.getSubscription (sub-admin c) (ProjectSubscriptionName/of project sub-id))
    (catch RuntimeException e nil)))

(defn create-subscription
  [c project topic sub-id]
  (let [admin (sub-admin c)
        topic-name (ProjectTopicName/of project topic)
        sub-name (ProjectSubscriptionName/of project sub-id)]
    (.createSubscription admin sub-name topic-name (PushConfig/getDefaultInstance) 0)))

(defn reciever [f]
  (reify MessageReceiver
    (receiveMessage [this message consumer]
      (try
        (let [id (.getMessageId message) data (.toStringUtf8 (.getData message))]
          (debug "got message" id "with data" data)
          (f id data)
          (.ack consumer))
        (catch Exception e
          (error "got" e))))))

(defn await- [sub]
  (try
    (doto sub
      (.startAsync)
      (.awaitRunning)
      (.awaitTerminated))
    (finally
      (.stopAsync sub))))

(defn builder
  ([sub-name f]
   (Subscriber/newBuilder sub-name (reciever f)))
  ([c sub-name f]
   (-> (Subscriber/newBuilder sub-name (reciever f))
       (.setChannelProvider c)
       (.setCredentialsProvider (creds-provider)))))

(defn subscriber
  ([project sub-id f]
   (.build (builder (ProjectSubscriptionName/of project sub-id) f)))
  ([c project sub-id f]
   (.build (builder c (ProjectSubscriptionName/of project sub-id) f))))
