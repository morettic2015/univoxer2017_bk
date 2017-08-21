<?php

/**
 * libs
 */
include_once 'webservice.php';

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * DATABASE CONNECTION AND CONFIG HERE
 * 
 * @author Malacma <malacma@gmail.com>
 * 874u425
 * @data 11/03/2014
 * @refactor 13/08/2016
 * 
 * */
//parametros do banco
define('DB_NAME', 'nosnaldeia01');
define('DB_USER', 'nosnaldeia01');
define('DB_PASS', '666171');
define('DB_HOST', 'mysql.morettic.com.br');

//System constants
define('EMAIL_DOES_NOT_EXIST', 'EMAIL_DOES_NOT_EXIST');
define('PASSWORD_DONT_MATCH', 'PASSWORD_DONT_MATCH');
define('CONFERENCE', 'CONFERENCE');
define('TRANSLATOR_UNAVALIABLE', 'TRANSLATOR_UNAVALIABLE');
define('TRANSLATOR_FOUND', 'TRANSLATOR_FOUND');
define('NOT_REGISTERED', 'NOT_REGISTERED');
define("AUTHENTICATED", "AUTHENTICATED");
define('PUSHY', 'PUSHY');
define('ADD', 'ADD');
define('DEL', 'DEL');
define("CREATED", "CREATED");
define("USER_NOT_FOUND", "USER_NOT_FOUND");
define("TOO_LARGE_FILE", "TOO_LARGE_FILE");
define("EXTENSION_NOT_ALLOWED", "EXTENSION_NOT_ALLOWED");
define("SUCESS", "SUCESS");
define("UPDATED", "UPDATED");
define("UPLOAD_ERROR", "UPLOAD_ERROR");
define('PROFICIENCY_ADDED', 'PROFICIENCY_ADDED');
define('PROFICIENCY_REMOVED', 'PROFICIENCY_REMOVED');
define('PROFICIENCY_REMOVED_ERROR', 'PROFICIENCY_REMOVED_ERROR');
define('PROFICIENCY_ADDED_ERROR', 'PROFICIENCY_ADDED_ERROR');
define('TIME_AVALIABLE_UPDATED', 'TIME_AVALIABLE_UPDATED');
define('INCOMPLETE_CALL', 'INCOMPLETE_CALL');
define("INFO_FOUND", "INFO_FOUND");
define("INFO_NOT_FOUND", "INFO_NOT_FOUND");
define('CODE_500', 500);
define('CODE_200', 200);
define('CODE_404', 404);
define('CODE_400', 400);
////***********************************************************
//Inicialização 
//***********************************************************
//Parametros do sistema
$IMAGE_PATH = "http://www.nosnaldeia.com.br/babel_json_services/libs/avatars/";

//DEfault msg
$mensagem = "WARNING: MENSAGEM NAO INFORMADA !";
/* * *********************** */
//Conexao global
$con = getMysqlConnection();
/* * *************************** */

/**
  @Function that connects to the database
 * Funções DAO
 *  */

/**
    @Get Rows
 *  */
function getMysqlRows($query) {
    $result = queryMysql($query);
    if (is_null($result)) {
        return null;
    }
    return mysqli_fetch_array($result);
}
/**
 * @Query DB
 */
function queryMysql($query) {
    // Connect to the database
    $connection = getMysqlConnection();
    // Query the database
    $result = mysqli_query($connection, $query);
    //Return resultset
    return $result;
}

/**
  @CLOSE DB;
 *  */
function closeMysql() {
    $con = getMysqlConnection();
    mysqli_close($con);
}
/**
 * @Connect DB
 */
function getMysqlConnection() {
    // Define connection as a static variable, to avoid connecting more than once 
    static $connection;

    // Try and connect to the database, if a connection has not been established yet
    if (!isset($connection)) {
        //Connect to the database using constants
        $connection = mysqli_connect(DB_HOST, DB_USER, DB_PASS, DB_NAME);
        mysql_query("SET NAMES 'utf8'");
        mysql_query('SET character_set_connection=utf8');
        mysql_query('SET character_set_client=utf8');
        mysql_query('SET character_set_results=utf8');
    }

    // If connection was not successful, handle the error
    if ($connection === false) {
        // Handle error - notify administrator, log to a file, show an error screen, etc.
        if (mysqli_connect_errno()) {
            echo "Erro ao conectar ao mysql: " . mysqli_connect_error();
        }
        return mysqli_connect_error();
    }
    return $connection;
}

/**
  Global std object to create json data to response
 *  */
$profile = new stdClass();

//@todo usage????
function isActive() {
    $isEnabled = isset($_SESSION["BABELON"]) ? $_SESSION["BABELON"] : false;
}

//Geolocate
function visitor_country() {
    $ip = getRemoteIp();
    $ws = new WebService();
    return $ws->geGeoLocationInfoJSON($ip);
}

//Recover request remote ip address
function getRemoteIp() {
    $ip = $_SERVER["REMOTE_ADDR"];

    if (filter_var(@$_SERVER['HTTP_X_FORWARDED_FOR'], FILTER_VALIDATE_IP)) {
        $ip = $_SERVER['HTTP_X_FORWARDED_FOR'];
    }

    if (filter_var(@$_SERVER['HTTP_CLIENT_IP'], FILTER_VALIDATE_IP)) {
        $ip = $_SERVER['HTTP_CLIENT_IP'];
    }

    return $ip;
}

/* function getField($field) {
  return $_GET[$field] != null ? $_GET[$field] : $_POST[$field];
  } */

function getCotacaoMoeda() {
    $homepage = file_get_contents("http://api.promasters.net.br/cotacao/v1/valores");
    $std = json_decode($homepage);
    return $std;
}

/**
  GET COTAçÂO DOLAR
 *  */
function getCotacaoDolar() {
    $url = 'http://api.promasters.net.br/cotacao/v1/valores';
    $file = fopen($url, "r");
    $cotacao = "";
    if ($file) {
        while (($line = fgets($file)) !== false) {
            $cotacao.= $line;
        }
    }
    $std = getCotacaoMoeda();
    $cotacao_um_dolar_em_reais = $std->valores->USD->valor;
    fclose($file);
    return $cotacao_um_dolar_em_reais;
}

/**
 * @IOS @ANDROID PUSH CALL NOTIFICATION VOIP SERVICE
 * @author Moretto, Luis Augusto <malacma@egmail.com> 
 * COnsulta o dispositivo pelo ID
 * Se achou então manda um push e com o callback do webservice
 * Insere o log e retorna para o usuário
 *  */
function getPushIOCallInfo($user_id) {
    $mcon = getMysqlConnection();
    $query = "select device_type,push_token,push_id_user from push_notification  where push_id_user = $user_id";
    $row1 = getMysqlRows($query);
    $so = $row1['device_type'];
    $token = $row1['push_token'];

    if (is_null($row1)) {
        return null;
    } else {
        $url = "http://www.univoxer.com:8080/push_service/push.io?so=$so&deviceId=" . urlencode($token) . "&userId=$user_id&action=CALL";
        $file = fopen($url, "r");
        $push = "";
        if ($file) {
            while (($line = fgets($file)) !== false) {
                $push.= $line;
            }
        }
        $json = json_decode(utf8_decode($push));
        return $json;
    }
}

?>
