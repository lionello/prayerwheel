<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--
Design by Free CSS Templates
http://www.freecsstemplates.org
Released for free under a Creative Commons Attribution 2.5 License
-->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<title>Prayer Wheel</title>
<meta name="keywords" content="buddhist, prayer wheel, prayer, prayerwheel, mindfulness" />
<?php
require 'Mobile_Detect.php';
$md = new Mobile_Detect();
if ($md->isMobile()) echo "<style type='text/css'>#qrcode { display:none; }</style>"
?>
<!-- <link href="screen.css" rel="stylesheet" type="text/css" media="screen" /> -->
<link href="handheld.css" rel="stylesheet" type="text/css" media="all" />
</head>
<body>
<!-- start header -->
<div id="header">
<!--  	<div id="menu">
		<ul id="main">
			<li class="current_page_item"><a href="#" class="first">Homepage</a></li>
			<li><a href="#">Spin the wheel</a></li>
			<li><a href="#">Contact</a></li>
		</ul>
	</div>-->
	<div id="logo">
		<h1><span>Prayer Wheel</span></h1>
	</div>
</div>
<!-- end header -->
<div id="wrapper">
	<!-- start page -->
	<div id="page">
		<div id="sidebar1" class="sidebar">
			<ul>
				<li id="sidebarwheel"><img src="http://www.dharma-haven.org/tibetan/prwhbl1.gif"/>
				</li>
				<li>
					<h2>Number of turns</h2>
<?php require_once 'db.php'; ?>
					<p>Prayer wheel app users have sent <?=$total?> prayers into the world.</p>
				</li>
				<li>
					<h2>Causes</h2>
					<p>Dedicate your prayers to a good cause! Choose one yourself or have a look at the suggestions below:</p>
					<ul>
						<li><a href="http://www.peace.org/">Peace</a></li>
						<li><a href="http://www.wisdompage.com/">Wisdom</a></li>
						<li><a href="http://www.spiritualnow.com/">Spiritual enlightenment</a></li>
						</ul>
				</li>
				</ul>
		</div>
		<!-- start content -->
		<div id="content">
			<div class="post">
				<h1 class="title">About prayer wheels</h1>
				<div class="entry">
					<p><strong>Prayer wheels </strong> are cylindrical 'wheels' with prayers written on them - traditionally, the mantra <em>Om Mani Padme Hum</em>. Prayer wheels are used by buddhists in and around Tibet. The wheels turn on a spindle; each revolution sends a prayer into the world. It is believed that spinning a prayer wheel is the same as orally reciting the prayer. Digital prayer wheels are supposed to work the same way, radiating peaceful prayers all around your computer or handheld.</p>
					<p><em>Mani</em> wheels are always spun clockwise, so the syllables of the mantra are rotated in such a way that they pass the viewer in the order they would be read.
				</div>
			</div>
			<div class="post">
				<h2 class="title">Use and benefits</h2>
				<div class="entry">
					<p>Prayer wheels are used for meditation. Turn the wheel gently, not too fast or frantically. As you turn the wheel, focus your mind and repeat the <a href="http://en.wikipedia.org/wiki/Om_mani_padme_hum" target="wiki1">Om Mani Padme Hum</a> mantra. Keep in mind the motivation and spirit of compassion for all beings. Apart from harmonizing your environment and increasing positive energy around you, this technique will stabilize your mind and improve mindfulness. Mantra recitation produces significant psychological and physiological relaxation.</p> <p>Tibetans use prayer wheels to accumulate wisdom and merit (good karma) and to purify negativities (bad karma), thus assisting them on their journeys to enlightenment.</p>
				</div>
			</div>
			<div class="post">
				<h2 class="title">Download the prayer wheel app</h2>
				<div class="entry">
					<p>Digital prayer wheels have the same benefits as physical ones. Now you can improve your mindfulness practice anywhere! Download the latest version of the prayer wheel app for your Android phone <a href="#">here</a>.</p>
				</div>
			</div>
		</div>
		<!-- end content -->
		<!-- start sidebars -->
		<div id="sidebar2" class="sidebar">
			<ul>
				<li>
					<h2>Download prayer wheel</h2>
<?php if (!$md->isMobile()) echo "<p><img src='qrcode.png'/></p>"; ?>
					<p><a href="market://details?id=com.lunesu.prayerwheel">Download here for Android</a></p>
				</li>
				<li>
					<h2>Links</h2>
					<ul>
						<li><a href="http://en.wikipedia.org/wiki/Prayer_wheel">Prayer wheel (Wikipedia)</a></li>
						<li><a href="http://www.bbc.co.uk/religion/religions/buddhism/">Buddhism (BBC)</a></li>
						<li><a href="http://en.wikipedia.org/wiki/Mindfulness_(psychology)">Mindfulness (Wikipedia)</a></li>
						<li><a href="http://www.how-to-meditate.org/">How to meditate</a></li>
						<li><a href="http://www.tibetanprayerwheels.com/">More on Tibetan prayer wheels</a></li>
						<li><a href="http://www.ted.com/talks/martin_seligman_on_the_state_of_psychology.html">Martin Seligmann on positive psychology (TED talk)</a></li>
						<li><a href="http://www.google.co.uk/search?q=mindfulness+training+youtube&hl=en&newwindow=1&biw=1260&bih=644&prmd=v&source=univ&tbs=vid:1&tbo=u&ei=bVPfTNrfHZCycfDmkJcM&sa=X&oi=video_result_group&ct=title&resnum=2&ved=0CDUQqwQwAQ" target="google">Mindfulness training videos</a></li>
					</ul>
				</li>
			</ul>
		</div>
		<!-- end sidebars -->
		<div style="clear: both;">&nbsp;</div>
	</div>
	<!-- end page -->
</div>
<div id="footer">
	<p class="copyright">&copy;&nbsp;&nbsp;2010 prayerwheel.net All Rights Reserved<br/>Design by <a href="http://www.freecsstemplates.org/">Free CSS Templates</a>.</p>
</div>
</body>
</html>
