<?php
require_once 'connection.php';

$user = $_GET['username'];
$pass = $_GET['password'];
$query = "SELECT user_id FROM users WHERE username='$user'";
$res = mysqli_query($con, $query);
if ($res && mysqli_num_rows($res) > 0) {
    echo "error";
} else {
    $query = "INSERT INTO users (username, password) VALUES ('$user', '$pass')";
    if (mysqli_query($con, $query)) {
        $userid = mysqli_insert_id($con);
        session_start();
        $_SESSION['isloggedin'] = 1;
        $_SESSION['username'] = $user;
        $userV = $_SESSION['username'];
        echo $userid;
    } else {
        echo "error";
    }
}
