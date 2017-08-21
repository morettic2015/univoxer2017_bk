<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<?php
$headerType = "HTML";
include_once '../libs/db_vars.config.php';

$id = $_GET['id'];

$query = "SELECT * FROM `view_profile` where id_user = $id";
$result = mysqli_query($con, $query);
$row = mysqli_fetch_array($result);
//var_dump($row);
if ($row == NULL) {
    echo "<script>alert('Usuário ou senha não conferem.\\nTente novamente');</script>";
    include 'index.html';
} else {
    ?>
    <html xmlns="http://www.w3.org/1999/xhtml">
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
            <title>Painel</title>
            <link rel="stylesheet" href="csss.css"/>
            <link rel="stylesheet" href="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.css"/>
            <script src="http://code.jquery.com/jquery-1.11.3.min.js"></script>
            <script src="http://code.jquery.com/mobile/1.4.5/jquery.mobile-1.4.5.min.js"></script>
            <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.5.1/jsgrid.min.css" />
            <link type="text/css" rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.5.1/jsgrid-theme.min.css" />
            <script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/jsgrid/1.5.1/jsgrid.min.js"></script>
        </head>

        <body>
            <?php include './mnu_upper.php'; ?>
            <table id="rcorners1" width="60%" border="0" align="center">
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
              <!--  <tr>
                    <td  nowrap  align="right">
                        Estimated amount avaliable:
                    </td>
                    <td>
                        <?php echo $row['amount']; ?> R$
                    </td>
                </tr> -->
                <tr>
                    <td colspan="2">
                        <?php
                        $query = "select name, credits, payment_status,product_amount, payer_address, posted_date from view_paypal where id_user = $id;";
                        $result = mysqli_query($con, $query);
                        $vet = array();
                        while ($row = mysqli_fetch_array($result)) {
                            $std = new stdClass();
                            $std->name = $row['name'];
                            $std->product_amount = $row['product_amount'];
                            $std->payment_status = $row['payment_status'];
                            $std->payer_address = $row['payer_address'];
                            $std->posted_date = $row['posted_date'];

                            $vet[] = $std;
                        }
                        ?>
                        <div id="jsGrid"></div>
                        <script>
                            var clients = <?php echo json_encode($vet); ?>;

                            $("#jsGrid").jsGrid({
                                width: "100%",
                                height: "400px",
                                inserting: false,
                                editing: false,
                                sorting: true,
                                paging: true,
                                data: clients,
                                fields: [
                                    {name: "name", title: "Buyer", type: "text"},
                                    {name: "product_amount", title: "Amount", type: "number"},
                                    {name: "payment_status", title: "Status", type: "text"},
                                    {name: "payer_address", title: "Address", type: "text"},
                                    {name: "posted_date", type: "Date", title: "date"}
                                ]
                            });
                        </script>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <input type="button" id="submit1" value="Back" class="mbt" onclick="history.back()"/>
                    </td>
                </tr>
            </table>
            <?php include './footer.php'; ?>
        </body>
    </html>
    <?php
}

mysqli_close($con);
?>