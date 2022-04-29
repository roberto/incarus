(ns incarus.core-test
  (:require [clj-test-containers.core :as tc]
            [clojure.test :refer :all]
            [taoensso.carmine :as car]
            [incarus.core :as incarus]))

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

(use-fixtures :each setup-container!)
(use-fixtures :once cleanup-containers!)

(defmacro wcar* [& body]
 `(car/wcar {:pool {}
             :spec {:port (get-in *redis-container* [:mapped-ports redis-port])}}
          ~@body))

(deftest incrby+limit-test
  (testing "should increment the value"
    (is (= 9 (wcar* (incarus/incrby+limit "key" 9 10)))))
  (testing "should set the value with the limit itself when the increment exceeds the limit"
    (is (= 10 (wcar* (incarus/incrby+limit "key" 2 10)))))
  (testing "should not increment when the current value is the limit"
    (is (= 10 (wcar* (incarus/incrby+limit "key" 1 10))))))

(deftest decrby+limit-test
  (testing "should decrement the value"
    (is (= -9 (wcar* (incarus/decrby+limit "key" 9 -10)))))
  (testing "should set the value with the limit itself when the decrement exceeds the limit"
    (is (= -10 (wcar* (incarus/decrby+limit "key" 2 -10)))))
  (testing "should not decrement when the current value is the limit"
    (is (= -10 (wcar* (incarus/decrby+limit "key" 1 -10))))))
