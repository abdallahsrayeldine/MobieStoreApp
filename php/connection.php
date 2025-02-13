<?php
$server = "localhost";
$user = "root";
$password = "";
$db = "store";
$con = mysqli_connect($server, $user, $password, $db);
if (mysqli_connect_errno()) echo mysqli_connect_error();
