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

//Recupera parametros

$id = getQVar('id_user');
$online = strtoupper(getQVar('online'));

$query = null;
//Set the profile id
$profile->id = $id;


//Default offline
if ($online == "BUSY") {
    $SQuery = "select fn_sipacc_by_id($id) as id_sip_acc";
    $result = queryMysql($SQuery);
    $profile->online = "BUSY";
    $profile->message = "ONGOING_CALL";
} else if ($online == "ON") {//ONLINE
    $profile->online = "ON";
    $profile->message = "CONNECTED";

    //Update status assign a sip account to the user
    $SQuery = "select fn_sipacc_by_id($id) as id_sip_acc";
    $row = getMysqlRows($SQuery);
    $idSipAcc = $row['id_sip_acc'];

    //SIP ACC INFO TO CONNECT
    $gSippDataQuery = "select user,pass,servername from sip_user as a,sip_server as b where idsip_user = $idSipAcc and a.sip_server_idsip_server = b.idsip_server; ";
    $row = getMysqlRows($gSippDataQuery);
    $profile->user = $row['user'];
    $profile->pass = $row['pass'];
    $profile->servername = $row['servername'];
} else {//OFFLINE
    $query = " SELECT fn_offline($id) AS fn_status";
    $row = getMysqlRows($query);

    $profile->online = "OFF";
    $profile->message = "DISCONNECTED";
    //Release account to the poll
    // $query1 = "UPDATE `sip_user` SET `profile_id_user`=null WHERE `profile_id_user` = $id";
    // $result = mysqli_query($con, $query1);
    // $row = mysqli_fetch_array($result);
}

//Release sip acc
if ($online == "EXIT") {
    $query = "UPDATE `sip_user` SET `profile_id_user`=null WHERE `profile_id_user` = $id";
    $profile->online = "EXIT";
    $profile->message = "EXIT";
    $result = queryMysql($query);
}

echo json_encode($profile);

mysqli_close($con);

die();
?>