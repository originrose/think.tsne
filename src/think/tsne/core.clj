(ns think.tsne.core
  (:require [clojure.core.matrix :as m]
            [clojure.core.matrix.macros :refer [c-for]])
  (:import [com.jujutsu.tsne.barneshut ParallelBHTsne BHTSne]
           [com.jujutsu.utils TSneUtils]
           [com.jujutsu.tsne TSne FastTSne]))

(set! *warn-on-reflection* true)


(defn core-mat-to-double-doubles
  ^"[[D" [matrix]
  (let [mat-shape (m/shape matrix)
        rows (m/rows matrix)
        num-rows (first mat-shape)
        num-cols (second mat-shape)
        ^"[[D" retval (make-array (Class/forName "[D") num-rows)]
    (c-for [idx 0 (< idx num-rows) (inc idx)]
           (aset retval idx (m/to-double-array (m/get-row matrix idx))))
    retval))

(def tsne-algo-names
  [:parallel-bht
   :bht
   :slow-standard-tsne])

(defmulti choose-tsne-algorithm
  (fn [algo-name]
    algo-name))


(defmethod choose-tsne-algorithm :parallel-bht
  [_]
  (ParallelBHTsne.))


(defmethod choose-tsne-algorithm :bht
  [_]
  (BHTSne.))


(defmethod choose-tsne-algorithm :slow-standard-tsne
  [_]
  (FastTSne.))


(defn tsne
  "Run tsne on a clojure.core.matrix.  Returns double [][]"
  ^"[[D" [matrix out-dims & {:keys [perplexity iters tsne-algorithm]
                             :or {perplexity 20.0 iters 1000
                                  tsne-algorithm :parallel-bht}}]
  (let [^TSne tsne (choose-tsne-algorithm tsne-algorithm)
        mat-shape (m/shape matrix)]
    (.tsne tsne (TSneUtils/buildConfig
                 (core-mat-to-double-doubles matrix) (int out-dims)
                 (int (second mat-shape)) (double perplexity)
                 (int iters)))))
