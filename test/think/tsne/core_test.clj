(ns think.tsne.core-test
  (:require [clojure.test :refer :all]
            [think.tsne.core :as tsne]
            [clojure.core.matrix :as m]
            [mikera.vectorz.matrix-api]))



(defn random-vector
  []
  (m/array :vectorz (repeatedly 5 rand)))


(defn get-centroid
  [row-seq]
  (let [n-rows (count row-seq)
        row-dim (m/ecount (first row-seq))]
    (m/div (reduce m/add
                   (m/new-array :vectorz [row-dim])
                   row-seq)
           n-rows)))


(defn tsne-result-make-sense?
  [output-matrix]
  (let [first-row-block (vec (take 50 (m/rows output-matrix)))
        second-row-block (vec (drop 50 (m/rows output-matrix)))
        first-centroid (get-centroid first-row-block)
        second-centroid (get-centroid second-row-block)]
    (and (every? (fn [row]
                  (< (m/distance row first-centroid)
                     (m/distance row second-centroid)))
                 first-row-block)
         (every? (fn [row]
                   (< (m/distance row second-centroid)
                      (m/distance row first-centroid)))
                 second-row-block))))


(deftest basic-tsne
  (let [first-centroid (m/array :vectorz [0 200 0 0 20])
        second-centroid (m/array :vectorz [0 0 -100 0 0])
        vec-set-1 (repeatedly 50 (fn [] (m/add first-centroid (m/mul (random-vector) 5))))
        vec-set-2 (repeatedly 50 (fn [] (m/add second-centroid (m/mul (random-vector) 5))))
        input-matrix (m/array :vectorz (concat vec-set-1 vec-set-2))
        output-map (->> tsne/tsne-algo-names
                        (map (fn [algo-name]
                               [algo-name (tsne/tsne input-matrix 2 :tsne-algorithm algo-name)]))
                        (into {}))]
    ;;Need some verification in here.
    (comment
     (doseq [algo-name (keys output-map)]
       (is (tsne-result-make-sense? (get output-map algo-name))
           (format "Algorithm %s failed" algo-name))))))
