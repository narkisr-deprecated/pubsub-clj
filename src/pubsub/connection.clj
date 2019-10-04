(ns pubsub.connection
  (:import
   com.google.api.gax.core.CredentialsProvider
   com.google.api.gax.core.NoCredentialsProvider
   com.google.api.gax.grpc.GrpcTransportChannel
   com.google.api.gax.rpc.FixedTransportChannelProvider
   com.google.api.gax.rpc.TransportChannelProvider
   com.google.pubsub.v1.ProjectTopicName
   io.grpc.ManagedChannel
   io.grpc.ManagedChannelBuilder))

(defn channel-builder [hostport]
  (-> (ManagedChannelBuilder/forTarget hostport)
      (.usePlaintext)
      (.build)))

(defn create-channel [host-port]
  (FixedTransportChannelProvider/create
   (GrpcTransportChannel/create (channel-builder host-port))))

(defn creds-provider []
  (NoCredentialsProvider/create))
