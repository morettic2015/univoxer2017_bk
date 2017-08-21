<?php
session_start();
echo "<!-- Session destroyed-->";
unset($_SESSION);
session_destroy();

//Password recovery
if (isset($_GET['email'])) {
    include './mail.php';
}
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Documento sin t&iacute;tulo</title>
        <link href="https://cdnjs.cloudflare.com/ajax/libs/jqueryui/1.12.0/jquery-ui.min.css"/>
        <link href="http://necolas.github.io/css3-facebook-buttons/fb-buttons.css"/>
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css"/>
        <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>

        <script src='https://www.google.com/recaptcha/api.js'></script>
    </head>

    <body>
        <form action="painel.php" method="post" data-ajax="false">

            <table id="rcorners1" width="12%" border="0" align="center">
                <tr>
                    <td><div align="center">
                            <p><img src="http://www.univoxer.com/wp-content/uploads/2016/06/Marca01-1.png" width="200" height="189"/></p>
                            <p>
                                <h3>Welcome!</h3>
                                <a class="ui-btn ui-icon-lock ui-btn-icon-left" rel="external"  href="../facebook/">Facebook login</a>
                            </p>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="center">or</div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <div align="center">
                            <input type="text" name="p_email" value="email@mail.com" onfocus="this.value = ''"/>
                        </div> 
                    </td>
                </tr>
                <tr>
                    <td><div align="center">
                            <input type="password" name="p_passwd" onfocus="this.value = ''" />
                        </div>              
                    </td>
                </tr>
                <tr>
                    <td>
                        <div class="uibutton-toolbar" align="center">
                            <?php
                            require_once('recaptchalib.php');
                            $publickey = "6Lcs9iUTAAAAAMEILEgro23GhRy1YKPldJnDnGwN"; // you got this from the signup page
                            echo recaptcha_get_html($publickey);
                            ?>
                            <br>
                                <input class="uibutton large confirm ui-icon-lock" type="submit" value="Login" title="Login"></input>
                        </div> 
                    </td>
                </tr>
                <tr>
                    <td  colspan="2" align="center">
                        <a  href="#popupLogin" data-rel="popup" data-position-to="window" class="ui-btn ui-corner-all ui-shadow ui-btn-inline ui-icon-check ui-btn-icon-left ui-btn-a" data-transition="pop">Password recovery</a>
                        <div data-role="popup" id="popupLogin" data-theme="a" class="ui-corner-all" align="center">
                            <div style="padding:10px 20px;" align="center">
                                <h3>Inform your email!</h3>
                                <label for="un" class="ui-hidden-accessible">Email:</label>
                                <input type="text" name="user" id="txtEmailRest" value="" placeholder="mail@mail.com" data-theme="a"/>
                                <button type="button" id="btResetPasswd" class="ui-btn ui-corner-all ui-shadow ui-btn-b ui-btn-icon-left ui-icon-check">Reset password</button>
                            </div>

                        </div>
                    </td>
                </tr>
            </table>
        </form>
        <!--  <div style="position: fixed;  left: 50%;  bottom: 20px;  transform: translate(-50%, -50%);   margin: 0 auto;">
              <a href="http://morettic.com.br" target="_BLANK">
                  <img src="http://smartapp.morettic.com.br/resources/morettic_xxx.png" width="60" height="20"/>
              </a>
          </div> -->
        <?php include './footer.php'; ?>
        <script>
            document.getElementById("btResetPasswd").onclick = function () {
                if (confirm("Really reset your password?")) {
                    location.href = "index.php?email=" + document.getElementById("txtEmailRest").value;
                }
            }
        </script>
    </body>
</html>
