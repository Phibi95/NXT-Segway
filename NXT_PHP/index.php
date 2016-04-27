<html>
	<head>

		<script src="display/lib/js/jquery.min.js"></script>
		<script src="display/lib/js/chartphp.js"></script>
		<link rel="stylesheet" href="display/lib/js/chartphp.css">
		
	</head>
	<body>
				
		
		<!-- display chart here -->
		<div>
							<div  style="margin-top:5px;position:relative">
							<div id="C0" align="center" style="width:48%; float:left; border:5px solid white">
								    <div style="position:relative;">   <div id="C0" style="height:100%; width:90%;"></div>  </div>
							<div id="C1" align="center" style="width:48%; float:right; border:5px solid white">
								    <div style="position:relative;">   <div id="C1" style="height:100%; width:90%;"></div>  </div>

<script>  
$(document).ready(function(){  

setInterval(test, 2000);
});

function test(){
	var xmlhttp;
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
			
	    }
	  }
	document.getElementById('C0').innerHTML = "Request";
	xmlhttp.open("GET","http://zezation.me/NXT/getChart.php?type=AKKU",true);
	xmlhttp.send();
	}
</script>