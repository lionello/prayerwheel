<?php 

require "db.php";

echo "<xml version='1.0' encoding='utf-8'?><prayerwheel><total>$total</total>";
if (isset($count)) {
	echo "<my><uuid>".htmlentities($uuid)."</uuid><count>$count</count><nick>".htmlentities($nick)."</nick></my>";
} 
echo "</prayerwheel>";
