<?php
 
/*
 * Following code will update a product information
 * A product is identified by product id (pid)
 */
 
// array for JSON response
$response = array();
 
// check for required fields
if (isset($_POST['id']) && isset($_POST['description']) && isset($_POST['image_url']) && isset($_POST['price']) && isset($_POST['color']) && isset($_POST['qtystock'])) {
 
    $id = $_POST['id'];
    $description = $_POST['description'];
    $image_url = $_POST['image_url'];
    $price = $_POST['price'];
    $color = $_POST['color'];
    $qtystock = $_POST['qtystock'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql update row with matched pid
    $result = mysql_query("UPDATE shirt SET description = '$description', image_url = '$image_url', price = '$price', color = '$color', qtystock = '$qtystock' WHERE id_shirt = $id");
 
    // check if row inserted or not
    if ($result) {
        // successfully updated
        $response["success"] = 1;
        $response["message"] = "Shirt successfully updated.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
 
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>