(ns data-reader-parser.database_controller
  (:require [clojure.java.jdbc :as sql])
  (:require [clojure.string :as str]))

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

(defn insert_into_receive_log
  [type data table ]
    (sql/insert! db-spec table
  {:rec_type type :rec_data (str/join ";" data)}))

(defn delete_from_receive
  "deletes row from receive table"
  [receive_id]
  (sql/delete! db-spec :alati_metode.receive ["rec_id=?" receive_id]))

(defn move_to_log
  [record record_id type]
  ((do (println "inserting to log")
     (insert_into_receive_log type record "alati_metode.receive_log")
     (println "deleting from receive")
    (delete_from_receive record_id)
    (println "deleted"))))

(defn insert_into_products
  "inserts data in products table"
  [product_code product_name product_net_weight product_shelf_life]
  (do (println "inserting product " product_code)
    (sql/insert! db-spec :alati_metode.products
  {:product_code product_code :product_name product_name :product_net_weight (Double/parseDouble product_net_weight) :product_shelf_life (Integer/parseInt product_shelf_life) })))

(defn insert_into_clients
  "inserts data in clients table"
  [client_code client_name client_phone client_email client_address]
  (do (println "inserting client " client_code) 
      (sql/insert! db-spec :alati_metode.clients
      {:client_code client_code :client_name client_name :client_phone client_phone :client_email client_email :client_address client_address})))

(defn insert_into_suppliers
  "inserts data in suppliers table"
  [supplier_code supplier_name supplier_phone supplier_email supplier_address]
  (do (println "inserting supplier " supplier_code) 
    (sql/insert! db-spec :alati_metode.suppliers
  {:supplier_code supplier_code :supplier_name supplier_name :supplier_phone supplier_phone :supplier_email supplier_email :supplier_address supplier_address})))

(defn find_by_code
  "finds record by code from provided table"
  [table_name column_name value]
  (sql/query db-spec [(str "SELECT * FROM alati_metode." table_name " WHERE " column_name "=?") value]))

(defn update_product
  [product_code product_name product_net_weight product_shelf_life]
  (do (println "Updating product " product_name)
    (sql/update! db-spec :alati_metode.products 
                {:product_name product_name :product_net_weight (Double/parseDouble product_net_weight) :product_shelf_life (Integer/parseInt product_shelf_life)}
                ["product_code=?" product_code])))

(defn update_client
  [client_code client_name client_phone client_email client_address]
  (do (println "Updating client " client_name)
    (sql/update! db-spec :alati_metode.clients
               {:client_name client_name :client_phone client_phone :client_email client_email :client_address client_address}
               ["client_code=?" client_code])))

(defn update_supplier
  [supplier_code supplier_name supplier_phone supplier_email supplier_address]
  (do (println "Updating supplier " supplier_name)
    (sql/update! db-spec :alati_metode.suppliers 
               {:supplier_name supplier_name :supplier_phone supplier_phone :supplier_email supplier_email :supplier_address supplier_address}
               ["supplier_code=?" supplier_code])))

(defn find_product_by_code
  "finds product by product code"
  [value]
  (find_by_code "products" "product_code" value))

(defn find_client_by_code
  [value]
  (find_by_code "clients" "client_code" value))

(defn find_supplier_by_code
  [value]
  (find_by_code "suppliers" "supplier_code" value))