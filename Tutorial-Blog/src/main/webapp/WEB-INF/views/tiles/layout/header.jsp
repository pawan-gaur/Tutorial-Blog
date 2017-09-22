<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Tutorial-Blog</title>
<link href="css/bootstrap.css" rel="stylesheet" type="text/css"/>
<link href="css/style.css" rel="stylesheet" type="text/css" media="all"/>
<script src="js/jquery.min.js"></script>
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<script type="application/x-javascript" addEventListener("load", function(){setTimeout(hideURLbar, 0); }, false); function hideURLbar(){ window.scrollTo(0, 1); }></script>
</head>
<body>
	<!-- Header Section Start -->
	<div class="header">
		<div class="container">
			<div class="logo">
				<a href="#"><h1>Jeldeberg</h1></a>
			</div>
			<div class="pages">
				<ul>
					<li><a class="active" href="#">Feed</a></li>
					<li><a href="#">Article</a></li>
					<li><a href="#">Tutorial</a></li>
				</ul>
			</div>
			<div class="navigation">
				<ul>
					<li><a href="#">About Us</a></li>
					<li><a href="#">Advertise</a></li>
					<li><a href="#">Contact</a></li>
				</ul>				
			</div>
			<div class="clearfix"></div>
		</div>
	</div>
	<!-- Header Sub Menu -->
	<div class="container">
		<div class="header-bottom">
			<div class="type">
				<h5>Article Types</h5>
			</div>
			<span class="menu"></span>
			<div class="list-nav">
				<ul>
					<li><a href="#">Java</a></li>|
					<li><a href="#">Hibernate</a></li>|
					<li><a href="#">Struts</a></li>|
					<li><a href="#">Spring</a></li>|
				</ul>
			</div>
				<script type="text/javascript">
					$("span.menu").click(function(){
						$(".list-nav").slideToggle("slow",function(){
							//Complete
						});
					});
				</script>
			<div class="clearfix"></div>
		</div>
	</div>
</body>
</html>