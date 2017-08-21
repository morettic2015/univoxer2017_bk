
<?php

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * CHANGE PASSWORD
 * 
 * @author Malacma <malacma@gmail.com>
 * 
 * @data 4/06/2014
 * 
 * 
  ;# 1 linha(s) afetadas.


  update profile set avatar_idavatar = (select idavatar from avatar where image_path = 'tttttt') where id_user = 1;# 1 linha(s) afetadas.

 * 
 * */
include_once 'db_vars.config.php';

$email = $_GET['email'];

//echo $email;

$newpasswd = "Un1_" . date("His") . rand(1234, 9632) . "_V0X";
$md5PassWd = md5($newpasswd);
//echo $md5PassWd;
$profile->newPass = $newpasswd;
$profile->email = $email;

//Atualiza senha 
$query = " update profile set passwd = '$md5PassWd' where email = '$email'";
$row = getMysqlRows($query);

//var_dump($row);
//Loga a operação
$ip = getRemoteIp();
$query = "INSERT INTO `log`(`ip`, `date`, `id_user`, `optype`) VALUES ('$ip',now(),(select id_user from profile where email = '$email') ,'password')";
$row = getMysqlRows($query);

//echo $query;
//Mensagem de email confirmando
//$title = "UNIVOXER - PASSWORD CHANGE ($md5PassWd)";

$message = 'Password change (UNIVOXER Coins)';
$message.= '\n';
$message.= 'New Password :' . $newpasswd;
$message.= '\n';
$message.= 'Date:' . date("d-m-y");
$message.= '\n';
$message.= 'Thank you. Enjoy it';
$message.= '\n';
$message.= 'If you arent the owner of this message please ignore it.';
//echo $query;
//mail(
//mail(, "BABEL2u Coins", $message.' '.$saida, "From: " . $paypalReturn->payer_email . "\n");
$email_headers = implode("\n", array("From: univoxer@morettic.com.br",
    "Reply-To: univoxer@morettic.com.br",
    "Subject: UNIVOXER - PASSWORD CHANGE",
    "Return-Path:  univoxer@nosnaldeia.com.br",
    "MIME-Version: 1.0", "X-Priority: 3",
    "Content-Type: text/html; charset=UTF-8"));
//====================================================
//Enviando o email
//====================================================
try {
    if (mail($email, "UNIVOXER - PASSWORD CHANGE", nl2br($message), $email_headers)) {
        $profile->statusCode = 202;
    }
} catch (Exception $e) {
    $profile->statusCode = 500;
    //$profile->error = $e->getMessage();
}
//$profile->statusCode = 202;
$query = "SELECT id_user FROM view_profile WHERE email ='$email'";
$row = getMysqlRows($query);
//Recupera ID para carregar profile com info do email request
$id = $row['id_user'];
//Mensagem
$mensagem = "New mail sent to $email";
//Objeto default para imprimir o json
include 'json_profile.php';
?>
