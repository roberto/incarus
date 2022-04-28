(ns incarus.core
  (:require [clojure.java.io :as io]
            [taoensso.carmine :as car]))

(defn incrby+limit [key increment limit]
  (car/lua (-> "lua/incrby+limit.lua" io/resource slurp)
           {:k key} {:n increment :o limit}))
