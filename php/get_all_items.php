<?php
require_once 'connection.php';
$brand = $_GET['brand'];
$category = $_GET['category'];
$query = "SELECT * FROM items WHERE brand = '$brand' AND category='$category'";
$result = mysqli_query($con, $query);
$return_array = array();
while ($row = mysqli_fetch_assoc($result)) {
    $row_array['name'] = $row['item_name'];
    $row_array['category'] = $row['category'];
    $row_array['brand'] = $row['brand'];
    $row_array['price'] = $row['price'];
    array_push($return_array, $row_array);
}
echo json_encode($return_array);
