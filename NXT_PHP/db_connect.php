<?php
/**
 * A class file to connect to database
 */
class DB_CONNECT {
 
    // constructor
    function __construct() {
        // connecting to database
        $this->connect();
    }
 
    // destructor
    function __destruct() {
        // closing db connection
        $this->close();
    }
 
    /**
     * Function to connect with database
     */
    function connect() {
        // import database connection variables
        require_once __DIR__ . '/config.php';
 
        // Connecting to mysql database
        $con = mysql_connect(DB_SERVER, DB_USER, DB_PASSWORD) or die($this->error());
 
        // Selecing database
        $db = mysql_select_db(DB_DATABASE) or die($this->error());
 
        // returing connection cursor
        return $con;
    }
 
    /**
     * Function to close db connection
     */
    function close() {
        // closing db connection
        //mysql_close();
    }
    
    function error(){
    	if (DEBUG_MODE==1) {
    		mysql_error();
    	}else {
    		echo "DB Incorrect. Please check again.";
    	}
    }
 
}
?>