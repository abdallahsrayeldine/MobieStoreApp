<?php
require_once 'connection.php';
$user_id = $_GET['user_id'];
if ($user_id == "1") {
    $name = $_GET['item_name'];
    $price = intval($_GET['price']);
    $brand = $_GET['brand'];
    $category = $_GET['category'];
    $query = "INSERT INTO store.items(item_name, price, brand, category) VALUES('$name', '$price', '$brand', '$category')";

    if ($con->query($query)) {
        echo "1";
    } else {
        echo "Error";
    }
} else {
    echo "-1";
}

mysqli_close($con);
