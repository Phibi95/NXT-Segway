<?php
$url = "www.zezation.me/NXT/recieve.php";
$itemJson = '{
  "user_id" : "1000",
  "sid": "921347843e4",
  "hash" : "",
	"values": {
			"100000": {
				"X" : 0.01,
				"Y" : 12.01234,
				"GYROSPEED" : 900.2,
				"MOTORPOSLEFT" : 1009,
				"MOTORPOSRIGHT" :1000,
				"OFFSET" : 820.0943,
				"ANGLE" : 2.12,
				"TIMESTAMP" : 100000
				},
			"100001": {
				"X" : 0.01,
				"Y" : 12.01234,
				"GYROSPEED" : 900.2,
				"MOTORPOSLEFT" : 1009,
				"MOTORPOSRIGHT" :1000,
				"OFFSET" : 820.0943,
				"ANGLE" : 2.12,
				"TIMESTAMP" : 100000
				},
			"100002": {
				"X" : 0.01,
				"Y" : 12.01234,
				"GYROSPEED" : 900.2,
				"MOTORPOSLEFT" : 1009,
				"MOTORPOSRIGHT" :1000,
				"OFFSET" : 820.0943,
				"ANGLE" : -12.12,
				"TIMESTAMP" : 100000
				},
			"100003": {
				"X" : 0.01,
				"Y" : 12.01234,
				"GYROSPEED" : 900.2,
				"MOTORPOSLEFT" : 1009,
				"MOTORPOSRIGHT" :1000,
				"OFFSET" : 820.0943,
				"ANGLE" : 4.12,
				"TIMESTAMP" : 100000
				},
			"100004": {
				"X" : 0.01,
				"Y" : 12.01234,
				"GYROSPEED" : 900.2,
				"MOTORPOSLEFT" : 1009,
				"MOTORPOSRIGHT" :1000,
				"OFFSET" : 820.0943,
				"ANGLE" : 4.12,
				"TIMESTAMP" : 100000
				},
			"200023": {
				"X" : 0.01,
				"Y" : 12.01234,
				"GYROSPEED" : 900.2,
				"MOTORPOSLEFT" : 1009,
				"MOTORPOSRIGHT" :1000,
				"OFFSET" : 820.0943,
				"ANGLE" : -24.12,
				"TIMESTAMP" : 100000
				},
			"202023": {
				"X" : 0.01,
				"Y" : 12.01234,
				"GYROSPEED" : 900.2,
				"MOTORPOSLEFT" : 1009,
				"MOTORPOSRIGHT" :1000,
				"OFFSET" : 820.0943,
				"ANGLE" : 70.12,
				"TIMESTAMP" : 100000
				}
		}
    }';

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