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
                <!-- <tr>
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
                        $query = "SELECT 
                                            DATE_FORMAT(start_t,'%d %b %Y %T') as start_time,
                                            DATE_FORMAT(end_t,'%d %b %Y %T') as end_time,  
                                            id_call, 
                                            l2.flag_img as f_img,
                                            l1.flag_img as t_img,
                                            upper(l1.description) as f_lang,
                                            upper(l2.description) as t_lang, 
                                            upper(p1.name) as name_user, 
                                            upper(p2.name) as name_translator, 
                                            TIMESTAMPDIFF(second,start_t,end_t) as tot 
                                        FROM `call` as c1
                                            left join profile as p1
                                            on p1.id_user = c1.from_c
                                            left join profile as p2
                                            on p2.id_user = c1.to_c
                                            left join language as l1
                                            on c1.to_id_lang = l1.id_lang
                                            left join language as l2
                                            on c1.from_id_lang = l2.id_lang
                                        where 
                                            (from_c = $id or to_c = $id)
                                        order 
                                            by start_t desc";
                        $result = mysqli_query($con, $query);
                        $vet = array();
                        while ($row = mysqli_fetch_array($result)) {
                            $std = new stdClass();
                            $std->start_time = $row['start_time'];
                            $std->end_time = $row['end_time'];
                            $std->id_call = $row['id_call'];
                            $std->f_lang = '<img src="./img/' . $row['f_img'] . '"/>';
                            $std->name_user = $row['name_user'];
                            $std->name_translator = $row['name_translator'];
                            $std->tot = $row['tot'];
                            $std->t_lang = '<img src="./img/' . $row['t_img'] . '"/>';
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
                                    {name: "start_time", title: "Start Time", type: "date"},
                                    {name: "end_time", title: "End Time", type: "date"},
                                    {name: "f_lang", title: "Native lang", type: "text"},
                                    {name: "t_lang", title: "Proficiency", type: "text"},
                                    {name: "tot", type: "number", title: "Seconds"},
                                    {name: "name_translator", type: "text", title: "Translator"}
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