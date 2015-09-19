(ns data-reader-parser.database_controller
  (:require [clojure.java.jdbc :as sql]))

(def db-spec {:classname "org.postgresql.Driver"
              :subprotocol "postgresql"
              :subname "//localhost:5432/alati_metode_db"
              :user "postgres"
              :password "log"
              })

(defn select_data_from_receive
  "returns rows of certain type"
  [type]
  (sql/query db-spec ["SELECT * FROM alati_metode.receive WHERE rec_type=?" type]))

(defn insert_into_comm_tables
  "inserts record in some communication table (receive, receive_log or receive_errors)"
  [type data table ]
    (sql/insert! db-spec table
  {:rec_type type :rec_data data}))
