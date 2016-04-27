<?php
	//INCLUDE DB
	include_once 'db_function.php';
	$db = new DB_Functions();
	
	//SELECT a.`TIMESTAMP`, a.`TYPE`, a.`VALUE` FROM (SELECT * FROM `mainTable` WHERE (`TYPE`='ANGLE' OR `TYPE`='TIMESTAMP') ORDER BY CREATED DESC)a ORDER BY CREATED LIMIT 500
	if ((isset($_GET['table']))||(isset($_GET['date']))) {
		if (isset($_GET['date']) && !isset($_GET['table'])) {
			$mysql= "SELECT `TIMESTAMP`, `TYPE`, `VALUE` FROM `mainTable` WHERE `CREATED`='".$_GET['date']."' ORDER BY CREATED DESC LIMIT 30"; //ORDER BY CREATED DESC LIMIT 30
			$result=$db->db_query($mysql);
		} else if (isset($_GET['date']) && isset($_GET['table'])){
			$mysql= "SELECT `TIMESTAMP`, `TYPE`, `VALUE` FROM `mainTable` WHERE ((`TYPE`='".$_GET['table']."' OR `TYPE`='TIMESTAMP') AND (`CREATED` BETWEEN '".$_GET['date']." 00:00:00' AND '".$_GET['date']." 23:59:59')) ORDER BY CREATED, TYPE";
			echo $mysql;
			$result=$db->db_query($mysql);
		} else {
			$mysql= "SELECT a.`TIMESTAMP`, a.`TYPE`, a.`VALUE` FROM (SELECT * FROM `mainTable` WHERE (`TYPE`='".$_GET['table']."' OR `TYPE`='TIMESTAMP') ORDER BY CREATED DESC LIMIT 30)a ORDER BY CREATED, TYPE";
			$result=$db->db_query($mysql);
		}
		
	} else {
		$mysql= "SELECT a.`TIMESTAMP`, a.`TYPE`, a.`VALUE` FROM (SELECT * FROM `mainTable` ORDER BY CREATED DESC LIMIT 500)a ORDER BY CREATED, TYPE";
		$result=$db->db_query($mysql);
	}
	
	$ARR_TIMESTAMP=array();
	$ARR_GESAMT=array();
	$ARR_TITLE = array();
	
	while ($row = mysql_fetch_row($result)) {
		switch($row[1]){
			case "TIMESTAMP":
				$ARR_TIMESTAMP[]=(int)$row[0];
				break;
			default:
				if(in_array($row[1],$ARR_TITLE)){
					$ARR_GESAMT[$row[1]]=array();
					$ARR_TITLE[]=$row[1];
				}
				//$ARR_GESAMT[$row[1]][]=$row[2];
				//echo $row[2];
				$ARR_GESAMT[$row[1]][]=array((int)$row[0],(float)$row[2]);
				//echo $row[2]+"<br>\n";
				break;
		}
// 		print_r($row);
	}
	
	//var_dump($ARR_GESAMT);
	if (isset($_GET['type'])) {
		$ARR_IT = array();
		$ARR_IT[] = $ARR_GESAMT[$_GET['type']];
	} else {
		$ARR_IT=$ARR_GESAMT;
	}
	echo json_encode($ARR_IT);