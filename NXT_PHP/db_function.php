<?php
 
class DB_Functions {
 
    private $db;
 
    //put your code here
    // constructor
    function __construct() {
        include_once 'db_connect.php';
        // import database connection variables
        include_once 'config.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
         
    }
    
    public function db_query($mysql){
    	$result=mysql_query($mysql) or die(mysql_error());
    	//$this->dblog->genLog("Result: ".mysql_num_rows($result));
    	return $result;
    	
    }
    
    public function select($select,$from){
    	$select = mysql_real_escape_string($select);
    	$from = mysql_real_escape_string($from);
    	
    	$mysql="SELECT $select FROM $from";
    	return $this->db_query($mysql);
    }
    
    public function selectfull($select,$from,$where){
    	$select = mysql_real_escape_string($select);
    	$from = mysql_real_escape_string($from);
    	
    	$mysql="SELECT $select FROM $from WHERE $where";
    	return $this->db_query($mysql);
    }
    
    public function selectall($from){
    	$from = mysql_real_escape_string($from);
    	
    	$mysql="SELECT * FROM $from";
    	return $this->db_query($mysql);
    }
    
    public function selectallfull($from,$where){
    	$from = mysql_real_escape_string($from);
    	
    	$mysql="SELECT * FROM $from WHERE $where";
    	return $this->db_query($mysql);
    }
    
    public function drop($table){
    	$table = mysql_real_escape_string($table);
    	
    	$mysql="DROP TABLE $table";
    	return $this->db_query($mysql);
    }
    
    public function deleteall($from,$where){
    	$from = mysql_real_escape_string($from);
    	 
    	$mysql="DELETE FROM $from WHERE $where";
    	return $this->db_query($mysql);
    }
    
    /**
     * @example UPDATE $table SET $set WHERE $where
     * @param String $table
     * @param String $set
     * @param String $where
     * @return MYSQL-resource
     */
    public function update($table,$set,$where){
    	$table = mysql_real_escape_string($table);
    	
    	$mysql="UPDATE $table SET $set WHERE $where";
    	return $this->db_query($mysql);
    }
}
?>