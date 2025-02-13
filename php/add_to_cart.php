
<?php
require_once 'connection.php';

$name = $_GET['item_name'];
$price = intval($_GET['price']);
$brand = $_GET['brand'];
$category = $_GET['category'];
$quantity = intval($_GET['quantity']);
$userid = intval($_GET['user_id']);

$query = "SELECT cart FROM users WHERE user_id = '$userid'";
$result = mysqli_query($con, $query);

if ($result && $result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $cart = json_decode($row['cart'], true);

    if (!is_array($cart)) {
        $cart = [];
    }

    $item_found = false;
    foreach ($cart as &$item) {
        if ($item['item_name'] === $name) {
            $item['quantity'] += $quantity;
            $item_found = true;
            break;
        }
    }

    if (!$item_found) {
        $cart[] = [
            'item_name' => $name,
            'price' => $price,
            'brand' => $brand,
            'category' => $category,
            'quantity' => $quantity
        ];
    }

    $updated_cart = json_encode($cart);

    $update_query = "UPDATE users SET cart = ? WHERE user_id = ?";
    $update_stmt = $con->prepare($update_query);
    $update_stmt->bind_param("si", $updated_cart, $userid);
    $update_stmt->execute();

    echo "success";
} else {
    echo "User not found or cart data unavailable.";
}
