(ns data-reader-parser.file_reader
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [data-reader-parser.database_controller :as dbcontroller])
  (:require [me.raynes.fs :as fs]))

(def date (java.util.Date.))

(defn format_date
  [date]
  (.format (java.text.SimpleDateFormat. "yyyyMMddHHmmss") date))

(defn read_directory
  "function reads all files in directory"
  [datatype]
  (file-seq (clojure.java.io/file (str "/home/boris/data/" datatype))))


(defn only-files
  "Filters result to only files"
  [file-s]
  (filter #(.isFile %) file-s))

(defn get_type_from_filename
  "returns type from file name"
  [file]
  (subs (.getName file) 0 3))

(defn move_files
  [path_from path_to]
  (me.raynes.fs/rename path_from path_to))

(defn read_file_lazy
  "this function reads file from provided path and inserts every row in database"
  [path]
  (with-open [rdr (io/reader path)]
         (doseq [line (line-seq rdr)]
         (dbcontroller/insert_into_comm_tables (get_type_from_filename path) line "alati_metode.receive")))
  (move_files path (str "/home/boris/data/archive/" (.getName path) "_" (format_date date))))


(defn read_files
  "reads all files from directory"
  [files]
  (doseq [file files]
    (read_file_lazy file)))


