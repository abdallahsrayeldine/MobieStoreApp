<?php
require_once 'connection.php';

$userid = intval($_GET['user_id']);

$query = "SELECT cart FROM users WHERE user_id = '$userid'";
$result = mysqli_query($con, $query);

if ($result && mysqli_num_rows($result) > 0) {

    $row = mysqli_fetch_assoc($result);
    $result = json_decode($row['cart'], true);

    if (!is_array($result)) $result = [];
    $result = json_encode($result);
    $cart = json_decode($row['cart'], true);
    $total = 0;
    foreach ($cart as &$item) {
        $total += intval($item['price']) * intval($item['quantity']);
    }
    echo $result . "|" . $total;
} else {
    echo "User not found or no cart data available.";
}
