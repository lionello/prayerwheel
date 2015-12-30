<?php 
require "db.php";

if ($banned)
	$almost="500,000";
else
        $almost="100,000";

require "Mobile_Detect.php";
$md = new Mobile_Detect();
?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<!--
Design by Free CSS Templates
http://www.freecsstemplates.org
Released for free under a Creative Commons Attribution 2.5 License
-->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>Prayer Wheel</title>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="buddhist, prayer wheel, prayer, prayerwheel, mindfulness" />
<?php 
if ($md->isMobile()) echo <<<EOT
	<link href="handheld.css" rel="stylesheet" type="text/css" />
	<meta name = "viewport" content = "width = device-width, initial-scale = 1.0, user-scalable = yes"/> 
EOT;
else echo '<link href="screen.css" rel="stylesheet" type="text/css" media="screen" />';
?>
	<link href="handheld.css" rel="stylesheet" type="text/css" media="handheld" />
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
				<!-- <li id="sidebarwheel"><img src="http://www.dharma-haven.org/tibetan/prwhbl1.gif"/>
				</li> -->
				<li>
					<h2>Number of turns</h2>
<div class="meter-wrap">
    <div class="meter-value" style="background-color: #0a0; width: <?=min(100,intval($total)/10000)?>%;">
        <div class="meter-text">
            <?=$total?>
        </div>
    </div>
</div>
					<p>Prayer wheel app users have sent <strong><?=$total?></strong> prayers into the world.</p>
<?php 
if (intval($count) > 1) {
	if ($calledfromapp)
		echo "<li><h2>Your prayers: $count</h2></li>";
	else if ($nick)
		echo "<li><h2>Prayers from ".htmlentities($nick).": $count</h2></li>";
}
$res = 32;
if ($md->isMobile())
	$res = 16;
?>
				<p>
				<a href="http://twitter.com/share?url=http://prayerwheel.net&text=%23android %23prayerwheel app users have uploaded <?=$count?> prayers."><img src="img/twitter_<?=$res?>.png"/></a>
				&nbsp;
				<a href="http://www.facebook.com/sharer.php?u=http://prayerwheel.net&t=Android Prayer Wheel app users have uploaded <?=$count?> prayers."><img src="img/facebook_<?=$res?>.png"/></a>
				</p>
				</li>
<!-- 				<li>
					<h2>Share this page</h2>
		 			<iframe src="http://www.facebook.com/plugins/like.php?href=http%3A%2F%2Fprayerwheel.net%2F&amp;layout=button_count&amp;show_faces=true&amp;width=320&amp;action=like&amp;colorscheme=light&amp;height=21" scrolling="no" frameborder="0" style="border:none; overflow:hidden; width:320px; height:21px;" allowTransparency="true"></iframe>
					<a href="http://twitter.com/share" class="twitter-share-button" data-text="#android #prayerwheel app users have uploaded <?=$count?> prayers." data-count="none">Tweet</a><script type="text/javascript" src="http://platform.twitter.com/widgets.js"></script>				
			 	</li> -->
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
				<h1 class="title">New version: Sanskrit script!</h1>
                                <div class="entry">
                                        <p>Download the latest version from the Android App Market. I dedicate this new version to my friend Glen El-Hayek. Miss you, man.</p>
                                </div>
                        </div>
                        <div class="post">
                                <h1 class="title">Almost <?=$almost?> prayers!</h1>
				<div class="entry">
					<p>We are nearing the <?=$almost?> prayer mark!</p>
<?php if ($banned != true) echo '<p>Some users have uploaded an artifically incremented prayer count. I have removed the invalid entries from the prayer count, which is why the count is about ~300,000 lower than it was.</p>';?>
					<p>To celebrate the occasion I'm adding additional mantra's to the Prayer Wheel app. Check for updates soon!</p>
				</div>
			</div>
			<div class="post">
				<h1 class="title">About prayer wheels</h1>
				<div class="entry">
					<p><strong>Prayer wheels </strong> are cylindrical 'wheels' with prayers written on them - traditionally, the mantra <em>Om Mani Padme Hum</em>. 
					Prayer wheels are used by buddhists in and around Tibet. The wheels turn on a spindle; each revolution sends a prayer into the world. 
					It is believed that spinning a prayer wheel is the same as orally reciting the prayer. 
					Digital prayer wheels are supposed to work the same way, radiating peaceful prayers all around your computer or handheld.</p>
					<p><em>Mani</em> wheels are always spun clockwise, so the syllables of the mantra are rotated in such a way that they pass the viewer in the order they would be read. </p>
				</div>
			</div>
			<div class="post">
				<h2 class="title">Use and benefits</h2>
				<div class="entry">
					<p>Prayer wheels are used for meditation. Turn the wheel gently, not too fast or frantically. 
					As you turn the wheel, focus your mind and repeat the <a href="http://en.wikipedia.org/wiki/Om_mani_padme_hum" target="wiki1">Om Mani Padme Hum</a> mantra. Keep in mind the motivation and spirit of compassion for all beings. Apart from harmonizing your environment and increasing positive energy around you, this technique will stabilize your mind and improve mindfulness. Mantra recitation produces significant psychological and physiological relaxation.</p> <p>Tibetans use prayer wheels to accumulate wisdom and merit (good karma) and to purify negativities (bad karma), thus assisting them on their journeys to enlightenment.</p>
				</div>
			</div>
			<div class="post">
				<h2 class="title">Download the app</h2>
				<div class="entry">
					<p>Digital prayer wheels have the same benefits as physical ones. Now you can improve your mindfulness practice anywhere! 
					Download the latest version of the prayer wheel app for your Android phone <a href="market://details?id=com.lunesu.prayerwheel">here</a>.</p>
				</div>
			</div>
<?php 
if (!$md->isMobile()) echo <<<EOT
			<div class="post">
				<div class="entry">
<script type="text/javascript"><!--
google_ad_client = "pub-1281712540525267";
/* 468x60, created 11/13/10 */
google_ad_slot = "7629288208";
google_ad_width = 468;
google_ad_height = 60;
//-->
</script>
<script type="text/javascript" src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
</script>
				</div>
			</div>
EOT;
?>
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
<?php 
if (!$calledfromapp && $md->isMobile()) echo <<<EOT
				<li>
<!-- adsense -->
<script type="text/javascript"><!--
window.googleAfmcRequest = {
  client: 'ca-mb-pub-1281712540525267',
  ad_type: 'text',
  output: 'html',
  channel: '7873676540',
  format: '320x50_mb',
  oe: 'utf8',
  color_border: '009C00',
  color_bg: '663300',
  color_link: 'FFCC00',
  color_text: 'FFCC00',
  color_url: 'FF6600',
};
//-->
</script>
<script type="text/javascript" src="http://pagead2.googlesyndication.com/pagead/show_afmc_ads.js"></script>
				</li>
EOT;
?>
			</ul>
		</div>
		<!-- end sidebars -->
		<div style="clear: both;">&nbsp;</div>
	</div>
	<!-- end page -->
</div>
<div id="footer">
	<p class="copyright">&copy;&nbsp;&nbsp;2011 prayerwheel.net All Rights Reserved<br/>Design by <a href="http://www.freecsstemplates.org/">Free CSS Templates</a>.</p>
</div>
</body>
</html>
