(ns data-reader-parser.file_reader
  (:require [clojure.java.io :as io])
  (:require [clojure.string :as str])
  (:require [data-reader-parser.database_controller :as dbcontroller])
  (:require [me.raynes.fs :as fs])
  (:require [data-reader-parser.parser :as parser]))

(def date (java.util.Date.))

(defn format_date
  [date]
  (.format (java.text.SimpleDateFormat. "yyyyMMddHHmmss") date))

(defn read_directory
  "function reads all files in directory"
  [dir_path datatype]
  (file-seq (clojure.java.io/file (str dir_path datatype))))


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
  [path archive_path]
  (with-open [rdr (io/reader path)]
         (doseq [line (line-seq rdr)]
         (dbcontroller/insert_into_comm_tables (get_type_from_filename path) line "alati_metode.receive")))
  (move_files path (str archive_path (.getName path) "_" (format_date date))))

(defn get_document_header
  [line]
  (str (nth (parser/split_record line) 0) ";"(nth (parser/split_record line) 1) ";" (nth (parser/split_record line) 2)))

(defn get_document_line
  [line]
  (str (nth (parser/split_record line) 0) ";"(nth (parser/split_record line) 3) ";" (nth (parser/split_record line) 4)))

(defn read_document_file
  [path archive_path datatype]
  (with-open [rdr (io/reader path)]
         (doseq [line (line-seq rdr)]
         (if (empty? (dbcontroller/find_document_by_number datatype (first (parser/split_record line))))
           (do (dbcontroller/insert_into_comm_tables (get_type_from_filename path) (get_document_header line) "alati_metode.receive")
             (dbcontroller/insert_into_comm_tables (str(get_type_from_filename path) "L") (get_document_line line) "alati_metode.receive"))
           (dbcontroller/insert_into_comm_tables (str(get_type_from_filename path) "L") (get_document_line line) "alati_metode.receive"))))
  (move_files path (str archive_path (.getName path) "_" (format_date date))))

(defn read_files
  "reads all files from directory"
  [files archive_path]
  (doseq [file files]
    (read_file_lazy file archive_path)))

(defn read_document_files
  "reads all files from directory"
  [files archive_path datatype]
  (doseq [file files]
    (read_document_file file archive_path datatype)))
