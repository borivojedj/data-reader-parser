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

(defn select_data_from_receive_by_id
  "returns row with provided id"
  [rec_id]
  (sql/query db-spec ["SELECT * FROM alati_metode.receive WHERE rec_id=?" rec_id]))

(defn insert_into_comm_tables
  "inserts record in some communication table (receive, receive_log or receive_errors)"
  [type data table ]
    (sql/insert! db-spec table
  {:rec_type type :rec_data data}))

(defn delete_from_receive
  "deletes row from receive table"
  [receive_id]
  (sql/delete! db-spec :alati_metode.receive ["rec_id=?" receive_id]))

(defn insert_into_products
  "inserts data in products table"
  [product_code product_name product_shelf_life]
  (sql/insert! db-spec :alati_metode.products
  {:product_code product_code :product_name product_name :product_shelf_life product_shelf_life}))

(defn insert_into_clients
  "inserts data in clients table"
  [client_code client_name client_phone client_email client_address]
  (sql/insert! db-spec :alati_metode.clients
  {:client_code client_code :client_name client_name :client_phone client_phone :client_email client_email :client_address client_address}))

(defn insert_into_suppliers
  "inserts data in suppliers table"
  [supplier_code supplier_name supplier_phone supplier_email supplier_address]
  (sql/insert! db-spec :alati_metode.suppliers
  {:supplier_code supplier_code :supplier_name supplier_name :supplier_phone supplier_phone :supplier_email supplier_email :supplier_address supplier_address}))

(defn find_by_code
  "finds record by code from provided table"
  [table_name column_name value]
  (sql/query db-spec [(str "SELECT * FROM alati_metode." table_name " WHERE " column_name "=?") value]))

(defn update_product
  [product_code product_name product_shelf_life]
  (sql/update! db-spec :alati_metode.products 
               {:product_name product_name :product_shelf_life product_shelf_life}
               ["product_code=?" product_code]))

(defn update_client
  [client_code client_name client_phone client_email client_address]
  (sql/update! db-spec :alati_metode.clients
               {:client_name client_name :client_phone client_phone :client_email client_email :client_address client_address}
               ["client_code=?" client_code]))

(defn update_supplier
  [supplier_code supplier_name supplier_phone supplier_email supplier_address]
  (sql/update! db-spec :alati_metode.suppliers 
               {:supplier_name supplier_name :supplier_phone supplier_phone :supplier_email supplier_email :supplier_address supplier_address}
               ["supplier_code=?" supplier_code]))
