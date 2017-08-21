<?php

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * @author Malacma <malacma@gmail.com>
 * 
 * LOGIN LOL
 * 
 * @data 11/03/2014
 * http://nosnaldeia.com.br/babel_json_services/?login=super@gmail.com&passwd=123456&proficiency=FR
 * */
include_once 'db_vars.config.php';

//Recupera parametros
$email = getQVar('login');
$passwd = getQVar('passwd');
//Usuário ou senha branco.....
if(empty($email)||empty($passwd)){
    $err = new stdClass();
    $err->code = 404;
    echo json_encode($err);
    die();
}

$mensagem = AUTHENTICATED;

//Atualiza o perfil para o status online e disponivel quando loga no sistema atualiza a lingua nativa tb
$ip = getRemoteIp();
$query = "SELECT token FROM language WHERE id_lang = ( SELECT nature FROM  `profile` WHERE email =  '$email' ) ";
$row = getMysqlRows($query);
//var_dump($row);
$token = $row['token'];

$query = " SELECT `fn_login`('$ip', '$email','$token') AS `fn_login`";
$row = getMysqlRows($query);
$nature = $row['fn_login'];

//Cria a session do ususario
if ($result) {
    session_start();
    $_SESSION["BABELON"] = true;
    $_SESSION["NATURE"] = $nature;
    $_SESSION["EMAIL"] = $email;
    $_SESSION["PASSWD"] = md5($passwd);
}
//
//Atualiza conta sip
$query = " select id_user from profile where email like '%$email%'";
$row = getMysqlRows($query);
$myId = $row['id_user'];

//Remove contas e atualiza sip user do pool
$query = "SELECT `fn_update_sip`($myId) AS `fn_update_sip`";
$row = getMysqlRows($query);

//Objeto default para imprimir o json
include 'json_profile.php';
//var_dump($profile);
?>