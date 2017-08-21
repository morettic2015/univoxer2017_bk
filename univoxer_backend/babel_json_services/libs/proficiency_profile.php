<?php

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

include_once 'db_vars.config.php';

$id_user = getQVar('id_user');
$id_lange = getQVar('id_lang');
$opt = getQVar('opt');

$std = new stdClass();
$std->id_user = $id_user;
if ($opt == ADD) {
    //Insere a nova imagem no banco usando a funcao no mysql 
    $query = "insert into `proficiency` (profile_id_user,language_id_lang) values ($id_user,$id_lange)";
    //echo $query;
    $result = queryMysql($query);
    if ($result) {
        $std->code = CODE_200;
        $std->message = PROFICIENCY_ADDED;
    } else {
        $std->code = CODE_500;
        $std->message = PROFICIENCY_ADDED_ERROR;
    }
} else if ($opt == DEL) {
    $query = "delete from  `proficiency`  where  profile_id_user = $id_user and  language_id_lang = $id_lange";
    $result = queryMysql($query);
    if ($result) {
        $std->code = CODE_200;
        $std->message = PROFICIENCY_REMOVED;
    } else {
        $std->code = CODE_500;
        $std->message = PROFICIENCY_REMOVED_ERROR;
    }
} else {
    $query = "SELECT profile_id_user,id_lang, description, token FROM `proficiency` left join language on language_id_lang = id_lang where  profile_id_user = $id_user group by token";
    
   // echo $query;
    $result = queryMysql($query);
    
    //var_dump($result);
    
    $std->my_languages = array();
    
    while ($row = mysqli_fetch_array($result)) {
        $obj = new stdClass();
       // $obj->id_user = $row['profile_id_user'];
        $obj->id_lang = $row['id_lang'];
        $obj->description = $row['description'];
        $obj->token = $row['token'];
        
       // var_dump($obj);
        array_push($std->my_languages,$obj);
    }
}

echo json_encode($std);

mysql_close($con);
die();
?>