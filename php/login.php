<?php
require_once 'connection.php';

$user = $_GET['username'];
$pass = $_GET['password'];

$query = "SELECT user_id FROM users WHERE username='$user' AND password='$pass'";
$result = mysqli_query($con, $query);
$nbrows = mysqli_num_rows($result);
if ($nbrows == 1) {
    session_start();
    $_SESSION['isloggedin'] = 1;
    $_SESSION['username'] = $user;
    $userV = $_SESSION['username'];
    $row = $result->fetch_assoc();
    $userid = $row['user_id'];
    echo $userid;
} else {
    echo "-1";
}
