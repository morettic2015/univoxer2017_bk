<?php

include_once 'db_vars.config.php';

/**
 * 
 * MAIN BABEL JSON SERVICES
 * 
 * RESIZE IMAGE AND UPLOAD IT
 * 
 * @author Malacma <malacma@gmail.com>
 * 
 * @data 11/03/2014
 * 
 * */
function ak_img_resize($target, $newcopy, $w, $h, $ext) {
    list($w_orig, $h_orig) = getimagesize($target);
    $scale_ratio = $w_orig / $h_orig;
    if (($w / $h) > $scale_ratio) {
        $w = $h * $scale_ratio;
    } else {
        $h = $w / $scale_ratio;
    }
    $img = "";
    $ext = strtolower($ext);
    if ($ext == "gif") {
        $img = imagecreatefromgif($target);
    } else if ($ext == "png") {
        $img = imagecreatefrompng($target);
    } else {
        $img = imagecreatefromjpeg($target);
    }
    $tci = imagecreatetruecolor($w, $h);
    // imagecopyresampled(dst_img, src_img, dst_x, dst_y, src_x, src_y, dst_w, dst_h, src_w, src_h)
    imagecopyresampled($tci, $img, 0, 0, 0, 0, $w, $h, $w_orig, $h_orig);
    imagejpeg($tci, $newcopy, 80);
}

/**

 *  *//*
  $fileName = $_FILES["name_f"]["name"]; // The file name
  $fileTmpLoc = $_FILES["name_f"]["tmp_name"]; // File in the PHP tmp folder
  $fileType = $_FILES["name_f"]["type"]; // The type of file it is
  $fileSize = $_FILES["name_f"]["size"]; // File size in bytes
  $fileErrorMsg = $_FILES["name_f"]["error"]; // 0 for false... and 1 for true
  $kaboom = explode(".", $fileName); // Split file name into an array using the dot
  echo "<pre>";
  var_dump($_FILES["name_f"]);

  $file_path = "./avatars/";

  $file_path = $file_path . basename($_FILES['uploaded_file']['name']);

  echo $file_path;
  if (move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
  echo "success";
  } else {
  echo "fail";
  }
  $target_file = $file_path;
  $resized_file = "./avatars/resized_$fileName";
  $wmax = 120;
  $hmax = 120;
  //Redimensiona a imagem
  ak_img_resize($target_file, $resized_file, $wmax, $hmax, $fileExt);
  //Remove imagem original
  unlink($target_file); */
//echo $_GET['id'];
//var_dump($_FILES);
//$_UP['pasta'] = '/www/babel_json_services/libs/avatars/';
$std = new stdClass();
$_UP['pasta'] = './avatars/'; // Pasta onde o arquivo vai ser salvo
$_UP['tamanho'] = 1024 * 1024 * 2; // Tamanho m�ximo do arquivo (em Bytes) (2MB)
$_UP['extensoes'] = array('jpg', 'png', 'gif'); // Extens�es permitidas
$_UP['renomeia'] = false; // Renomeia o arquivo? (Se true, o arquivo ser� salvo como .jpg e um nome �nico)
// Array com os tipos de erros de upload do PHP
$_UP['erros'][0] = 'Não houve erro';
$_UP['erros'][1] = 'O arquivo no upload É maior do que o limite do PHP';
$_UP['erros'][2] = 'O arquivo ultrapassa o limite de tamanho especifiado no HTML';
$_UP['erros'][3] = 'O upload do arquivo foi feito parcialmente';
$_UP['erros'][4] = 'Não foi feito o upload do arquivo';

// Verifica se houve algum erro com o upload. Se sim, exibe a mensagem do erro
if ($_FILES['arquivo']['error'] != 0) {
    $std->message = "ERRO:<br />" . $_UP['erros'][$_FILES['arquivo']['error']];
    $std->code = CODE_500;
    echo json_encode($std);
    die();
}

// Caso script chegue a esse ponto, n�o houve erro com o upload e o PHP pode continuar
// Faz a verifica��o da extens�o do arquivo
$extensao = strtolower(end(explode('.', $_FILES['arquivo']['name'])));
if (array_search($extensao, $_UP['extensoes']) === false) {
    $std->message = EXTENSION_NOT_ALLOWED;
    $std->code = CODE_500;
    echo json_encode($std);
    die();
}

// Faz a verifica��o do tamanho do arquivo
else if ($_UP['tamanho'] < $_FILES['arquivo']['size']) {
    $std->message = TOO_LARGE_FILE;
    $std->code = CODE_500;
    echo json_encode($std);
    die();
}

if (empty($_GET['id'])) {
    $std->message = USER_NOT_FOUND;
    $std->code = CODE_500;
    echo json_encode($std);
    die();
}

// O arquivo passou em todas as verifica��es, hora de tentar mov�-lo para a pasta
else {
// Primeiro verifica se deve trocar o nome do arquivo

    $nome_final = time() . '.jpg';

// Depois verifica se � poss�vel mover o arquivo para a pasta escolhida
    if (move_uploaded_file($_FILES['arquivo']['tmp_name'], $_UP['pasta'] . $nome_final)) {
// Upload efetuado com sucesso, exibe uma mensagem e um link para o arquivo
        //$filename = basename();
        $f = split("/", $_SERVER['PHP_SELF']);
        //var_dump($f);
        $std->message = SUCESS;
        $std->code = CODE_200;
        $std->file_path = $nome_final;
        $std->url = 'http://' . $_SERVER['HTTP_HOST'] . "/" . $f[1] . "/avatars/" . $nome_final;

        /**
          -- insert into avatar set image_path = '123'

          -- select idavatar from avatar where image_path like '123'
          update profile set avatar_idavatar = 55 where id_user = 306;
         * 
         *          */
        $query = "insert into avatar set image_path = '$nome_final'";
        $row = getMysqlRows($query);

        $query = "select idavatar from avatar where image_path like '$nome_final'";
        $row = getMysqlRows($query);
        
        $query = "update profile set avatar_idavatar = " . $row['idavatar'] . " where id_user = " . $_GET['id'];
        $row = getMysqlRows($query);
        
        $std->image_id = $row['idavatar'];
    } else {
// N�o foi poss�vel fazer o upload, provavelmente a pasta est� incorreta
        $std->message = UPLOAD_ERROR;
        $std->code = 500;
    }
    echo json_encode($std);
    closeMysql();
}

 die();
?>
