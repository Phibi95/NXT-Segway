<?php
	//INCLUDE DB
	include_once 'db_function.php';
	$db = new DB_Functions();
	
	if ((isset($_GET['table']))||(isset($_GET['date']))) {
		if (isset($_GET['date']) && !isset($_GET['table'])) {
			$mysql= "SELECT `TIMESTAMP`, `TYPE`, `VALUE` FROM `mainTable` WHERE `CREATED`='".$_GET['date']."'";
		$result=$db->db_query($mysql);
		} else if (isset($_GET['date']) && isset($_GET['table'])){
			$mysql= "SELECT `TIMESTAMP`, `TYPE`, `VALUE` FROM `mainTable` WHERE (`TYPE`='".$_GET['table']."' OR `TYPE`='TIMESTAMP') AND (`CREATED` BETWEEN '".$_GET['date']." 00:00:00' AND '".$_GET['date']." 23:59:59')";
			$result=$db->db_query($mysql);
		} else {
			$mysql= "SELECT `TIMESTAMP`, `TYPE`, `VALUE` FROM `mainTable` WHERE `TYPE`='".$_GET['table']."' OR `TYPE`='TIMESTAMP'";
			$result=$db->db_query($mysql);
		}
		
	} else {
		$mysql= "SELECT `TIMESTAMP`, `TYPE`, `VALUE` FROM `mainTable`";
		$result=$db->db_query($mysql);
	}
	$ARR_TIMESTAMP=array();
	$ARR_X=array();
	$ARR_Y=array();
	$ARR_GYROSPEED=array();
	$ARR_OFFSET=array();
	$ARR_MOTORPOSLEFT=array();
	$ARR_MOTORPOSRIGHT=array();
	$ARR_ANGLE=array();
	
	
	while ($row = mysql_fetch_row($result)) {
		switch($row[1]){
			case "X":
				$ARR_X[]=(float)$row[2];
				break;
			case "Y":
				$ARR_Y[]=(float)$row[2];
				break;
			case "GYROSPEED":
				$ARR_GYROSPEED[]=(float)$row[2];
				break;
			case "OFFSET":
				$ARR_OFFSET[]=(float)$row[2];
				break;
			case "MOTORPOSLEFT":
				$ARR_MOTORPOSLEFT[]=$row[2];
				break;
			case "MOTORPOSRIGHT":
				$ARR_MOTORPOSRIGHT[]=$row[2];
				break;
			case "ANGLE":
				$ARR_ANGLE[]=(float)$row[2];
				break;
			case "TIMESTAMP":
				$ARR_TIMESTAMP[]=(int)$row[0];
				break;
				
		}
// 		print_r($row);
	}
	$ARR_FULL = array();
	$ARR_FULL[] = $ARR_X;
	$ARR_FULL[] = $ARR_Y;
	$ARR_FULL[] = $ARR_GYROSPEED;
	$ARR_FULL[] = $ARR_OFFSET;
	$ARR_FULL[] = $ARR_MOTORPOSLEFT;
	$ARR_FULL[] = $ARR_MOTORPOSRIGHT;
	$ARR_FULL[] = $ARR_ANGLE;
	$ARR_TITLE = array("X","Y","GyroSpeed","Offset","MotorPosLeft","MotorPosRight","Angle");
	
	// include and create object
	include("display/lib/inc/chartphp_dist.php");
	$p = new chartphp();

	
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
									echo $out[$u];
									$u++;
									echo "\n";
									printskript();
								?>
							</div>
							<div id="<?php echo "C$u"; ?>" align="center" style="width:48%; float:right; border:5px solid white">
								<?php 
									echo $out[$u];
									echo "\n";
									printskript();
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
												echo $out[0];
												echo "\n";
												printskript();
											?>
										</div>
									</div>
			<?php 
		}
		?>
		</div>
		<!-- display chart here -->
		
		
	</body>
	</html>
	
<?php 
function printskript(){?>
	<script>
		function getImageData(obj, type, title)
		{
			var str;
			
			imgtype = type;
			
			if (type == 'jpg-pdf')
			{
				imgtype = 'jpg';
				type = 'pdf';
			}
			
			var imgCanvas = obj.jqplotToImageCanvas();
			if (imgCanvas) {
				str = imgCanvas.toDataURL("image/"+imgtype);
			}
			else {
				str = null;
			}
			
			$('#pngdata').val(str); 
			$('#imgtype').val(type); 
			$('#imgtitle').val(title); 
			
			$('#imgform').submit();
			
		}
			</script><?php
}

?>