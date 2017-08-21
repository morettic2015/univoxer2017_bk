<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<?php
$headerType = "HTML";
include_once '../libs/db_vars.config.php';
$id = $_GET['id'];
$query = "SELECT * FROM `view_profile` where id_user = $id";
$result = mysqli_query($con, $query);
$row = mysqli_fetch_array($result);
$idLang = $row['nature'];
//var_dump($row);
$role1 = $row['fk_id_role'] == "1" ? "checked" : "";
$role2 = $row['fk_id_role'] == "2" ? "checked" : "";
session_start();
if ($row == NULL) {
    echo "<script>alert('User or password dont match.\\nTry again');</script>";
    include 'index.html';
} else {
    ?>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
            <title>Painel</title>
            <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css"/>
            <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
            <script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
            <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.5.1/jsgrid.min.css" />
            <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.5.1/jsgrid-theme.min.css" />
            <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.5.1/jsgrid.min.js"></script>
            <script type="text/javascript" src="http://ajax.aspnetcdn.com/ajax/jquery.validate/1.7/jquery.validate.min.js"></script>
        </head>
        <script>
            function formatar(src, mask) {
                var i = src.value.length;
                var saida = mask.substring(i, i + 1);
                var ascii = event.keyCode;
                if (saida == "A") {
                    if ((ascii >= 97) && (ascii <= 122)) {
                        event.keyCode -= 32;
                    }
                    else {
                        event.keyCode = 0;
                    }
                } else if (saida == "0") {
                    if ((ascii >= 48) && (ascii <= 57)) {
                        return
                    }
                    else {
                        event.keyCode = 0
                    }
                } else if (saida == "#") {
                    return;
                } else {
                    src.value += saida;


                    if (saida == "A") {
                        if ((ascii >= 97) && (ascii <= 122)) {
                            event.keyCode -= 32;
                        }
                    } else {
                        return;
                    }
                }
            }
        </script>
        <body>
            <?php
            include './mnu_upper.php';
            ?>
            <form data-ajax="false"  action="profile.php?id=<?php echo $id; ?>" method="post" name="form1" id="form1"> 
                <input type="hidden" name="avatar_url" value="<?php echo $fName; ?>" />
                <center>
                    <table width="60%">
                        <tr>
                            <td colspan="2">
                                <center>
                                    <p><img src="http://www.univoxer.com/wp-content/uploads/2016/06/Marca01-1.png" width="200" height="189"/></p>
                                    <h3>Univoxer - Profile</h3>
                                    <?php
                                    if (isset($_POST['name'])) {
                                        $error = false;
                                        //var_dump($_POST);
                                        $messageError = "";

                                        if (empty($_POST['name'])) {
                                            $messageError = "*Name is required!\\n";
                                            $error = true;
                                        }
                                        $p1 = $_POST['passwda'];
                                        $p2 = $_POST['passwda1'];
                                        echo strcmp($p1, $p2);
                                        if (strcmp($p1, $p2) <> 0) {
                                            $messageError .= "*Password dont match...\\n";
                                            $error = true;
                                            // echo "....................$messageError";
                                        }
                                        if (strlen($_POST['passwda']) < 7) {
                                            $messageError .= "*Password too short!\\n";
                                            $error = true;
                                        }
                                        $query1a = "SELECT count(*) as tot FROM `view_profile` where email like '%" . $_POST['email1'] . "%' and email not like '%" . $row['email'] . "%'";
                                        $result1a = mysqli_query($con, $query1a);
                                        $row1a = mysqli_fetch_array($result1a);
                                        if ($row1a['tot'] != "0") {
                                            $messageError .= "*Email already exists!\\n";
                                            $error = true;
                                        }
                                        if (empty($_POST['birthday'])) {
                                            $messageError .= "*Birthday required!\\n";
                                            $error = true;
                                        }

                                        if (empty($_POST['nature'])) {
                                            $messageError .= "*Choose your language!";
                                            $error = true;
                                        }
                                        if ($_POST['role'] == "2" && empty($_POST['proficiency'])) {
                                            $messageError .= "*As a translator u should choose u proficiency!\\n";
                                            $error = true;
                                        }

                                        if (!$error) {
                                            $insert = "UPDATE `profile`
                                                        SET
                                                         `name` = '" . $_POST['name'] . "',
                                                         `email` = '" . $_POST['email1'] . "',
                                                         `passwd` = '" . md5($_POST['passwda']) . "',
                                                         `birthday` = '" . $_POST['birthday'] . "',
                                                         `fk_id_role` = " . $_POST['role'] . ",
                                                         `nature` = " . $_POST['nature'] . "
                                                        WHERE `id_user` = $id";

                                            //echo $insert;
                                            $result1aa = mysqli_query($con, $insert);
                                            //$row1aa = mysqli_fetch_array($result1aa);

                                            $del = "DELETE FROM `proficiency` WHERE profile_id_user = $id";
                                            $result123 = mysqli_query($con, $del);
                                            $row123 = mysqli_fetch_array($result123);
                                            //var_dump($result123);
                                            if ($_POST['role'] == "2") {
                                                $proficiency = $_POST['proficiency'];
                                                for ($i = 0; $i < count($proficiency); $i++) {
                                                    $id_lang = $proficiency[$i];
                                                    $query123 = "INSERT INTO `proficiency` (`profile_id_user`,`language_id_lang`) VALUES ($id, $id_lang);";
                                                    $result123 = mysqli_query($con, $query123);
                                                    $row123 = mysqli_fetch_array($result123);
                                                }
                                            }
                                            $query = "SELECT * FROM `view_profile` where id_user = $id";
                                            $result = mysqli_query($con, $query);
                                            $row = mysqli_fetch_array($result);
                                            $idLang = $row['nature'];
                                            $messageError = "Profile updated!";
                                        }
                                        echo "<script>alert('$messageError');</script>";
                                    }
                                    ?>
                                </center>    
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <h4>User Profile</h4>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="hidden" name="avatar" value="<?php echo "https://graph.facebook.com/$user/picture" ?>" />
                                <input type="hidden" name="id_user" value="-1" />
                            </td>
                        </tr>

                        <tr>
                            <td align="right"><label>Avatar</label></td>
                            <?php
                            $queryAvatar = "select `fn_get_avatar`($id) as img";
                            $resultAvatar = mysqli_query($con, $queryAvatar);
                            $rowAvatar = mysqli_fetch_array($resultAvatar);
                            ?>
                            <td><img src="http://www.morettic.com.br/babel_json_services/avatars/<?php echo $rowAvatar['img']; ?>"></td>
                        </tr>

                        <tr>
                            <td align="right"><label>Name:</label></td>
                            <td><input class="required"  type="text" name="name" value="<?php echo $row['name']; ?>" /></td>
                        </tr>
                        <tr>
                            <td align="right"><label>Email</label></td>
                            <td><input class="required"   type="email" name="email1" value="<?php echo $row['email']; ?>" /></td>
                        </tr>
                        <tr>
                            <td align="right"><label>Birthday</label></td>
                            <td><input type="date" maxlength="10" size="10"type="text" placeholder="yyyy-mm-dd" maxlength="10" name="birthday" id="birthday" data-inline="true" value="<?php echo date('Y-m-d', strtotime($row['birthday'])); ?>" /></td>
                        </tr>
                        <tr>
                            <td align="right"><label>Password</label></td>
                            <td><input class="required"   type="password" name="passwda" value="<?php echo $_SESSION["passwd"]; ?>"/></td>
                        </tr>
                        <tr>
                            <td align="right"><label>Password confirm</label></td>
                            <td><input  class="required"  type="password" name="passwda1" value="<?php echo $_SESSION["passwd"]; ?>" /></td>
                        </tr>

                        <tr>
                            <td colspan="2" align="center" nowrap>
                                <div style="width: 72%;float: right" align="right">

                                    <label>
                                        User  
                                        <input type="radio" name="role" value="1" <?php echo $role1; ?>/>  
                                    </label>                  
                                    <label>
                                        Translator  
                                        <input type="radio" name="role" value="2" <?php echo $role2; ?>/> 
                                    </label>  

                                </div>
                            </td>
                        </tr>
                        <tr>
                            <td  align="right">
                                My Language
                            </td>
                            <td>
                                <select class="required"   name="nature" id="nature">
                                    <?php
                                    $query = "select * from `language` order by description ASC";
                                    $result = mysqli_query($con, $query);
                                    while ($row = mysqli_fetch_array($result)) {
                                        $selected = ($row['id_lang'] == $idLang) ? "selected " : "";
                                        echo "<option value='" . $row['id_lang'] . "' $selected>" . $row['description'] . "</option>\n";
                                    }
                                    ?>
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
                                        $query = "select language_id_lang as id_lang from proficiency where profile_id_user = $id";
                                        $result = mysqli_query($con, $query);
                                        $prof = array();
                                        while ($row = mysqli_fetch_array($result)) {
                                            $id = $row['id_lang'];
                                            $prof[$id] = $id;
                                        }

                                        //var_dump($prof);

                                        $query = "select * from `language` order by description ASC";
                                        $result = mysqli_query($con, $query);
                                        while ($row = mysqli_fetch_array($result)) {
                                            $ida = $row['id_lang'];
                                            echo "<option value='" . $row['id_lang'] . "' ";
                                            if (!empty($prof[$ida])) {
                                                echo "selected ";
                                            }
                                            echo "'>" . $row['description'] . "</option>";
                                        }
                                        ?>
                                    </select>
                                </fieldset>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2" align="center">
                                <input type="submit" id="submit1" value="Save Profile"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <input type="button" id="submit1" value="Back" class="mbt" onclick="history.back()"/>
                            </td>
                        </tr>
                    </table>
                </center>
            </form>
            <?php include './footer.php'; ?>

        </body>
    </html>
    <?php
}
mysqli_close($con);
?>