(ns incarus.core-test
  (:require [clojure.test :refer :all]
            [incarus.core :as incarus]
            [incarus.aux.redis-container :as aux.container]))

(use-fixtures :each aux.container/setup-container!)
(use-fixtures :once aux.container/cleanup-containers!)

(deftest incrby+limit-test
  (testing "should increment the value"
    (is (= 9 (aux.container/wcar* (incarus/incrby+limit "key" 9 10)))))
  (testing "should set the value with the limit itself when the increment exceeds the limit"
    (is (= 10 (aux.container/wcar* (incarus/incrby+limit "key" 2 10)))))
  (testing "should not increment when the current value is the limit"
    (is (= 10 (aux.container/wcar* (incarus/incrby+limit "key" 1 10))))))

(deftest decrby+limit-test
  (testing "should decrement the value"
    (is (= -9 (aux.container/wcar* (incarus/decrby+limit "key" 9 -10)))))
  (testing "should set the value with the limit itself when the decrement exceeds the limit"
    (is (= -10 (aux.container/wcar* (incarus/decrby+limit "key" 2 -10)))))
  (testing "should not decrement when the current value is the limit"
    (is (= -10 (aux.container/wcar* (incarus/decrby+limit "key" 1 -10))))))
