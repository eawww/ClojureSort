(ns thatclojureproject.core (:require [clojure.java.io :as io]))

(def filename "numbers_txt.txt") ;const defines filename for easy changin'

;loosely based on
;http://stackoverflow.com/questions/9047231/read-a-file-into-a-list-each-element-represents-one-line-of-the-file
(defn getlistfromfile []
  (with-open [readr (io/reader filename)]
    (doall (map read-string (line-seq readr)));jams all the values as ints into list
  )
)

(def numberlst (getlistfromfile)) ;for some dumb reason, we have to do it this way to treat the return of the function as a collection


(defn merg
  "merges two sorted lists into one sorted list"
  [leftlst rightlst]
  (loop [lf leftlst rt rightlst result [] ]
    (cond ;build merged list out of first elements of each
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
         (apply merg 
          (map mergsort 
           (split-at (quot (count numbers) 2) numbers)
           ;split at because partition can drop elements off the end
           ;quot because split-at will return empty if given a fraction index
           );divide list in half and apply merge algorithm to sorted
          );results of each side
   )
)

;referenced idea from answer on
;http://stackoverflow.com/questions/36731188/multithreaded-merge-sort-algorithm-in-clojure
;but made it my own and compacted it into a single function
(defn threadulesque-mergsort 
  "Lets you specify number of threads (please only powers of 2)"
  [numbers threads]
  (if (< (count numbers) 2) 
    numbers ;just in case we're already at size 1 or 0
    (if (> threads 2) ;see if we've made enough threads
	    (apply merg
	           (pmap threadulesque-mergsort
	                 (split-at (quot (count numbers) 2) numbers)
	                 [(quot threads 2) (quot threads 2)] ;divide threads by 2
            )                                          ;on each recurse
	    ) ;if it's the last time, we don't need to recur into this function
	    (apply merg ;but, rather the basic merge sort
	           (pmap mergsort
	                 (split-at (quot (count numbers) 2) numbers)
	            )
	     )
	   )
  )
)

(println "1 thread")
(time (mergsort numberlst))
(println "2 threads")
(time (threadulesque-mergsort numberlst 2))
(println "4 threads")
(time (threadulesque-mergsort numberlst 4))
(println "8 threads")
(time (threadulesque-mergsort numberlst 8))
(println "16 threads")
(time (threadulesque-mergsort numberlst 16))
(println "32 threads")
(time (threadulesque-mergsort numberlst 32))
      