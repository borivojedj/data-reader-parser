(ns data-reader-parser.core
  (:gen-class
    :methods [#^{:static true}[read_master_data [String String String] void]
              #^{:static true}[parse_data [String] void]
              #^{:static true}[read_documents [String String String String] void]])
  (:require [data-reader-parser.functions :as functions])
  (:require [data-reader-parser.file_reader :as file_reader])
  (:require [data-reader-parser.database_controller :as dbcontroller])
  (:require [data-reader-parser.parser :as parser]))

(defn -read_master_data
  "reads files for master data"
  [dir_path archive_path dir_name]
  (file_reader/read_files (file_reader/only-files (file_reader/read_directory dir_path dir_name)) archive_path))

(defn -read_documents
  "reads files for documents"
  [dir_path archive_path dir_name datatype]
  (file_reader/read_document_files (file_reader/only-files (file_reader/read_directory dir_path dir_name)) archive_path datatype))

(defn -parse_data
  "function that parses data from receive table"
  [datatype]
  (parser/parsing datatype))
