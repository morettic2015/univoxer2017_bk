<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<?php
/**
  @author Luis Augusto Machado Moretto <malacma@gmail.com>
 *  @data 7/72016
 *
 *  */
$headerType = "HTML";
require_once("../libs/db_vars.config.php"); // include the library file
//REcopuera ID e IP para o log etc...
$id = $_GET['id'];
$ip = getRemoteIp();


//Log acess to
$query = "SELECT `fn_log`($ip, '$id','PAYPAL_PURCHASE') AS `fn_log`;";
$result = mysqli_query($con, $query);
/**
  @Recupera a cotação do dolar do dia para calcular o valor em reais para o usuário
 *  */

$std = getCotacaoMoeda();
$cotacao_um_dolar_em_reais = $std->valores->USD->valor;
$query = "select fn_cotacao_moeda('$cotacao_um_dolar_em_reais') as valor_hora_real";
$result = mysqli_query($con, $query);
$row = mysqli_fetch_array($result);
$valor_hora_real = $row['valor_hora_real'];
//Seleciona o usuario da view
$query = "select * from view_paypal where id_user = $id ORDER BY payer_address desc limit 1";
$result = mysqli_query($con, $query);
$row = mysqli_fetch_array($result);

?>
</pre>
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
        <title>Univoxer.com</title>
        <link rel="stylesheet" href="../painel/csss.css"/>  
        <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css"/>
        <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
        <script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
    </head>
    <script>
        function validateFields() {
            var msg = ""
            if (document.form1.payer_zip.value == "") {
                msg += 'Please inform your zip code!\n';
            }
            if (document.form1.payer_address.value == "") {
                msg += 'Please inform your address!\n';
            }
            if (document.form1.payer_city.value == "") {
                msg += 'Please inform your city!\n';
            }
            if (document.form1.payer_state.value == "") {
                msg += 'Please inform your state!\n';
            }
            if (document.form1.payer_country.value == "") {
                msg += 'Please inform your country!\n';
            }
            if (msg != "") {
                alert(msg);
                return false;
            }
            return true;

        }
    </script>
    <body style="background-size: 100% 100% ; background-repeat: no-repeat">
        <?php include '../painel/mnu_upper.php'; ?>
        <div  align="center">
            <form  data-ajax="false" action="paypal.php?id=<?php echo $id ?>" method="post" name="form1" onsubmit="return validateFields();"> <?php // remove sandbox=1 for live transactions                                                   ?>
                <input type="hidden" name="action" value="process" />
                <input type="hidden" name="cmd" value="_cart" /> <?php // use _cart for cart checkout                                                   ?>
                <input type="hidden" name="currency_code" value="BRL" />
                <input type="hidden" name="invoice" value="<?php echo date("His") . rand(1234, 9632); ?>" />
                <table id="rcorners1" class="as_wrapper1">
                    <tr>
                        <td colspan="2">
                            <center>
                                <p><img src="http://www.univoxer.com/wp-content/uploads/2016/06/Marca01-1.png" width="200" height="189"/></p>
                                <h3>Welcome:<b>
                                        <label><?php echo $row['name']; ?><br>Email:(<?php echo $row['email']; ?>)</label></h3>
                                </b>
                                <input type="hidden" name="payer_lname" value="<?php echo $row['name']; ?>" />
                                <input type="hidden" name="user_id" value="<?php echo $id; ?>" />
                                <input type="hidden" name="payer_fname" readonly value="<?php echo $row['name']; ?>" />
                                <input type="hidden" readonly name="payer_email" value="<?php echo $row['email']; ?>" />
                                <input type="hidden" name="product_id" value="1" />
                                <input type="hidden" name="product_name"  value="UNIVOXER_TRANSLATION_SERVICE" />
                                <input type="hidden" name="product_quantity" value="1" step="1"/>
                            </center>
                        </td>
                    </tr>
                    <tr>
                        <td  colspan="2">
                            <label><b>Translation time</b></label>
                        </td>
                    </tr>
                    <tr>
                        <td  align="right"><label>Total (hours)</label></td>
                        <td>
                            <select name="product_amount" class="ui-button"/>
                            <option value="1">1 REAL RAFA</option>
                            <?php
                            //echo  $item = $valor_hora_real * 1;
                            for ($i = 1; $i <= 20; $i++) {
                                $item = $valor_hora_real * $i;

                                $fig = (int) str_pad('1', '3', '0');
                                $item = (ceil($item * $fig) / $fig);

                                echo "<option value='" . $item . "'>";
                                echo $i . " horas - " . $item . " R$";
                                echo "</option>";
                            }
                            //echo "<input type='text' name='product_amount' value='$valor_hora_real'/>";
                            ?>
                           </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right"><label>Zip</label></td>
                        <td><input type="text" name="payer_zip" id="payer_zip" value="<?php echo $row['payer_zip']; ?>" class="ui-button"/></td>
                    </tr>
                    <tr>
                        <td  align="right"><label>Address</label></td>
                        <td><input type="text" name="payer_address" value="<?php echo $row['payer_address']; ?>" class="ui-button"/></td>
                    </tr>
                    <tr>
                        <td  align="right"><label>City</label></td>
                        <td><input type="text" name="payer_city" value="<?php
                            echo $row['payer_city'];
                            ;
                            ?>" class="ui-button"/></td>
                    </tr>
                    <tr>
                        <td  align="right"><label>State</label></td>
                        <td><input type="text" name="payer_state" value="<?php echo visitor_country() != "Unknown" ? visitor_country()->geoplugin_regionName : $row['payer_state']; ?>" class="ui-button"/></td>
                    </tr>     

                    <tr>
                        <td  align="right"><label>Country</label></td>
                        <td><input type="text" name="payer_country" value="<?php echo visitor_country() != "Unknown" ? visitor_country()->geoplugin_countryCode : $row['payer_city'] ?>" class="ui-button"/></td>
                    </tr> 
                    <tr>
                        <td colspan="2" align="center"><input type="submit" name="submit" value="Buy" class="mbt"/></td>
                    </tr>
                    <tr>
                        <td colspan="2">
                            <input type="button" id="submit1" value="Back" class="mbt" onclick="history.back()"/>
                        </td>
                    </tr>
                </table>
            </form>
            <script>
                document.getElementById("payer_zip").onblur = function () {
                    //alert();
                    var MyUrl = "http://www.morettic.com.br/babel_json_services/?action=BUSCACEP&cep=" + this.value;
                    $.ajax({
                        // url para o arquivo json.php 
                        url: MyUrl,
                        // dataType json
                        dataType: "json",
                        // função para de sucesso
                        success: function (data) {
                            document.form1.payer_address.value = data.address + "/" + data.district;
                            document.form1.payer_city.value = data.city;
                            document.form1.payer_state.value = data.state;
                            document.form1.payer_country.value = data.country;
                            //alert(data);
                        }
                    });
                }

            </script>
        </div>
    </body>
</html>
<?php
// CLOSE DATABASE CONNECTION;
mysql_close($con);
?>
