<?php
 
/*
 * Following code will create a new shirt row
 * All shirts details are read from HTTP Post Request
 */
 
// array for JSON response
$response = array();
 
// check for required fields

if (isset($_POST['description']) && isset($_POST['image_url']) && isset($_POST['price']) && isset($_POST['color']) && isset($_POST['qtystock'])) {
  
    $description = $_POST['description'];
    $image_url = $_POST['image_url'];
    $price = $_POST['price'];
    $color = $_POST['color'];
    $qtystock = $_POST['qtystock'];
 
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
 
    // mysql inserting a new row
    $result = mysql_query("INSERT INTO shirt (description, image_url, price, color, qtystock) VALUES('$description', '$image_url', '$price', '$color', '$qtystock')");
 
    // check if row inserted or not
    if ($result) {
        // successfully inserted into database
        $response["success"] = 1;
        $response["message"] = "Product successfully created.";
 
        // echoing JSON response
        echo json_encode($response);
    } else {
        // failed to insert row
        $response["success"] = 0;
        $response["message"] = "Oops! An error occurred.";
 
        // echoing JSON response
        echo json_encode($response);
    }
} else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";
 
    // echoing JSON response
    echo json_encode($response);
}
?>