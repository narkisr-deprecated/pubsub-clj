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

(def reciever
  (reify MessageReceiver
    (receiveMessage [this message consumer]
      (try
        (let [id (.getMessageId message) data (.toStringUtf8 (.getData message))]
          (info "got message" id "with data" data)
          (.ack consumer))
        (catch Exception e
          (error "got" e))))))

(defn await- [sub]
  (doto sub
    (.startAsync)
    (.awaitRunning)
    (.awaitTerminated)))

(defn build [c sub-name]
  (-> (Subscriber/newBuilder sub-name reciever)
      (.setChannelProvider c)
      (.setCredentialsProvider (creds-provider))
      (.build)))

(defn subscriber
  [c project sub-id]
  (let [sub (build c (ProjectSubscriptionName/of project sub-id))]
    (try
      (await- sub)
      (finally
        (.stopAsync sub)))))
