(ns data-reader-parser.core
  (:require [data-reader-parser.functions :as functions])
  (:require [data-reader-parser.file_reader :as file_reader])
  (:require[data-reader-parser.database_controller :as dbcontroller]))

(comment(file_reader/only-files (file_reader/read_directory "clients")))
