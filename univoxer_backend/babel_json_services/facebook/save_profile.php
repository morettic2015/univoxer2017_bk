<?php

$headerType = "HTML";
include_once '../libs/db_vars.config.php';
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//echo "<pre>";
//var_dump($_POST);

$name = $_POST['name'];
$avatar_url = $_POST['avatar_url'];
$email = $_POST['email'];
$birthday = $_POST['birthday'];
$passwd = $_POST['passwd'];
$role = $_POST['role'];
$nature = $_POST['nature'];
$proficiency = $_POST['proficiency'];

$query = "SELECT id_user FROM `profile` where email = '$email'";
$result = mysqli_query($con, $query);
$row = mysqli_fetch_array($result);
if (!is_null($row)) {
    echo "Redirecionando...";
    echo "<script>alert('Email ja registrado');this.location.href='../painel/index.php'</script>";
    mysqli_close($con);
    die();
}

$insertAvata = "INSERT INTO `avatar` (`image_path`) VALUES ('$avatar_url')";
$result = mysqli_query($con, $insertAvata);
$row = mysqli_fetch_array($result);

$queryAvatar = "select idavatar from avatar where image_path = '$avatar_url'";
$result = mysqli_query($con, $queryAvatar);
$row = mysqli_fetch_array($result);
$id_avatar = $row['idavatar'];

$insertQuery = "INSERT INTO `profile`
                    (`name`,
                    `email`,
                    `passwd`,
                    `online`,
                    `birthday`,
                    `paypall_acc`,
                    `credits`,
                    `fk_id_role`,
                    `nature`,
                    `avatar_idavatar`,
                    `qualified`,
                    `avaliable`,
                    `proficiency`,
                    `total_seconds`)
                VALUES
                    ('$name',
                    '$email',
                    '" . md5($passwd) . "',
                    '0',
                    '$birthday',
                    '$email',
                    '0',
                    $role,
                    $nature,
                    $id_avatar,
                    0,
                    0,
                    $nature,
                    0);";

//echo $insertQuery;
$result = mysqli_query($con, $insertQuery);
$row = mysqli_fetch_array($result);

$query = "SELECT id_user FROM `profile` where email = '$email'";
$result = mysqli_query($con, $query);
$row = mysqli_fetch_array($result);
$id = $row['id_user'];
//echo $query;

for ($i = 0; $i < count($proficiency); $i++) {
    $id_lang = $proficiency[$i];
    $query = "INSERT INTO `proficiency` (`profile_id_user`,`language_id_lang`) VALUES ($id, $id_lang);";
    $result = mysqli_query($con, $query);
    $row = mysqli_fetch_array($result);
    //echo $query;
}


$query = " INSERT INTO `log`
            (`ip`,
            `date`,
            `id_user`,
            `optype`)
            VALUES
            ('" . getRemoteIp() . "',
            now(),
            $id,
            'NEW_FACEBOOK_USER($email)')";

$result = mysqli_query($con, $query);
$row = mysqli_fetch_array($result);

session_start();
$_SESSION['email'] = $email;
$_SESSION['facebook'] = true;
echo "Redirecionando...";
echo "<script>this.location.href='../painel/painel.php'</script>";
mysqli_close($con);
die();
?>

