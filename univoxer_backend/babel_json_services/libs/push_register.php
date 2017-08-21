<?php

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * @author Malacma <malacma@gmail.com>
 * @copyright (c) 2016, Morettic.com.br
 * REGISTER PUSH NOTIFICATIONS ASSOCIATE WITH USER;
 * @data 2/08/2016
 *
 * */
include_once 'db_vars.config.php';

//Recupera parametros
$id = getQVar('id');
$token = getQVar('token');
$device = getQVar('so');

//Remove atual
$query = "delete from push_notification where push_id_user = $id";
$row = getMysqlRows($query);

//Atualiza atual
$query = "INSERT INTO `push_notification` (`push_token`,`push_id_user`,`device_type`) VALUES ('$token','$id','$device');";
$row = getMysqlRows($query);

$std = new stdClass();
$std->id = $id;
$std->token =  $token;
echo json_encode($std);

closeMysql();
die();
?>