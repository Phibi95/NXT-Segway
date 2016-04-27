<?php
if(isset($_GET['I_START'])&&isset($_GET['I_END'])){
	$url = "www.zezation.me/NXT/recieve.php";
	$itemJson = '{
  "user_id" : "1000",
  "sid": "921347843e4",
  "hash" : "",
	"values": {';
	$u=0;
	$itemJson = $itemJson.'
			"'.$u.'": {
				"X" : '.mt_rand(-100,100).',
				"Y" : '.mt_rand(-100,100).',
				"GYROSPEED" : '.mt_rand(-900,900).',
				"MOTORPOSLEFT" : '.mt_rand(0,10000).',
				"MOTORPOSRIGHT" : '.mt_rand(0,10000).',
				"OFFSET" : '.mt_rand(800.25,870.99).',
				"ANGLE" : '.mt_rand(-45,45).',
				"TIMESTAMP" : '.$u.'
				}';
	for ($i = $_GET['I_START']; $i < $_GET['I_END']; $i++) {
		$itemJson = $itemJson.',
			"'.$i.'": {
				"X" : '.mt_rand(-100,100).',
				"Y" : '.mt_rand(-100,100).',
				"GYROSPEED" : '.mt_rand(-900,900).',
				"MOTORPOSLEFT" : '.mt_rand(0,10000).',
				"MOTORPOSRIGHT" : '.mt_rand(0,10000).',
				"OFFSET" : '.mt_rand(800.25,870.99).',
				"ANGLE" : '.mt_rand(-45,45).',
				"TIMESTAMP" : '.$i.'
				}';
	}
	$itemJson = $itemJson.'
		}
    }';
	if (isset($_GET['send'])) {
		$ch = curl_init();
	
	curl_setopt($ch, CURLOPT_URL, $url);
	curl_setopt($ch, CURLOPT_HTTPHEADER, array(
			'Content-Type: application/json',
			'Content-Length: '. strlen($itemJson))
	);
	curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
	curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");
	curl_setopt($ch, CURLOPT_POSTFIELDS, $itemJson);
	$response = curl_exec($ch);
	if(!$response){
		die('Error: "' . curl_error($ch) . '" - Code: ' . curl_errno($ch));
	}
	print_r($response);
	curl_close($ch);
	}
	echo "\n";
	echo "<br>";
	var_dump($itemJson);
}
