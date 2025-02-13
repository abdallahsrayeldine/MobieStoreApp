<?php
require_once 'connection.php';

$userid = intval($_GET['user_id']);

$query = "SELECT cart FROM users WHERE user_id = '$userid'";
$result = mysqli_query($con, $query);

if ($result && mysqli_num_rows($result) > 0) {
    $row = mysqli_fetch_assoc($result);
    $cart = json_decode($row['cart'], true);

    if (!is_array($cart)) $cart = [];
    $total = 0;
    foreach ($cart as &$item) {
        $total += intval($item['price']) * intval($item['quantity']);
    }

    $new_cart = [];
    $updated_cart_json = json_encode($new_cart);

    $update_query = "UPDATE users SET cart = '$updated_cart_json' WHERE user_id = '$userid'";
    if (mysqli_query($con, $update_query)) {
        echo "Checkout Successfull!";
    } else {
        echo "Error updating cart: " . mysqli_error($con);
    }
} else {
    echo "User not found or no cart data available.";
}
