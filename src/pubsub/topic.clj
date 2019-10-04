(ns pubsub.topic
  (:require
   [pubsub.connection :refer (creds-provider)])
  (:import
   com.google.cloud.pubsub.v1.TopicAdminSettings
   com.google.cloud.pubsub.v1.TopicAdminClient
   com.google.pubsub.v1.ProjectName
   com.google.pubsub.v1.ProjectTopicName
   com.google.pubsub.v1.Topic))

(defn topic-client [c]
  (TopicAdminClient/create
   (-> (TopicAdminSettings/newBuilder)
       (.setTransportChannelProvider c)
       (.setCredentialsProvider (creds-provider))
       (.build))))

(defn create-topic [c project-id topic-id]
  (.createTopic (topic-client c) (ProjectTopicName/of project-id topic-id)))

(defn get-topic [c project-id topic-id]
  (try
    (.getTopic (topic-client c) (ProjectTopicName/of project-id topic-id))
    (catch RuntimeException _ nil)))
