<?php
 
/*
 * Following code will list all the shirts
 */
 
// array for JSON response
$response = array();
 
// include db connect class
require_once 'db_connect.php';
 
// connecting to db
$db = new DB_CONNECT();

// check for post data
if (isset($_GET["rnd"])) {
 
    // get all shirts from shirts table
    $result = mysql_query("SELECT * FROM shirt") or die(mysql_error());
     
    // check for empty result
    if (mysql_num_rows($result) > 0) {
        // looping through all results
        // shirts node
        $response["shirts"] = array();
     
        while ($row = mysql_fetch_array($result)) {
            // temp user array
            $shirt = array();
            $shirt["id_shirt"] = $row["id_shirt"];
            $shirt["description"] = $row["description"];
            $shirt["image_url"] = $row["image_url"];
            $shirt["price"] = $row["price"];
            $shirt["color"] = $row["color"];
            $shirt["qtystock"] = $row["qtystock"];
     
            // push single shirt into final response array
            array_push($response["shirts"], $shirt);
        }
        // success
        $response["success"] = 1;
     
        // echoing JSON response
        echo json_encode($response);
    } else {
        // no shirts found
        $response["success"] = 0;
        $response["message"] = "No shirts found";
     
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