(ns thatclojureproject.core (:require [clojure.java.io :as io]))

(def filename "numbers.txt") ;const defines filename for easy changin'

(defn getlistfromfile []
  (with-open [readr (io/reader filename)]
    (doall (map read-string (line-seq readr)));jams all the values as ints into list
  )
)

(def numberlst (getlistfromfile)) ;for some dumb reason, we have to do it this way to treat the return of the function as a collection

(defn mrg
  "merges two sorted lists into one sorted list"
  [leftlst rightlst]
  (loop [lf leftlst rt rightlst result [] ]
    (cond
      (empty? lf)                 (concat result rt)
      (empty? rt)                 (concat result lf)
      (> (first lf) (first rt))   (recur lf (rest rt) (conj result (first rt)))
      :else                       (recur (rest lf) rt (conj result (first lf)))                    
    )
  )
) ;stagger my closing parentheses because there's no place like C#

;http://www.urbandictionary.com/define.php?term=Merg 
(defn mergsort
  "just your basic merge sort"
  [numbers]
  (if (< (count numbers) 2) numbers
         (apply mrg 
          (map mergsort 
           (split-at(/ (count numbers) 2) numbers)
           )
          )
   )
)

