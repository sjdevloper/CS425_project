<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Top 5 Comments in Groups (PHP)</title>

	<link rel="stylesheet" href="/static/css/bootstrap.min.css">
	<script src="/static/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container" role="main">
    <div class="jumbotron">
        <h1>Top 5 Comments in Groups (PHP)</h1>
    </div>
    <form action="group_list.php" method="get">
		Group ID: <input type="text" name="paramA">
		<input type="submit" value="Search">
	</form>    
<?php
$db = "(DESCRIPTION=(ADDRESS_LIST = (ADDRESS = (PROTOCOL = TCP)(HOST = fourier.cs.iit.edu)(PORT = 1521)))(CONNECT_DATA=(SID=orcl)))";
$conn = oci_connect($argv[1], $argv[2], $db);

if (!$conn)
{
   $m = oci_error();
   echo $argv[1], "<br>";
   echo $argv[3], "<br>";
   echo $m['message'], "<br>";
   exit;
}

$id = $argv[4];
if (($id != "paramA") && ($id != "paramB"))
{
	$sql_group = "SELECT * FROM InterestGroupsClubs WHERE id='$id'";
	$sql_comments = "SELECT comments.id, topic, message, author"
		. " FROM comments JOIN discussionForums"
		. " ON comments.forumID = discussionforums.id"
		. " WHERE groupID='$id' AND ROWNUM <=5"
		. " ORDER BY ID DESC";
	
	$q_group = oci_parse($conn, $sql_group);
	oci_execute($q_group);
	
	if (false !== ($group = oci_fetch_assoc($q_group)))
	{
		echo "<h2>" . $group['NAME'] . "</h2>";
		$q_comments = oci_parse($conn, $sql_comments);
		oci_execute($q_comments);
		while (false !== ($comment = oci_fetch_assoc($q_comments)))
		{
			echo "<p>" . $comment['TOPIC']
				. " - " . $comment['MESSAGE']
				. " by " . $comment['AUTHOR'];
		}
	}
	else
	{
		echo "<h2>Not found!</h2>";
	}
}
else
{
	$sql_group = "SELECT * FROM InterestGroupsClubs";
	
	$q_group = oci_parse($conn, $sql_group);
	oci_execute($q_group);
	
	while (false !== ($group = oci_fetch_assoc($q_group)))
	{
		$id = $group['ID'];
		
		$sql_comments = "SELECT comments.id, topic, message, author"
			. " FROM comments JOIN discussionForums"
			. " ON comments.forumID = discussionforums.id"
			. " WHERE groupID='$id' AND ROWNUM <=5"
			. " ORDER BY ID DESC";
		
		echo "<h2>" . $group['NAME'] . "/" . $group['ID']. "</h2>";
		$q_comments = oci_parse($conn, $sql_comments);
		oci_execute($q_comments);
		while (false !== ($comment = oci_fetch_assoc($q_comments)))
		{
			echo "<p>" . $comment['TOPIC']
				. " - " . $comment['MESSAGE']
				. " by " . $comment['AUTHOR'];
		}
	}
}

oci_close($conn);
?>
</div>
</body>
</html>