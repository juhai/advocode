(defproject day_01 "0.1.0-SNAPSHOT"
  :description "Advent of code 2021 - Day 1"
  :dependencies [[org.clojure/clojure "1.10.1"]]
  :main ^:skip-aot day-01.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
