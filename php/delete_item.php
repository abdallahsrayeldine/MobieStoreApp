<?php
require_once 'connection.php';

$item_id = intval($_GET['item_id']);
$user_id = intval($_GET['user_id']);


if ($user_id == 0) {
    if ($item_id > 0) {

        $query = "DELETE FROM items WHERE item_id = '$item_id'";


        if (mysqli_query($con, $query)) {
            echo "Item with ID $item_id has been deleted successfully.";
        } else {
            echo "Error deleting item: " . mysqli_error($con);
        }
    } else {
        echo "Invalid item ID.";
    }
} else {
    echo "Denied: You do not have permission to delete items.";
}
