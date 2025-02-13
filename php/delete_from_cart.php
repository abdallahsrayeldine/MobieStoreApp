<?php
require_once 'connection.php';

$userid = intval($_GET['user_id']);
$product_name = $_GET['item_name'];
$quantity_to_remove = isset($_GET['quantity']) ? intval($_GET['quantity']) : 0;

$query = "SELECT cart FROM users WHERE user_id = '$userid'";
$result = mysqli_query($con, $query);

if ($result && mysqli_num_rows($result) > 0) {
    $row = mysqli_fetch_assoc($result);
    $cart = json_decode($row['cart'], true);

    if (!is_array($cart)) $cart = [];

    $item_found = false;
    foreach ($cart as $index => &$item) {
        if ($item['item_name'] === $product_name) {
            $item_found = true;

            if ($quantity_to_remove > 0) {
                $item['quantity'] -= $quantity_to_remove;

                if ($item['quantity'] <= 0) {
                    unset($cart[$index]);
                }
            } else {
                unset($cart[$index]);
            }
            break;
        }
    }

    if ($item_found) {
        $cart = array_values($cart);

        $updated_cart_json = json_encode($cart);

        $update_query = "UPDATE users SET cart = ? WHERE user_id = ?";
        $stmt = $con->prepare($update_query);
        $stmt->bind_param("si", $updated_cart_json, $userid);

        if ($stmt->execute()) {
            echo "success";
        } else {
            echo "Error updating cart: " . $stmt->error;
        }
    } else {
        echo "Item not found in the cart.";
    }
} else {
    echo "User not found or no cart data available.";
}
