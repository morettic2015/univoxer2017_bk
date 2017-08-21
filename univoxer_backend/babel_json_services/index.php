<?php
/**
 * ******************************************
 * MAIN BABEL JSON SERVICES
 * ******************************************
 * @author Malacma <malacma@gmail.com>
 * @url http://morettic.com.br - Tecnologia da Informação e Comunicação LTDA.
 * @copyright (c) Moretto, Luis Augusto Machado
 * @data 11/03/2014
 * @since 2014
 * @updated: 22/07/2016 
 * (Univoxer.com ME)
 * *******************************************
 * */
$action = getQVar("action");
/**
 * Ações do sistema
 */
switch ($action) {
    //@babel_json_services/?action=FINISH_PROFILE&token=36c7dcadb6d8b22dcf95a3227a5048ec
    case "FINISH_PROFILE":
        include './libs/finish_profile.php';
        break;
    //@babel_json_services/?action=SAVE_PROFILE&id_user=1&email=dao2ne@gmail.com&name=Alejandro%20Martines&birthday=2010-01-01&paypall_acc=12223kkjj12&nature=1&proficiency=2&id_role=1&passwd=123123
    case "SAVE_PROFILE":
        include './libs/save_profile.php';
        break;
    case "SAVE_PROFILEV":
        include './libs/save_profile.html';
        break;
    //@babel_json_services/?action=CALL_PROFILE&nature=2&proficiency=3&id_user=2&id_service_type=3
    case "CALL_PROFILE":
        include './libs/call_profile.php';
        break;
    //@babel_json_services/?action=CALL_PROFILE&nature=2&proficiency=3&id_user=2&id_service_type=3
    case "CALL_STATUS":
        include './libs/call_status.php';
        break;
    //@babel_json_services/?action=UPLOAD
    case "UPLOAD":
        include './libs/upload.config.php';
        break;
    //@babel_json_services/?action=AVATAR&id_user=2&iimage_path=123123123.png
    case "AVATAR_VIEW":
    case "AVATAR":
        include './libs/avatar_profile.php';
        break;
        //@babel_json_services/?action=AVATAR_VIEW&id_user=2
        include './libs/view_avatar_profile.php';
        break;
    case "PAYPALL_CALLBACK":
        //include './libs/paypall/index.php';
        break;
    case "PAYPAL_REQUEST":
        include './paypall/index.php';
        break;
  /*  case "NETWORK":
        include './libs/network_profile.php';
        break;*/
    //?action=EVALUATION&id_user=1&id_trans=139&rate=1.4
    case "EVALUATION":
        include './libs/evaluation_profile.php';
        break;
    //?action=STATUS&id_user=1&online=1
    case "STATUS":
        include './libs/status_profile.php';
        break;
    //?action=STATUS&id_user=1&online=1
    case "BUSCACEP":
        include './libs/json_address.php';
        break;
    //?action=STATUS&id_user=1&online=1
    case "ACKPASSWD":
        include './libs/email_profile.php';
        break;
    //?action=STATUS&id_user=1&online=1
    case "ACKPASSWDV":
        include './libs/email_profile.html';
        break;
    ///babel_json_services/?action=REQPAY&email=nono@mail.com
    case "REQPAY":
        include './paypall/paypal_request_pay.php';
        break;
    case "REQPAYV":
        include './paypall/paypal_request_pay.html';
        break;
    ///babel_json_services/?action=REQPAY&email=nono@mail.com
    case "SHOWPAY":
        include './paypall/paypal.html';
        break;
    case "SHOWCOINS":
        include './paypall/coins.html';
        break;
    case "MAPS":
        include './libs/social_maps.php';
        break;
    case "PROFICIENCY": //CARREGA SALVA E REMOVE PROFICIENCIA
        include './libs/proficiency_profile.php';
        break;
    case "LIST_LANGUAGES":
        include './libs/list_languages.php';
        break;
    case "INFOCALL_TRANSLATOR":
        include './libs/infocall_translator.php';
        break;
    case "MY_CREDITS":
        include './libs/my_credits.php';
        break;
    case "PUSH_REGISTER":
        include './libs/push_register.php';
        break;
    default:
        include './libs/login_profile.php';
        break;
}
/**
    @   recupera os parâmetros pelo metodo post get
 *  */
function getQVar($key) {
    return isset($_GET[$key]) ? $_GET[$key] : (isset($_POST[$key]) ? $_POST[$key] : null);
}
?>
    