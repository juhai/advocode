(defproject day_02 "0.1.0-SNAPSHOT"
  :description "Advent of code 2021 - Day 2"
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot day-02.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
