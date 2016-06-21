<?php
	//$link = mysql_connect('localhost')
    $link = mysql_connect('us-cdbr-azure-west-c.cloudapp.net', 'b86206e3c5f51f', 'a700a05f')
		or die('mysql_connect error'.mysql_error());
    mysql_select_db('mondoxsAJRT4DALX', $link)
		or die('mysql_select_db error'.mysql_error());
