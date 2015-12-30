<?php

function v3($name) {
    // Calculate hash value
    $hash = md5($name);
    return sprintf('%08s-%04s-%04x-%04x-%12s',
      // 32 bits for "time_low"
      substr($hash, 0, 8),
      // 16 bits for "time_mid"
      substr($hash, 8, 4),
      // 16 bits for "time_hi_and_version",
      // four most significant bits holds version number 3
      (hexdec(substr($hash, 12, 4)) & 0x0fff) | 0x3000,
      // 16 bits, 8 bits for "clk_seq_hi_res",
      // 8 bits for "clk_seq_low",
      // two most significant bits holds zero and one for variant DCE1.1
      (hexdec(substr($hash, 16, 4)) & 0x3fff) | 0x8000,
      // 48 bits for "node"
      substr($hash, 20, 12)
    );
}

$uuid = $_GET['uuid'];
$count = $_GET['count'];

if (intval($count) > 0)
{
	// Ensure it's our hash (GET)
	$ourhash = v3($uuid.'saltyballs'.$count);
	$hash = $_GET['hash'];
	if ($ourhash != $hash)
	{
		// Wrong hash; die
		header("HTTP/1.1 403 Forbidden");
		die("Access denied.");
	}

	require_once 'inc/db.php';

	// Create SQL-safe uuid
	$uuid2 = mysql_real_escape_string($uuid, $link);
	// Create SQL-safe count
	$count2 = mysql_real_escape_string($count, $link);

	// Update the 'count' column for this user
	//mysql_query("INSERT IGNORE Counts (id,uuid) VALUES(NULL,\"$uuid2\")", $link);
	mysql_query("UPDATE Counts SET count = \"$count2\" WHERE uuid = \"$uuid2\" AND count < \"$count2\"", $link)
		or die('mysql_query UPDATE error'.mysql_error());
	if (mysql_affected_rows($link) != 1)
	{
		// Nothing got update, either because of missing UUID or smaller count
		mysql_query("INSERT IGNORE Counts (id,uuid,count) VALUES(NULL,\"$uuid2\",\"$count2\")", $link)
	                or die('mysql_query INSERT error'.mysql_error());
	}
	if ($count > 32000)
	{
	        mysql_query("UPDATE Counts SET banned = 1 WHERE uuid = \"$uuid2\"", $link)
        	        or die('mysql_query UPDATE2 error'.mysql_error());
 	}
	mysql_close($link);

	// Create a temporary session for this uuid
	session_name($uuid);
	session_start();
	$_SESSION['count'] = $count;
}
else
{
	// Upload page was called without a count/hash
}

// Redirect to homepage (but must keep hash)
header("HTTP/1.1 303 See Other");
header("Location: http://$_SERVER[SERVER_NAME]/?uuid=$uuid");

die();

// **************should never get here***************************

// Update the 'nick' for this user
$nick = $_POST[nick];
if (isset($nick))
{
	// Ensure it's our hash (POST)
	/*$ourhash = v3($uuid."saltyballs".$nick);
	$hash = $_POST[hash];
	if ($ourhash != $hash)
	{
		// Wrong hash; die
		header("HTTP/1.1 403 Forbidden");
		die("Access denied.");
	}*/

	// Create SQL-safe nick
	$nick2 = mysql_real_escape_string($nick, $link);

	// Update the nickname for this uuid
	mysql_query('UPDATE Counts SET nickname = "'.$nick2.'" WHERE uuid = "'.$uuid2.'"', $link)
		or die('mysql_query UPDATE2 error'.mysql_error());
}
else
{
	$result = mysql_query('SELECT nickname FROM Counts WHERE uuid = "'.$uuid2.'"', $link);
	$row = mysql_fetch_array($result, MYSQL_NUM);
	if ($row)
		$nick = $row[0];
	mysql_free_result($result);
}

$result = mysql_query('SELECT SUM(count) FROM Counts', $link)
	or die('mysql_query SELECT error'.mysql_error());
$row = mysql_fetch_array($result, MYSQL_NUM)
	or die('mysql_fetch_array error'.mysql_error());
$total = $row[0];
// Free the resources associated with the result set
// This is done automatically at the end of the script
mysql_free_result($result);

mysql_close($link);
unset($link);
?><html>
<head>
	<title>Prayer Wheel</title>
	<link rel="stylesheet" type="text/css" href="prayerwheel.css" />
	<link rel="SHORTCUT ICON" href="/favicon.ico" type="image/x-icon" />
	<meta name = "viewport" content = "width = device-width, initial-scale = 1.0, user-scalable = yes"/>
</head>
<body>
<h1>Prayer Wheel</h1>
<?php
$escaped_nick = htmlentities($nick);
$escaped_uuid = htmlentities($uuid);

if (isset($nick)) {
	echo "<p>Welcome ".$escaped_nick.",</p>";
}
if (isset($count)) {
	echo "<p>Thank you for your ".$count." prayers. </p>";
}
echo "<h2>In total we've received <br/>".$total."<br/> prayers. </h2>";
//echo $ourhash;

if (!$calledfromapp) echo <<<EOT
<!-- adsense -->
<script type="text/javascript"><!--
window.googleAfmcRequest = {
  client: 'ca-mb-pub-1281712540525267',
  ad_type: 'text',
  output: 'html',
  channel: '7873676540',
  format: '320x50_mb',
  oe: 'utf8',
  color_border: 'CCCCCC',
  color_bg: 'CCCCCC',
  color_link: '000000',
  color_text: '333333',
  color_url: '666666',
};
//--></script>
<script type="text/javascript"
   src="http://pagead2.googlesyndication.com/pagead/show_afmc_ads.js"></script>
EOT;
/*else echo <<<EOT
	<p>
	You can set a nickname so that when you share this page with your friends, they can see how many prayers you've uploaded:
	<form action='prayerwheel.php?uuid=$escaped_uuid' method='POST'>
		<input type='text' name='nick' size='12' value='$escaped_nick'/>
		<input type='submit' value='OK'/>
	</form>
	</p>
EOT;*/
?>
</body>
</html>
