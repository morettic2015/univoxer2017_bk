<?php

session_start();
$headerType = "HTML";
include_once '../libs/db_vars.config.php';
//echo "<pre>";
//print_r($_SESSION);
$date = $today = date("Y_m_d");
//echo $date;
$id_mobile = $_GET['id'];
$token = $_GET['token'];
$query = "SELECT email FROM `profile` WHERE id_user = " . $id_mobile;
$result = mysqli_query($con, $query);
$row = mysqli_fetch_array($result);
$email = $row['email'];
//var_dump($row); 

$md5Key = $id_mobile . '_' . $date . '@';
//echo $md5Key;
//echo '<br>';
$md5Crypto = md5($md5Key);

//echo $md5Crypto;
//Se for nullo ou o token gerado for diferente do token calculado....
if (is_null($row) || $md5Crypto != $token) {
    echo "<center><h1>An error has occurred while trying to authenticate your account.</h1><h3> Please close this window and try again.</h3></center>";
    session_destroy();
    include_once './index.php';
} else {
    $_SESSION["facebook"] = true;
    $_SESSION['email'] = $email;

    $action = $_GET['action'];
    if ($action == "PAYPAL") {
        header("Location: ../paypall/index.php?id=" . $id_mobile);
    } else {
        header("Location: ./painel.php");
    }
}
mysqli_close($con);
die();
?>