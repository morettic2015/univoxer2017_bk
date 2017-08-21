<?php
session_start();
$headerType = "HTML";
include_once '../libs/db_vars.config.php';
//echo "<pre>";
//print_r($_SESSION);
//echo 'Session path "' . session_save_path() . '" is not writable for PHP!';

if (isset($_POST["p_email"])) {

    //session_start();
    $email = $_POST["p_email"];
    $passwd = $_POST["p_passwd"];
    $_SESSION["p_email"] = $email;
    $_SESSION["p_passwd"] = $passwd;
    $query = "SELECT * FROM `view_profile` WHERE email ='" . $email . "' AND passwd ='" . md5($passwd) . "'";
    require_once('recaptchalib.php');
    if ($_POST["recaptcha_response_field"]) {
        $resp = recaptcha_check_answer("6Lcs9iUTAAAAAAwMu0tYeFaGVfQ8N4Xj41g0h7Pf", $_SERVER["REMOTE_ADDR"], $_POST["recaptcha_challenge_field"], $_POST["recaptcha_response_field"]);

        if ($resp->is_valid) {
            //echo "You got it!";
        } else {
            echo "<script>alert('Captcha incorreto.\\nTente novamente');</script>";
            include 'index.php';
            die();
        }
    } else {
        echo "<script>alert('Captcha inválido.\\nTente novamente');</script>";
        include 'index.php';
        die();
    }
    //echo "POST";
    //die();
} else if (isset($_SESSION["p_email"])) {
    $email = $_SESSION["p_email"];
    $passwd = $_SESSION["p_passwd"];
    $query = "SELECT * FROM `view_profile` WHERE email ='" . $email . "' AND passwd ='" . md5($passwd) . "'";

    //echo "SESSION";
    //die();
} else if (isset($_SESSION["facebook"])) {
    //var_dump($_SESSION);
    $email = $_SESSION['email'];
    $query = "SELECT * FROM `view_profile` WHERE email ='" . $email . "'";
    //echo $query;
    //echo "FACEBOOK";
    //die();
}

$result = mysqli_query($con, $query);
$row = mysqli_fetch_array($result);

if (is_null($row)) {
    echo "<script>alert('Acesso negado.\\nTente novamente');</script>";
    include 'index.php';
} else {
    ?>
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
            <title>Painel</title>
  
            <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css" />
            <script src="http://code.jquery.com/jquery-1.11.1.min.js"></script>
            <script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
        </head>

        <body>
            <?php include './mnu_upper.php'; ?>
            <table id="rcorners1" width="80%" border="0" align="center">
                <tr>
                    <td colspan="2">
                        <center>
                            <p><img src="http://www.univoxer.com/wp-content/uploads/2016/06/Marca01-1.png" width="200" height="189"/></p>
                        </center>
                    </td>
                </tr>
                <tr>
                    <td  align="right">
                        Welcome:
                    </td>
                    <td nowrap>
                        <?php echo $row['name']; ?>
                    </td>
                </tr>
                <tr>
                    <td  nowrap align="right">
                        Total time avaliable:
                    </td>
                    <td >
                        <?php echo $row['time_avaliable']; ?>
                    </td>
                </tr>
                <!-- <tr>
                    <td  nowrap  align="right">
                        Estimated amount avaliable:
                    </td>
                    <td>
                        <?php echo $row['amount']; ?> R$
                    </td>
                </tr> -->
                <tr>
                    <td ><div align="center"><img src="img/pay_pay.png" /><a class="ui-btn ui-icon-shop ui-btn-icon-left" rel="external" href="../paypall/?id=<?php echo $row['id_user']; ?>"><br>Buy translation time!</a></div></td>
                    <td> <div align="center"><img src="img/profile.png" alt="asd" /><a class="ui-btn ui-icon-user ui-btn-icon-left" rel="external" href="profile.php?id=<?php echo $row['id_user']; ?>"><br>Edit profile!</a></div></td>
                </tr>
                <tr>
                    <td ><div align="center"><img src="img/call_call.png" alt="asd" /><a class="ui-btn ui-icon-phone ui-btn-icon-left" rel="external" href="call_history.php?id=<?php echo $row['id_user']; ?>"><br>Call history</a></div></td>
                    <td ><div align="center"><img src="img/transactions_history.png" alt="ad" /><a class="ui-btn ui-icon-bars ui-btn-icon-left" rel="external" href="buy_history.php?id=<?php echo $row['id_user']; ?>"><br>Buy History</a></div></td>
                </tr>
            </table>
        </body>
        <?php include './footer.php'; ?>
    </html>
    <?php
}
mysqli_close($con);
?>