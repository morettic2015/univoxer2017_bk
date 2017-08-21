<?php

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * @author Malacma <malacma@gmail.com>
 * 
 * FINISH A CALL AND UPDATES A CALL HISTORY. 
 * 
 * @TODO Also calculates Translator and CLient Credits!!!!!!
 * 
 * @data 11/03/2014
 * 
 * */
include_once 'db_vars.config.php';
//init vars
$token = getQVar('token');
$date = new DateTime();
$timestamp = $date->getTimestamp();
//$mensagem = "FINISH CONF";

//echo "<pre>";
//Finish time from call
$end_time = gmdate("Y-m-d H:i:s", $timestamp);
$query = "select a.*, b.total_seconds as my_time_avaliable from `call` as a, `profile` as b where token = '$token' and id_user = from_c";
$row = getMysqlRows($query);
$from_c = $row['from_c'];
$to_c = $row['to_c'];
$mySeconds = $row['my_time_avaliable'];
//var_dump($row);
/**
 * @Se o status for '4', a ligação foi completada. 
 * Atualiza os créditos e a data de finalização
 * */
if ($row['id_call_status'] == "4") {
    //se tiver o id do tradutor
    if (isset($_GET['id_t'])) {
        $query = "SELECT token FROM `call` WHERE `to_c` = " . $_GET['id_t']
                . " and start_t = end_t order by id_call desc limit 1";
        $row = getMysqlRows($query);
        $token = $row['token'];
    }

    //Update a call set finish time
    $query = "UPDATE  `call` SET  `end_t` = now() WHERE  token = '" . $token . "'";
    $row = getMysqlRows($query);

    /**
     * UPDATE USER AND TRANSLATOR CREDITS ON SYSTEM
     */
    $query = "SELECT * FROM `view_call_info` WHERE token = '" . $token . "'";
    $row = getMysqlRows($query);

    $id = $row['id_from'];
    $id_translator = $row['id_to'];
    $start_time = $row['start_t'];
    $credits_user = $row['credits_user'];
    $credits_translator = $row['credits_translator'];
    $mySeconds = $row['seconds'];

    //Get time difff
    $query = "SELECT * FROM view_call_time_diff where token = '$token'";
    //echo $query;
    $row = getMysqlRows($query);
    $total_seconds = $row['diff_time'];
    //echo $total_seconds;
    //Update time elapsed
    $mySeconds -= $total_seconds;
    
    //Recupera o valor de uma hora
    $query = "select cost from prices where hours = 1;";
    $row = getMysqlRows($query);

    //Recupera a cotação do dolar....
    $reais = getCotacaoDolar();
    $value_sec = (($row['cost'] * $reais) / 3600);

    //Creditos do usuário
    $spend_credits = $value_sec * $total_seconds;
    //$credits = ($spend_credits);
    $credits_user-=$spend_credits;
    $credits_translator+=$credits;

    //Udate translator status
    $query = "update profile set online=1,avaliable=1 where id_user=" . $id_translator;
    $row = getMysqlRows($query);

    //Update user credits, status and seconds;
    //$query = "update profile set online=1,avaliable=1,total_seconds='$total_seconds' where id_user=" . $id;
    $query = "update profile set online=1,avaliable=1,total_seconds=$mySeconds,credits=$credits_user where id_user=$id";
    $row = getMysqlRows($query);
    //*************************************
    //JSON DATA
    //*************************************
    $profile->estimated_call_value = $spend_credits;
    $profile->call_start = $start_time;
    $profile->call_finish = $end_time;
    $profile->call_token = $token;
    $profile->credits = $mySeconds;
    $profile->id_translator = $id_translator;
    $profile->id_translator = $to_c;
    $profile->id_user = $from_c;
    $profile->call_time_seconds = $total_seconds;
    $profile->message = TIME_AVALIABLE_UPDATED;
} else {
    $profile->call_start = $row['start_t'];
    $profile->call_finish = $row['end_t'];
    $profile->call_token = $row['token'];
    $profile->credits = $mySeconds;
    $profile->id_translator = $to_c;
    $profile->id_user = $from_c;
    $profile->call_time_seconds = 0;
    $profile->message = INCOMPLETE_CALL;

    //Udate translator status
    $query = "update profile set online=1,avaliable=1 where id_user  in($to_c,$from_c)";
    $row = getMysqlRows($query);
}
echo json_encode($profile);
$ip = getRemoteIp();
$query = "INSERT INTO `log`(ip,date,id_user,optype)  VALUES('" . $ip . "',now()," . $id . ",'{action:CALL_FINISH,token:" . $token . "}')";
$row = getMysqlRows($query);
closeMysql();
die();
?>
