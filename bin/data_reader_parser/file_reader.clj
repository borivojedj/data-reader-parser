(ns data-reader-parser.file_reader
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str]))

(defn read_directory
  "function reads all files in directory"
  [datatype]
  (file-seq (clojure.java.io/file (str "data/" datatype))))



(defn only-files
  "Filters result to only files"
  [file-s]
  (filter #(.isFile %) file-s))

(defn read_file_lazy
  "this function reads file from provided path"
  [path]
  (with-open [rdr (io/reader path)]
         (doseq [line (line-seq rdr)]
            (println line))))

