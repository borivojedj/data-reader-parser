(ns data-reader-parser.core
  (:gen-class
    :methods [#^{:static true} [start [String String String] void]])
  (:require [data-reader-parser.functions :as functions])
  (:require [data-reader-parser.file_reader :as file_reader])
  (:require [data-reader-parser.database_controller :as dbcontroller]))

(defn -start
  "main function to run"
  [dir_path archive_path dir_name]
  (file_reader/read_files (file_reader/only-files (file_reader/read_directory dir_path dir_name)) archive_path))


