<?php

//The Path where the json Data goes to...
$request_body = file_get_contents('php://input');
$json = json_decode($request_body);
if(isset($json)){
//var_dump($json);
//First check User

// $hash = password_hash($passwod, PASSWORD_DEFAULT);

// if (password_verify($password, $hash)) {
// 	// Success!
// }
// else {
// 	// Invalid credentials
// }

//Now update DB
include_once 'db_function.php';
$db = new DB_Functions();
//INSERT INTO `mainTable` (`TIMESTAMP`, `Created`, `USER-ID`, `VALUE`, `TYPE`)
//VALUES ('3', '', '234', '34.00', 'X');
$user_id="";
$sid="";
$pw_hash="";
$i=0;
$result=0;
foreach ($json as $key => $value) {
	switch ($key) {
		case "user_id":
			$user_id=$value;
			break;
		case "sid":
			$sid=$value;
			break;
		case "hash":
			$pw_hash= $value;
			break;
		case "values":
			foreach ($value as $date => $werte) {
				echo $i;
				$timestamp = -1;
				foreach ($werte as $type => $wert) {
					if($type=="TIMESTAMP"){
						$timestamp = $wert;
					}
				}
				
				foreach ($werte as $type => $wert) {
					$mysql= "INSERT INTO `mainTable` (`TIMESTAMP`,`USER-ID`, `VALUE`, `TYPE`, `DATE`) VALUES ('$timestamp', '$user_id', '$wert', '$type', '$date')";
					$db->db_query($mysql);
					$result++;
				}
				$i++;
			}
			break;
		default:
		break;
	}
}
echo "Success!";
echo "Added $result Entitys";
// $mysql= "INSERT INTO `mainTable` (`TIMESTAMP`,`USER-ID`, `VALUE`, `TYPE`) VALUES ('3', '234', '34.00', 'X')";
// $db->db_query($mysql);
} else {
	echo "ERROR NO JSON!";
}