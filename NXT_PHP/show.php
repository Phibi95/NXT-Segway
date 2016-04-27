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
	
	// include and create object
	include("display/lib/inc/chartphp_dist.php");
	$p = new chartphp();
	
	$p->width = "90%";
	$p->height = "100%";
	$p->series_label = array("TEST");
	if (isset($_GET['type'])) {
		$p->chart_type = $_GET['type'];
	} else {
		$p->chart_type = "line";
	}
	$debug = array();
	$out = array();
	$out_var = array();
	$typetocarr = array();
	$c=0;
	foreach ($ARR_GESAMT as $type => $werte) {
		//echo $type;
		$p->series_label = array($type);
		$p->title = $type;
		$p->ylabel = $type;
		$p->xlabel = "Timestamp";
		$hilf=array($werte);
		$p->data = $hilf;
		//var_dump($hilf);
		$out_string = $p->render("C$c");
		
		$out_string = str_replace("var","",$out_string);
		
		//Bereinigen des render Codes
		$parts = explode('[[[', $out_string, 2);
		$part1=$parts[0];
		
		$parts = explode(']]]', $parts[1], 2);
		$part2=$parts[1];
		
		
		// replace $(document).ready(function(){
		$part1 = str_replace("<script>  $(document).ready(function(){   ","",$part1);
		//replace  });	    </script>
		$part2 = str_replace(" });	    </script>","",$part2);
		
		$parts = explode('</div>', $part1, 3);
		$part1=$parts[2];
		
		
		$parts = explode('jQuery(\'#', $part2, 2);
		$part2=$parts[0];
		
		$debug[]=$part2;
		
		$typetocarr["valuesC$c"] = $type;
		
		$out_var[] = $part1."valuesC$c".$part2;
		
		
		$out[] = $out_string;
		$c++;
	}
	//echo sizeof($out);
	//var_dump($ARR_GESAMT);
	
	/*
	//CREATE THE DATA ARRAY
	$type= "Angle";
	$ARR_DATA = array();
	foreach ($ARR_FULL as $ARR_NOW) {
		$i=0;
		$ARR_HILF=array();
		if(sizeof($ARR_NOW)>0){
			foreach ($ARR_TIMESTAMP as $TIMESTAMP) {
				$ARR_HILF[] = array("$TIMESTAMP",$ARR_NOW[$i]);
				$i++;
			}
			$ARR_DATA[] = array($ARR_HILF);
		} else {
			$ARR_DATA[] = array();
		}
	}
	
	$p->width = "90%";
	$p->height = "100%";
	$p->series_label = array("TEST");
	if (isset($_GET['type'])) {
		$p->chart_type = $_GET['type'];
	} else {
		$p->chart_type = "line";
	}
	
	
	
	// set few params X(TIMESTAMP) Y(VALUES)
	//$p->data =array(array(3,7,-9,1,4,6,8,2,5),array(5,3,8,2,6,2,9,2,6));
	//$p->data = array(array(array("2010/10",48.25),array("2011/01",238.75),array("2011/02",95.50)));
// 	$p->data = array(array(array(100000,48.25),array(100001,238.75),array(200002,95.50),array(200003,-5.50),array(200004,-25.50),array(200005,15.50),array(200006,15.50),array(200007,15.50),array(200008,15.50),array(200009,15.50),array(105,15.50)));
	$out = array();
	// render chart and get html/js output
	$p->xlabel = "TimeStamp";
	$z=0;
	
	
	foreach ($ARR_DATA as $ARR_RENDER) {
		if(sizeof($ARR_RENDER)>0){
			$p->title = $ARR_TITLE[$z];
			$p->ylabel = $ARR_TITLE[$z]; //TYPE
			$p->data = $ARR_RENDER;
			$out[] = $p->render("C$z");
		}
		$z++;
	}
	*/
	?>
<!DOCTYPE html>
	<html>
	<head>

		<script src="display/lib/js/jquery.min.js"></script>
		<script src="display/lib/js/chartphp.js"></script>
		<link rel="stylesheet" href="display/lib/js/chartphp.css">
		
	</head>
	<body>
		<?php 
// 		var_dump($p->data);
// 		echo "<br>";
// 		print_r(array(array(array("2010/10",48.25),array("2011/01",238.75),array("2011/02",95.50))));
		?>
		
		
		<!-- display chart here -->
		<div>
		<?php
		$max = sizeof($out);
		if ($max>1) {
			for ($u = 0; $u < $max; $u++) {
				if(isset($out[$u])){
					?>
					<div  style="margin-top:5px;position:relative">
							<div id="<?php echo "C$u"; ?>" align="center" style="width:48%; float:left; border:5px solid white">
								<?php 
								echo "<script>var plot_C$u</script>";
									echo $out[$u];
									$u++;
									echo "\n";
								?>
							</div>
							<div id="<?php echo "C$u"; ?>" align="center" style="width:48%; float:right; border:5px solid white">
								<?php 
								echo "<script>var plot_C$u</script>";
									echo $out[$u];
									echo "\n";
								?>
							</div>
						</div>
			<?php
				}
				
			}
		} else if ($max==1) {
			?>
								<div  style="margin-top:5px;position:relative">
										<div id="<?php echo "C0"; ?>" align="center" style="width:95%; float:left; border:5px solid white">
											<?php 
												echo "<script>var plot_C0</script>";
												echo $out[0];
												echo "\n";
											?>
										</div>
									</div>
			<?php 
		}
		?>
		</div>
		<!-- display chart here -->
		
		<script type="text/javascript">
		var refreshtime=1000;
		$(document).ready(function(){  

			setInterval(updateCharts, refreshtime);
			});
			var xmlhttp;

			<?php 
					function updateChart($out,$typearr){
						$c=0;
						foreach ($out as $value) {
							echo "if(plot_C$c){\n";
							echo "plot_C$c.destroy();\n";
							echo "}\n";
							echo "valuesC$c = [obj[\"".$typearr["valuesC$c"]."\"]];\n";
							echo $value;
							echo "\n";
							$c++;
						}
					}
					?>
		
		function updateCharts(){
			if (window.XMLHttpRequest)
			  {// code for IE7+, Firefox, Chrome, Opera, Safari
			  xmlhttp=new XMLHttpRequest();
			  }
			else
			  {// code for IE6, IE5
			  xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
			  }
			xmlhttp.onreadystatechange=function()
			  {
			  if (xmlhttp.readyState==4 && xmlhttp.status==200)
			    {
					var obj = xmlhttp.responseText;
// 					console.log(obj);
					//JSON.parse(obj, function(k, v) {
						//  console.log(k); // Logge den Eigenschaften-Namen, der letzte ist "".
// 						  return v;       // Gib den unveränderten Eigenschaftenwert zurück.
// 						});
					obj=JSON.parse(obj);
					<?php 
						updateChart($out_var,$typetocarr);
					?>
					console.log("updated Charts");
			    }
			}
			xmlhttp.open("GET","http://zezation.me/NXT/getData.php",true);
			xmlhttp.send();
		}</script>

<?php ?>
		
	</body>
	</html>