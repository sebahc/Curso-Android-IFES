<?php
 
/*
 * Following code will get single shirt details
 * A shirt is identified by shirt id (pid)
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once __DIR__ . '/db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();
 
// check for post data
if (isset($_GET["id"])) {
    $id = $_GET['id'];
 
    // get a shirt from shirts table
    $result = mysql_query("SELECT * FROM shirt WHERE id_shirt = $id");
 
    if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
 
            $row = mysql_fetch_array($result);
 
            $shirt = array();
            $shirt["id_shirt"] = $row["id_shirt"];
            $shirt["description"] = $row["description"];
            $shirt["image_url"] = $row["image_url"];
            $shirt["price"] = $row["price"];
            $shirt["color"] = $row["color"];
            $shirt["qtystock"] = $row["qtystock"];
            // success
            $response["success"] = 1;
 
            // user node
            $response["shirt"] = array();
 
            array_push($response["shirt"], $shirt);
 
            // echoing JSON response
            echo json_encode($response);
        } else {
            // no shirt found
            $response["success"] = 0;
            $response["message"] = "No shirt found";
 
            // echo no users JSON
            echo json_encode($response);
        }
    } else {
        // no shirt found
        $response["success"] = 0;
        $response["message"] = "No shirt found";
 
        // echo no users JSON
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