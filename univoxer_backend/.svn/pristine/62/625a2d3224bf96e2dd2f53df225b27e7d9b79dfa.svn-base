<?php

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * CONTROL: CALL STATUS. 
 * 
 * @author Luis Augusto Machado Moretto <malacma@gmail.com>
 * @copyright (c) 2014, Morettic.com.br
 * @data 11/03/2014
 * @Update 8/08/2016
 * */
include_once 'db_vars.config.php';

//echo "<pre>";

$token = getQVar('token');
$status = getQVar('status');

//Atualiza status da chamada
$query = "UPDATE `call` SET `start_t` = now(), `end_t` = now(), `id_call_status` = $status WHERE `token` = '$token'";
//echo $query;
$row = getMysqlRows($query);
//Log messsage
$str = "{'CALL_LOG STATUS':'$status','TOKEN CALL':'$token',";
$ip = getRemoteIp();

//Recover user id from token
$query = "SELECT from_c,to_c FROM `call` where token = '".$token."'";
//echo $query;
$row2 = getMysqlRows($query);
//var_dump($row2);
$id = $row2['from_c'];
$to = $row2['to_c'];

//Se status 2 ou 3 deve alterar o status do tradutor e do cara que fez a chamada para receber novamente ligações
if ($status == "1" || $status == "2" || $status == "3") {
    $queryP = "UPDATE `profile` SET `online` = 1, `avaliable` = 1 WHERE `id_user` = $id or id_user = $to";
    $row2 = getMysqlRows($queryP);
    $str.= ",'ONLINE':'1','AVALIABLE':'1 $id/$to',)";
}
$str.= "}";

//Create response object
$std = new stdClass();
$std->token = $token;
$std->status = $status;
$std->user_id = $id;
$std->translator_id = $to;
$std->log = $str;

//Log actions
$query = "INSERT INTO `log` (`ip`, `date`, `id_user`,`optype`) VALUES ('$ip',now(),'" . $id . "','" . json_encode($std) . "')";
$row3 = getMysqlRows($queryP);

//output
echo json_encode($std);
closeMysql();
die();
?>
