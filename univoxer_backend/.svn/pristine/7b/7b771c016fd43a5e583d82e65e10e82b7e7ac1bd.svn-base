<?php

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * @author Malacma <malacma@gmail.com>
 * 
 * PRINTS JSON FOR ALL CALLBACK
 * 
 * @data 11/03/2014
 * 
 * */
$query = '';
$mySeconds = 0;
if (isset($id) && $id != "-1") {//modo edicao
    $query = "SELECT * FROM `view_profile` WHERE id_user ='$id'";
    $query1 = "update profile set online = true, avaliable = true WHERE id_user ='$id'";
    $row = getMysqlRows($query1);
} else {
    $query = "select passwd, count(*) as total from profile where email = '$email'";
    $row = getMysqlRows($query);
    //Verificar se o email existe e a senha nao confere....
    if ($row['total'] != '1') {
        $error = new stdClass();
        $error->code = CODE_404;
        $error->message = EMAIL_DOES_NOT_EXIST;
        echo json_encode($error);
        die();
    } else if ($row['passwd'] != md5($passwd)) {
        $error = new stdClass();
        $error->code = CODE_404;
        $error->message = PASSWORD_DONT_MATCH;
        echo json_encode($error);
        die();
    }
    //Recupera o usuario pelo email e senha
    $query = "SELECT * FROM `view_profile` WHERE email ='" . $email . "' AND passwd ='" . md5($passwd) . "'";
    //echo $query;
}

//Vincula a conta sip ao serviço
$querySipAcc = "SELECT `fn_sipacc`('$email', '" . md5($passwd) . "') AS `fn_sipacc`;";
//Executa a consulta 
$result = queryMysql($querySipAcc);
//var_dump($query);
//echo $query;
$row = getMysqlRows($query);
//$mySeconds = $row['seconds'];
//var_dump($row);
//JSON PROFILE ATTRS

$imageQuery = "select image_path from avatar where idavatar = (select avatar_idavatar from profile where id_user = " . $row['id_user'] . ")";
$rowImg = getMysqlRows($imageQuery);

$mySeconds = $row['seconds'];

//echo $mySeconds;
/**
    @Profile json callback
 *  */
$profile->image_path = is_null($rowImg) ? "null.png" : $rowImg['image_path'];
$profile->id_user = $row['id_user'] == null ? "-1" : $row['id_user'];
$profile->name = $row['name'] == null ? "" : mb_convert_encoding($row['name'], "UTF-8");
$profile->email = $row['email'] == null ? $email : $row['email'];
$profile->birthday = $row['birthday'] == null ? "" : $row['birthday'];
$profile->paypall_acc = $row['paypall_acc'] == null ? '' : $row['paypall_acc'];
$profile->credits = ($mySeconds);
$profile->nature = (isset($nature) ? $nature : ($row['nature'] == null ? "1" : $row['nature'])); //Lungua default portugues
$profile->proficiency = $row['proficiency'] == null ? "null" : $row['proficiency'];
$profile->serverName = $row['servername'] == null ? "ekiga.net" : $row['servername'];
$profile->user = $row['user'] == null ? "trandutoringles" : $row['user'];
$profile->pass = $row['pass'] == null ? "translate1" : $row['pass'];
$profile->role = $row['fk_id_role'] == null ? "1" : $row['fk_id_role'];
$profile->code = $profile->id_user == "-1" ? 404 : 200;
$profile->message = $profile->id_user == "-1" ? "FAIL" : $mensagem;

echo json_encode($profile);
//CLOSE DATABASE CONNECTION
closeMysql();
die();
?>