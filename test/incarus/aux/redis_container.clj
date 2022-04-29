(ns incarus.aux.redis-container
  (:require [clj-test-containers.core :as tc]
            [taoensso.carmine :as car]))

(def redis-port 6379)

(def ^:dynamic *redis-container* nil)

(defn start-container []
  (-> (tc/create {:image-name    "redis"
                  :exposed-ports [redis-port]})
      (tc/start!)))

(defn setup-container! [f]
  (binding [*redis-container* (start-container)]
    (f)
    (tc/stop! *redis-container*)))

(defn cleanup-containers! [f] (f)
  (tc/perform-cleanup!))

(defmacro wcar* [& body]
 `(car/wcar {:pool {}
             :spec {:port (get-in *redis-container* [:mapped-ports redis-port])}}
          ~@body))
