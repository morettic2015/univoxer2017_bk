<?php

/**
 * http://www.nosnaldeia.com.br/babel_json_services/?action=SAVE_PROFILE&id_user=69&email=super@mail.com&passwd=123456&name=USUARIO%20SUPER%201&birthday=2010-01-01&paypall_acc=zxcvbasdAAA&nature=PT&proficiency=PT&id_role=1&passwd=123456
 * MAIN BABEL JSON SERVICES
 * 
 * @author Malacma <malacma@gmail.com>
 * 
 * SAVES OR UPDATES A PROFILE
 * 
 * @data 11/03/2014
 * 
 * */
include 'db_vars.config.php';

//init vars
$id = getQVar('id_user');
$email = getQVar('email');
$name = getQVar('name');
$birth = getQVar('birthday');
$paypall = getQVar('paypall_acc');
$natureP = getQVar('nature');
$proficiencyP = getQVar('proficiency');
$avatar = getQVar('avatar');
$role = getQVar('id_role');
$passwd = getQVar('passwd');

//echo $role."PAPEL";
//Acha a pk da lingua de proficiencia
$query = "SELECT id_lang,token FROM language WHERE token =  '$proficiencyP'";
$row = getMysqlRows($query);
$proficiencyP = $row['id_lang'];

//echo $proficiencyP;
$query = "SELECT id_lang,token FROM language WHERE token =  '$natureP'";
$row = getMysqlRows($query);
$natureP = $row['id_lang'];

//echo $natureP;
$query = "select id_user,passwd, count(*) as total from profile where email = '$email'";
$row = getMysqlRows($query);

//Verificar se o email existe e 
//Se o email existe e o ID for inválido retorna mensagem de erro.
//var_dump($result);
if ($row['total'] == '1' & ($id == "-1" || $id == "" || empty($id))) {
    $profile->message = "EMAIL_ALREADY_REGISTERED";
    echo json_encode($profile);
    mysqli_close($con);
    die();
}

$mensagem = "";
$query = "";
$isNew = false;
//INSERT A NEW PROFILE
if ($id == "-1" || $id == "" || empty($id)) {

    $query = "INSERT INTO  profile (
                name ,
                email ,
                passwd ,
                online ,
                avaliable ,
                birthday ,
                paypall_acc ,
                credits ,
                fk_id_role ,
                nature ,
                proficiency ,
                avatar_idavatar,
                qualified
        )
        VALUES (
                '" . $name . "',  
                '" . $email . "',  
                '" . md5($passwd) . "',  
                '1',  
                '1',  
                '" . $birth . "',  
                '" . $paypall . "',  
                '0',  
                '" . $role . "',  
                '" . $natureP . "', 
                '" . $proficiencyP . "' ,  
                '1',
                '0')";
    $mensagem = CREATED;
    $isNew = true;

    // echo $query;
} else {//UPDATE A PROFILE @todo Update Passwd
    $query = " UPDATE  "
            . "     profile "
            . "SET  "
            . "     paypall_acc =  '$paypall' ,"
            . "     name =  '$name' ,"
            . "     nature =  '$natureP' ,"
            . "     fk_id_role =  '$role' ,"
            . "     proficiency =  '$proficiencyP',"
            . "     birthday = '$birth' "
            . " WHERE "
            . "id_user = " . $id;
    $mensagem = UPDATED;
}

//echo $query;
//Insere ou atualiza o usuario / tradutor
$result = queryMysql($query);
include 'json_profile.php';
?>
