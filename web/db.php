<?php
/*
CREATE DATABASE prayers;
USE prayers;
CREATE TABLE Counts (id INTEGER PRIMARY KEY AUTO_INCREMENT NOT NULL, uuid CHAR(36) NOT NULL, nickname TEXT, count BIGINT DEFAULT 0,
 	last_update TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP);
CREATE UNIQUE INDEX IDX_Counts_uuid ON Counts (uuid);
GRANT ALL ON prayers.Counts TO ''@'localhost';
*/
require_once 'inc/db.php';

$uuid = $_GET['uuid'];
if (isset($uuid))
{
	// Try to find the temporary session for this uuid
	session_name($uuid);
	session_start();
	$count = $_SESSION["count"];
	if (intval($count) > 0)
	{
		$calledfromapp = true;
		// Destroy the temporary session
		session_destroy();
	}
	else
	{
		// Create SQL-safe uuid
		$uuid2 = mysql_real_escape_string($uuid, $link);
		// Retrieve the current count for this user
		$result = mysql_query("SELECT count FROM prayers.Counts WHERE uuid = \"$uuid2\"", $link);
		$row = mysql_fetch_array($result, MYSQL_NUM);
		if ($row)
			$count = $row[0];
		else
			unset($count);
		mysql_free_result($result);
		/*
		$result = mysql_query('SELECT nickname FROM prayers.Counts WHERE uuid = "'.$uuid2.'"', $link);
		$row = mysql_fetch_array($result, MYSQL_NUM);
		if ($row)
			$nick = $row[0];
		mysql_free_result($result);
		$escaped_nick = htmlentities($nick);
		*/
	}
}

if (intval($count) > 32000)
{
	$result = mysql_query('SELECT SUM(count) FROM prayers.Counts', $link)
		or die('mysql_query SELECT error'.mysql_error());
	$banned = true;
}
else
{
	$result = mysql_query('SELECT SUM(count) FROM prayers.Counts WHERE banned IS NULL', $link)
        	or die('mysql_query SELECT error'.mysql_error());
}
$row = mysql_fetch_array($result, MYSQL_NUM)
	or die('mysql_fetch_array error'.mysql_error());
$total = $row[0];
// Free the resources associated with the result set
// This is done automatically at the end of the script
mysql_free_result($result);

mysql_close($link);
unset($link);
