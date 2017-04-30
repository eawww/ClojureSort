(ns thatclojureproject.core (:require [clojure.java.io :as io]))

(def filename "numbers.txt")

(defn numbers-list []
  (into [] (with-open [rdr (io/reader filename)]
            (doall (map read-string (line-seq rdr))))))
