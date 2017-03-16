(defproject think.topic/think.tsne "0.1.0-SNAPSHOT"
  :description "Simple bindings to the TSNE algorithm of dimensionality reduction."
  :url "http://github.com/thinktopic/think.tsne"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [net.mikera/core.matrix "0.58.0"]
                 [net.mikera/vectorz-clj "0.46.0"]
                 [gov.nist.math/jama "1.0.2"]
                 [com.googlecode.efficient-java-matrix-library/core "0.26"]
                 [com.googlecode.efficient-java-matrix-library/equation "0.26"]
                 [com.github.fommil.netlib/all "1.1.2" :extension "pom"]
                 [org.jblas/jblas "1.2.3"]]

  :java-source-paths ["T-SNE-Java/tsne-core/src/main/java"]

  :think/meta {:type :library :tags [:low-level]})
