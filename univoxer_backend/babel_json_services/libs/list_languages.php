<?php

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * @author Malacma <malacma@gmail.com>
 * 
 * User status: Online offline. Recover or release the sip account to the database pool;
 * 
 * @Created 11/03/2014
 * @Updated 9/04/2016
 * @Updated 0/07/2016
 * */
include_once 'db_vars.config.php';

$query = "SELECT * FROM language;";
//echo $query;
$result = queryMysql($query);

$vet = array();

while ($row = mysqli_fetch_array($result)) {
    $linha = new stdClass();
    $linha->token = $row['token'];
    $linha->icon = $row['flag_img'];
    $linha->id_lang = $row['id_lang'];
    $linha->description = $row['description'];
    $vet[] = $linha;
}

echo json_encode($vet);

closeMysql();
die();
?>