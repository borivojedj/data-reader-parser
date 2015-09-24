(ns data-reader-parser.parser
 (:require [data-reader-parser.database_controller :as dbcontroller])
 (:require [clojure.string :as str]))


(defn get_data_by_type
  "gets data from receive table"
  [datatype]
  (dbcontroller/select_data_from_receive datatype))


(defn split_record
  [record]
  (str/split record #";" -1))

(defn replace_empty
  [s]
  (if (str/blank? s)
    "/"
    s))

(defn product_action
  "if product exists update it, if not run insert statement"
  [record record_id]
  (do(if (empty? (dbcontroller/find_by_code "products" "product_code" (record 0)))
      (dbcontroller/insert_into_products (record 0) (record 1) (record 2) (record 3))
      (dbcontroller/update_product (record 0) (record 1) (record 2) (record 3)))
      (dbcontroller/insert_into_receive_log "PRO" record "alati_metode.receive_log")
      (dbcontroller/delete_from_receive record_id)))

(defn client_action
  [record record_id]
  (do (if (empty? (dbcontroller/find_by_code "clients" "client_code" (record 0)))
       (dbcontroller/insert_into_clients 
         (replace_empty(record 0)) 
         (replace_empty(record 1)) 
         (replace_empty(record 2)) 
         (replace_empty(record 3))
         (replace_empty(record 4)))
       (dbcontroller/update_client  
         (replace_empty(record 0)) 
         (replace_empty(record 1)) 
         (replace_empty(record 2)) 
         (replace_empty(record 3))
         (replace_empty(record 4))))
       (dbcontroller/insert_into_receive_log "CLI" record "alati_metode.receive_log")
       (dbcontroller/delete_from_receive record_id)))

(defn supplier_action
  [record record_id]
  (do (if (empty? (dbcontroller/find_by_code "suppliers" "supplier_code" (record 0)))
    (dbcontroller/insert_into_suppliers          
         (replace_empty(record 0)) 
         (replace_empty(record 1)) 
         (replace_empty(record 2)) 
         (replace_empty(record 3))
         (replace_empty(record 4)))
    (dbcontroller/update_supplier 
         (replace_empty(record 0)) 
         (replace_empty(record 1)) 
         (replace_empty(record 2)) 
         (replace_empty(record 3))
         (replace_empty(record 4))))
    (dbcontroller/insert_into_receive_log "SUP" record "alati_metode.receive_log")
    (dbcontroller/delete_from_receive record_id)))


(defn parsing_data
  [record datatype record_id]
  (case datatype
    "PRO" (product_action record record_id)
    "CLI" (client_action record record_id)
    "SUP" (supplier_action record record_id)
    (println "Unknown type of data! " datatype)))

(defn read_data
  "reads every record returned from db"
  [maps datatype]
  (doseq [map maps]
    (do (println "fetching row...")(parsing_data (split_record (map :rec_data)) datatype (map :rec_id)) (println "fetching other row..."))))

(defn parsing
  "main function to call for parsing data"
  [datatype]
  (do (println "parsing started...")(read_data (get_data_by_type datatype) datatype)))
