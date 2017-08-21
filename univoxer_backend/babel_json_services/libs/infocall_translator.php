<?php

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * CALL A TRANSLATOR AVALIABLE HERE
 * 
 * @author Malacma <malacma@gmail.com>
 * 
 * @data 11/03/2014
 * 
 * */
include_once 'db_vars.config.php';

$id = getQVar('id_sip');

/**
  Search the info from the user who called to show avatar and others
 *  */
$query = "SELECT * FROM view_info_call_profile as a1 
            where user like '%$id%'
            and id_call = ((select max(id_call) from `call` where from_c = a1.from_c) )";

//echo $query;
$row = getMysqlRows($query);

if (!empty($row['token'])) {
    $profile->token = $row['token'];
    $profile->id_call = $row['id_call'];
    $profile->name = $row['name'];
    $profile->user = $row['user'];
    $profile->pass = $row['pass'];
    $profile->servername = $row['servername'];
    $profile->image_path = $row['image_path'];
    $profile->nature = $row['from_id_lang'];
    $profile->proficiency = $row['to_id_lang'];
    $profile->message = INFO_FOUND;
} else {
    $profile->message = INFO_NOT_FOUND;
    $profile->user = $id;
}

echo json_encode($profile);

closeMysql();

die();
?>