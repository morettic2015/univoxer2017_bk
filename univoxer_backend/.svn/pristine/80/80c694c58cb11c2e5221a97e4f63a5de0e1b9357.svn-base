<?php

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * @author Malacma <malacma@gmail.com>
 * @copyright (c) 2016, Morettic.com.br
 * Credits in seconds by user id
 * @data 2/08/2016
 *
 * */
include_once 'db_vars.config.php';

//Recupera parametros
$id = getQVar('id');

$query = "select total_seconds from profile where id_user = $id;";
$row = getMysqlRows($query);

$std = new stdClass();
$std->id = $id;
$std->credits = $row['total_seconds'];

echo json_encode($std);

closeMysql();

die();
?>