<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$headerType = "HTML";
include_once '../libs/db_vars.config.php';

$email = $_GET['email'];
$query = "select id_user,email,name as total from profile where email like '$email'";
$result = mysqli_query($con, $query);
$row = mysqli_fetch_array($result);
//If nao encontra o usuario
if (is_null($row)) {
    echo "<script>alert('Email not found. Try again.');</script>";
} else {
    echo "<script>alert('Verify your email account! A new password where mailed.');</script>";
    //ID USUARIO E EMAIL PARA ENVIO
    $id = $row['id_user'];
    $email = $row['email'];
    $name = $row['name'];
    //Update password
    $password = generateRandomString();
    $query = "UPDATE `profile`SET `passwd` = '" . md5($password) . "' WHERE `id_user` = " . $row['id_user'];
    $result = mysqli_query($con, $query);
    $row = mysqli_fetch_array($result);
    //Mail the password
    $message = "$name:\nYour new password is:< $password > \n Keep it safe! Thank u!\n http://univoxer.com";
    // In case any of our lines are larger than 70 characters, we should use wordwrap()
    $message = wordwrap($message, 70);
    //Prepare message and headers
    $to = $email;
    $subject = 'UNIVOXER.COM - PASSWORD RECOVERY';
    $headers = 'From: equipeunivoxer@gmail.com' . "\r\n" .
            'Reply-To: equipeunivoxer@gmail.com' . "\r\n" .
            'X-Mailer: PHP/' . phpversion() . "UNIVOXER.MORETTIC.COM.br";

    mail($to, $subject, $message, $headers);
    //fecha conexão com o banco de dados
   

// Send
}
 mysqli_close($con);
function generateRandomString($length = 7) {
    $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
    $charactersLength = strlen($characters);
    $randomString = '';
    for ($i = 0; $i < $length; $i++) {
        $randomString .= $characters[rand(0, $charactersLength - 1)];
    }
    return $randomString;
}

?>
