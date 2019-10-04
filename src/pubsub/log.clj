(ns pubsub.log
  (:require
   [taoensso.timbre.appenders.core :refer (println-appender)]
   [taoensso.timbre.appenders.3rd-party.rolling :refer (rolling-appender)]
   [taoensso.timbre :refer (refer-timbre set-level! merge-config!)]))

(refer-timbre)

(defn setup
  "See https://github.com/ptaoussanis/timbre"
  []
  (merge-config! {:appenders {:rolling (rolling-appender {:path "pubsub.log" :pattern :weekly})
                              :println (println-appender {:stream :auto})}})
  (merge-config!  {:timestamp-opts {:timezone  (java.util.TimeZone/getDefault)}}))

(comment
  (setup)
  (info "bla"))
