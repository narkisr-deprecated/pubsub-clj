(ns pubsub.topic
  (:require
   [pubsub.connection :refer (topic-client)])
  (:import
   com.google.cloud.pubsub.v1.TopicAdminClient
   com.google.pubsub.v1.ProjectName
   com.google.pubsub.v1.ProjectTopicName
   com.google.pubsub.v1.Topic))

(defn create-topic [c project-id topic-id]
  (let [topic-name (ProjectTopicName/of project-id topic-id)]
    (.createTopic (topic-client c) topic-name)))

(defn get-topic [c project-id topic-id]
  (try
    (let [topic-name (ProjectTopicName/of project-id topic-id)]
      (.getTopic (topic-client c) topic-name))
    (catch RuntimeException e nil)))
