<?php

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * CALL A TRANSLATOR AVALIABLE HERE
 * 
 * @author Luis Augusto Machado Moretto <malacma@gmail.com>
 * @copyright (c) 2014, Morettic.com.br
 * @data 11/03/2014
 * @Update 31/07/2016
 * */
include_once 'db_vars.config.php';

$id = getQVar('id_user');
$nature = getQVar('nature');
$proficiency = getQVar('proficiency');
$id_service_type = getQVar('id_service_type');

//Se nao for definido é do tipo padrão
$id_service_type = isset($id_service_type) ? $id_service_type : "1";

/**
  @View do banco de dados com a function que verifica se o usuário tem as duas linguas em uma determinada proficiencia
 * Ordenando pela data da última chamada de forma ASC ou seja o ultimo que atendeu primeiro a ser chamado
 *  */
$query = "  SELECT * 
            FROM view_filter_translators as a
            where ((nature = $nature and language_id_lang = $proficiency)
                or (nature = $proficiency and language_id_lang = $nature)
                or fn_has_proficiency(id_user, $nature,$proficiency))
                and `online` = '1'
                and avaliable = '1'
                and id_user <> $id                
            order by (select max(start_t) from `call` where to_c = id_user) ASC 
            limit 1";

//Filter translators  $nature $proficiency myself<>$id
$row = getMysqlRows($query);
//var_dump($row);die();
//Init values
$date = new DateTime();
$timestamp = $date->getTimestamp();
$token = md5(uniqid(rand(), true));
$mensagem = CONFERENCE;
$email = $row['email'];
$passwd = $row['passwd'];

//Set profile json object attrs

if (is_null($row['id_user'])) {

    $stdErro = new stdClass();
    $stdErro->code = CODE_404;
    $stdErro->message = TRANSLATOR_UNAVALIABLE;
    echo json_encode($stdErro);
    mysqli_close($con);
    die();//Finish here.....
}
$queryCreditos = "select total_seconds from profile where id_user = $id";
$row1 = getMysqlRows($queryCreditos);

$profile->id_translator = $row['id_user'];
$profile->translator_name = $row['name'];
$profile->sip_user_t = $row['user'];
$profile->sip_pass_t = $row['pass'];
$profile->credits = $row1['total_seconds'];
$profile->servername = $row['servername'];
$profile->message = TRANSLATOR_FOUND;
$profile->start_t = $date->getTimestamp();
$profile->call_token = $token;

//Make a push to the device.... IOS OR ANDROID
$callback = getPushIOCallInfo($profile->id_translator);
if (is_null($callback)) {//PUSH ERROR 
    $profile->push_info = NOT_REGISTERED;
    $profile->push_io = new stdClass();
} else {// PUSH SENT
    $profile->push_info = PUSHY;
    $profile->push_io = $callback;
}
/**
 * 
 *  SAVE CALL INFO WITH A UNIQUE TOKEN
 * 
 *  */
//if ($profile->sip_user_t != null) {
//Seta o usuario e tradutor como ocupados
$query = "SELECT `fn_set_unavaliable`('$id', '" . $profile->id_translator . "') AS `fn_set_busy`";
$row = getMysqlRows($query);

//Recupera os avatars
$query = "SELECT fn_get_avatar('" . $profile->id_translator . "') AS fn_get_avatar";
$row = getMysqlRows($query);
$profile->translator_avatar = $row['fn_get_avatar'];

$query = "SELECT fn_get_avatar('" . $id . "') AS fn_get_avatar";
$row = getMysqlRows($query);
$profile->user_avatar = $row['fn_get_avatar'];

$query = "INSERT INTO `call` ("
        . " `id_call`, "
        . " `from_id_lang`, "
        . " `to_id_lang`, "
        . " `start_t`, "
        . " `end_t`, "
        . " `from_c`, "
        . " `to_c`, "
        . " `service_type_idservice_type`, "
        . " `token`) "
        . "VALUES (NULL, '" . $nature . "','" . $proficiency . "','" . gmdate("Y-m-d H:i:s", $timestamp) . "', '" . gmdate("Y-m-d H:i:s", $timestamp) . "', '$id', '" . $profile->id_translator . "', '$id_service_type', '$token');";
//Insert call info
//echo $query;

$result = queryMysql($query);
$callbackR = json_encode($profile);
//Log action. Records. JSON INFO
$ip = getRemoteIp();
$query = "INSERT INTO `log`(ip,date,id_user,optype)  VALUES('" . $ip . "',now(),".$id.",'{action:CALL_PROFILE,token:".$token."}')";
$row = getMysqlRows($query);
//PRINT CALLBACK
echo $callbackR;
closeMysql();
die();
?>