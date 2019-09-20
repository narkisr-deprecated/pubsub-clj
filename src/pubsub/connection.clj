(ns pubsub.connection
  (:import
   com.google.api.gax.core.CredentialsProvider
   com.google.api.gax.core.NoCredentialsProvider
   com.google.api.gax.grpc.GrpcTransportChannel
   com.google.api.gax.rpc.FixedTransportChannelProvider
   com.google.api.gax.rpc.TransportChannelProvider
   com.google.cloud.pubsub.v1.TopicAdminClient
   com.google.cloud.pubsub.v1.TopicAdminSettings
   com.google.pubsub.v1.ProjectTopicName
   io.grpc.ManagedChannel
   io.grpc.ManagedChannelBuilder))

(defn channel-builder [hostport]
  (-> (ManagedChannelBuilder/forTarget hostport)
      (.usePlaintext)
      (.build)))

(defn channel-provider [channel]
  (FixedTransportChannelProvider/create (GrpcTransportChannel/create channel)))

(defn creds-provider []
  (NoCredentialsProvider/create))

(defn topic-client [channel creds]
  (TopicAdminClient/create
   (-> (TopicAdminSettings/newBuilder)
       (.setTransportChannelProvider channel)
       (.setCredentialsProvider creds)
       (.build))))

(defn topic [project id]
  (ProjectTopicName/of project id))
