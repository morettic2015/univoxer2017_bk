<?php
$headerType = "HTML";
include_once '../libs/db_vars.config.php';
/**
 * Copyright 2011 Facebook, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain
 * a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
require_once './facebook.php'; 
$FACE_APP_ID = "754601351216534";
$FACE_APP_TOKEN = "d9ed7cf6495fba5f78e09032740669f6";
// Create our Application instance (replace this with your appId and secret).
$facebook = new Facebook(array(
    'appId' => $FACE_APP_ID,
    'secret' => $FACE_APP_TOKEN,
        ));

// Get User ID
$user = $facebook->getUser();

// We may or may not have this data based on whether the user is logged in.
//
// If we have a $user id here, it means we know the user is logged into
// Facebook, but we don't know if the access token is valid. An access
// token is invalid if the user logged out of Facebook.

if ($user) {
    try {
        // Proceed knowing you have a logged in user who's authenticated.
        $user_profile = $facebook->api('me?fields=email,name,locale,picture,birthday');


        $query = "SELECT * FROM `view_profile` WHERE upper(email) = upper('" . $user_profile['email'] . "')";
        //echo $query;
        $result = mysqli_query($con, $query);
        $row = mysqli_fetch_array($result);
        // var_dump($row);
        if ($row != NULL) {
            session_start();
            $_SESSION['email'] = $user_profile['email'];
            $_SESSION['facebook'] = true;
            echo "Redirecionando...";
            echo "<script>this.location.href='../painel/painel.php'</script>";
            die();
        }

        //var_dump($user_profile);

        $friends = $facebook->api('/me/friends');

        //print_r($friends);
        //echo "<img src=\"https://graph.facebook.com/" . $user->id . "/picture\">";
    } catch (FacebookApiException $e) {
        error_log($e);
        $user = null;
    }
}

// Login or logout url will be needed depending on current user state.
if ($user) {
    $logoutUrl = $facebook->getLogoutUrl();
} else {
    $statusUrl = $facebook->getLoginStatusUrl();
    $loginUrl = $facebook->getLoginUrl();
}

// This call will always work since we are fetching public data.
//$naitik = $facebook->api('/naitik');



function get_file_name($copyurl) {
    return $copyurl . "jpg";
}
?>
<!doctype html>
<html xmlns:fb="http://www.facebook.com/2008/fbml">
    <head>
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css"/>
        <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
        <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.5.1/jsgrid.min.css" />
        <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.5.1/jsgrid-theme.min.css" />
        <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.5.1/jsgrid.min.js"></script>
        <link rel="stylesheet" href="../painel/csss.css"/>
        <title>UNIVOXER FACEBOOK</title>

    </head>
    <body >


        <?php if ($user): ?>

        <?php else: ?>
        <?php include '../painel/mnu_upper.php'; ?>
            <table id="rcorners1" width="12%" border="0" align="center">
                <tr>
                    <td><div align="center">
                            <p><img src="http://www.univoxer.com/wp-content/uploads/2016/06/Marca01-1.png" width="200" height="189"/></p>
                            <p>
                                <a class="ui-btn ui-icon-lock ui-btn-icon-left" rel="external" href="<?php echo $loginUrl; ?>">Login with Facebook</a>
                            </p>
                        </div>
                        <div>
                            <strong><em>You are not Connected.</em></strong>
                        </div>
                    </td>
                </tr>

            </table>

        <?php endif ?>




        <?php if ($user): ?>

            <?php
            //$json = json_encode($user_profile);
            // var_dump($json);
            //var_dump($user_profile);
            //copia a imagem do face pro meu diretorio
            //var_dump($user);
            $dir = '../avatars/';

            $iurm = "https://graph.facebook.com/" . $user . "/picture";
            //echo get_file_name($iurm);
            $fName = "resized_$user.jpg";

            copy($iurm, $dir . $fName);
            //var_dump($content);
            //Store in the filesystem.
            //echo $dir . get_file_name();
            ?>
            <a href="index.php"></a>
            <form data-ajax="false"  action="save_profile.php" method="post" name="form1"> 
                <input type="hidden" name="avatar_url" value="<?php echo $fName; ?>" />
                <center>
                    <table  id="rcorners1">
                        <tr>
                            <td colspan="2">
                        <center>
                            <p><img src="http://www.univoxer.com/wp-content/uploads/2016/06/Marca01-1.png" width="200" height="189"/></p>
                            <h3>Univoxer - facebook</h3>
                        </center>    
                        </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <h4>Register</h4>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="hidden" name="avatar" value="<?php echo "https://graph.facebook.com/$user/picture" ?>" />
                                <input type="hidden" name="id_user" value="-1" />
                            </td>
                        </tr>

                        <tr>
                            <td><label>Avatar</label></td>
                            <td><img src="<?php echo $iurm; ?>"></td>
                        </tr>

                        <tr>
                            <td align="right"><label>Name:</label></td>
                            <td><input class="ui-button" type="text" name="name" value="<?php echo strtoupper($user_profile['name']); ?>" class="mbt1"/></td>
                        </tr>
                        <tr>
                            <td align="right"><label>email</label></td>
                            <td><input class="ui-button"  type="text" name="email" value="<?php echo strtoupper($user_profile['email']); ?>" class="mbt1"/></td>
                        </tr>
                        <tr>
                            <td align="right"><label>Birthday</label></td>
                            <td><input class="ui-button" data-role="date" type="date" name="birthday" id="datepicker" value="<?php echo strtoupper($user_profile['birthday']); ?>" class="mbt1"/></td>
                        </tr>
                        <tr>
                            <td align="right"><label>Password</label></td>
                            <td><input class="ui-button"  type="password" name="passwd" value="<?php echo ""; ?>" class="mbt1"/></td>
                        </tr>
                        <tr>
                            <td align="right"><label>Password confirm</label></td>
                            <td><input  class="ui-button" type="password" name="passwd1" value="<?php echo ""; ?>" class="mbt1"/></td>
                        </tr>

                        <tr>
                            <td colspan="2" align="center" nowrap>
                                <input class="ui-button"  type="radio" name="role" value="1"> User
                                <input class="ui-button"  type="radio" name="role" value="2"> Translator
                            </td>
                        </tr>
                        <tr>
                            <td  align="right">
                                My Language
                            </td>
                            <td>
                                <select class="ui-button"  name="nature" id="nature">
                                    <?php
                                    $query = "select * from `language` order by description ASC";
                                    $result = mysqli_query($con, $query);
                                    while ($row = mysqli_fetch_array($result)) {
                                        echo "<option value='" . $row['id_lang'] . "'>" . $row['description'] . "</option>\n";
                                    }
                                    
                                    ?>
                                     <script> document.form1.nature.value = 1; </script>
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td  align="right">
                                My Proficiency
                            </td>
                            <td>
                                <fieldset>
                                    <label for="day">Select As Many Languages as your proficiency:</label>
                                    <select multiple="multiple" data-native-menu="false"  multiple="" size="5" name="proficiency[]" id="proficiency">
                                        <?php
                                        $query = "select * from `language` order by description ASC";
                                        $result = mysqli_query($con, $query);
                                        while ($row = mysqli_fetch_array($result)) {
                                            echo "<option value='" . $row['id_lang'] . "'>" . $row['description'] . "</option>";
                                        }
                                        ?>
                                    </select>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <input type="button" id="submit1" value="Register" class="mbt"/>
                            </td>
                        </tr>

                    </table>
                </center>
            </form>
        <?php else: ?>

        <?php endif ?>
        <script>
            document.getElementById("submit1").onclick = function () {
                //alert();
                if (!validateEmail(document.form1.email.value)) {
                    alert("Email invalid!");
                    return;
                }
                if (document.form1.email.value == "") {
                    alert("Email required!");
                    return;
                }
                if (document.form1.name.value == "") {
                    alert("Name required!");
                    return;
                }
                if (document.form1.birthday.value == "") {
                    alert("Birthday required!");
                    return;
                }
                if (document.form1.passwd.value != document.form1.passwd.value) {
                    alert("Password dont match!");
                    return;
                }
                if (document.form1.passwd.value.length < 7) {
                    alert("Password requires at least 7 characters!");
                    return;
                }

                var birth = document.getElementById('datepicker').value;
                var vBirth = birth.split("/");

                birth = vBirth[2] + "-" + vBirth[1] + "-" + vBirth[0];
                // http://www.morettic.com.br/babel_json_services/?action=SAVE_PROFILE&id_user=-1&email=MALACMA@GMAIL.COM&name=LAM%20MXRETTX&birthday=1979-29-04&paypall_acc=fcb10207525212627414&nature=&proficiency=BR&id_role=2
                // http://www.morettic.com.br/babel_json_services/?action=SAVE_PROFILE&id_user=-1&email=MALACMA@GMAIL.COM&name=LAM%20MXRETTX&birthday=1979-29-04&paypall_acc=fcb10207525212627414&nature=&proficiency=BR&id_role=2
                // http://www.morettic.com.br/babel_json_services/?action=SAVE_PROFILE&id_user=-1&email=nenene@gmail.com&name=Alejandro%20Martines&birthday=2010-01-01&paypall_acc=12223kkjj12&nature=EN&proficiency=EN&id_role=1&passwd=123123
                /**    var MyUrl = "http://www.morettic.com.br/babel_json_services/?action=SAVE_PROFILE&"
                 + "id_user=-1"
                 + "&email="
                 + document.form1.email.value.toUpperCase()
                 + "&name="
                 + document.form1.name.value.toUpperCase()
                 + "&birthday="
                 + birth
                 + "&paypall_acc="
                 + "fcb<?php echo $user; ?>"
                 + "&nature="+document.form1.nature.value;
                 + "&proficiency=BR"
                 + "&id_role="+document.form1.role.value;
                 + "&passwd="
                 + document.form1.passwd.value;
                 
                 $.ajax({
                 // url para o arquivo json.php 
                 url: MyUrl,
                 // dataType json
                 dataType: "json",
                 // função para de sucesso
                 success: function (data) {
                 alert(data.id_user);
                 if (data.id_user == "-1") {
                 alert("Already registered!")
                 } else {
                 document.form1.id_user.value = data.id_user;
                 alert("Welcome!")
                 saveAvatar()
                 //atualiza o usuario maldito com a foto do facebook
                 }
                 
                 }
                 });
                 }*/
                document.form1.submit();
            }

            function saveAvatar() {
                MyUrl = "http://www.morettic.com.br/babel_json_services/?action=AVATAR&id_user="
                        + document.form1.id_user.value
                        + "&image_path=<?php echo $user; ?>.jpg";
                $.ajax({
                    // url para o arquivo json.php 
                    url: MyUrl,
                    // dataType json
                    dataType: "json",
                    // função para de sucesso
                    success: function (data) {
                        alert(data);


                    }
                });
            }

            $(function () {
                $("#datepicker").datepicker();
            });
            function validateEmail(email) {
                var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            }
        </script>
    </body>
</html>
<?php
mysqli_close($con);
?>