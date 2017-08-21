<?php
header('Content-Type: application/json');
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * {
status: 1,
code: "88020-100",
state: "SC",
city: "Florianópolis",
district: "Centro",
address: "Rua General Bittencourt"
}
 * {"postal_code":"90002","city":"Los Angeles County","state":"California","country":"United States","bairro":""}
 */


$cep = $_GET['cep'];
$url = "http://apps.widenet.com.br/busca-cep/api/cep/".$cep.".json";
$retorno =  file_get_contents($url);
$std = json_decode($retorno);
$std->country = "BR";

if($std->status==0){
    $url = "http://smartapp.morettic.com.br/postalcode/?code=".$cep;
    
    //echo $url;
    $retorno =  file_get_contents($url);
    $json = json_decode($retorno);
    
   // var_dump($json);
    
    $std = new stdClass();
    $std->status = 1;
    $std->code = $json->postal_code;
    $std->city = $json->city;
    $std->state = $json->state;
    $std->country = $json->country;
    $std->district = empty($json->bairro)?"N/A":$json->bairro;
}

echo json_encode($std);
die();
?>