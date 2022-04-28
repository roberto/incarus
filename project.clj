(defproject incarus "0.1.0-SNAPSHOT"
  :description "Redis/Carmine increment/decrement by with limit"
  :url "http://github.com/roberto/incarus"
  :license {:name "MIT"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.taoensso/carmine "3.1.0" :exclusions [org.clojure/clojure]]]
  :profiles {:dev {:source-paths ["config" "resources"]
                   :resource-paths ["resources"]
                   :dependencies [[clj-test-containers "0.7.0"]
                                  [org.clojure/tools.namespace "0.2.10"]]}}
  :repl-options {:init-ns user}
  :resource-paths ["resources"])
