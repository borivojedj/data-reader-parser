# data-reader-and-parser
Reads data about products, clients and supliers, parses them and stores them in database

core.clij
defined two functions for reading files and parsing data.

Function read_master_data:
using parameters dir_path and dir_name, reads certain file, inserts data in receive table in database and moves file to archive (path for archive is passed as parameter).

Folder structure should be:
dir_path/clients
dir_path/suppliers
dir_path/products
dir_path/archive

Function parse_data:
Reads data from receive table in database, parses it, stores it in tables products, clients or suppliers and moves data from receive to receive_log table.
For products, parameter passed to function should be "PRO", for clients "CLI" and for suppliers "SUP".
If some of the rows already exist in the database, function should update its values.


file_reader.clij
Has functions for reading whole directory, filtering results to only files and moving files to another directory. 
Also, there is a function that reads every line from file and calls funtion that inserts the data in database

database_controller.clij
Functions for CRUD operations with database.

parser.clij
Functions for parsing data from receive and inserts or updates in products, clients or suppliers table.
