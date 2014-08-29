<?php

class JSONDialog
{
	public $text;
	public $answers = array();
}

Header("Content-Type: text/html;charset=UTF-8");

if($fh = fopen("quest_2.src","r"))
	{
	while (!feof($fh)) { $F1[] = fgets($fh,9999); }
	fclose($fh);
	}
$dialogs = array();
foreach ($F1 as $str)
{
	$str_mass = explode("\t",$str);
	$text = $str_mass[0];
	$action = $str_mass[1];
	$answer_num = array();
	for ($i=0;$i<=count($str_mass)-2;$i++)
	{
		if ($str_mass[$i+2] != "\r\n" & $str_mass[$i+2] != null & $str_mass[$i+2] != "") {array_push($answer_num, (int)$str_mass[$i+2]);}
	}
	if ($action != null) { array_push($dialogs, array('text'=>$text,'action'=>array('savepoint'=>(int)$action),'answer_num'=>$answer_num)); }
	else { array_push($dialogs, array('text'=>$text,'answer_num'=>$answer_num)); }
}

echo json_encode_cyr($dialogs);

function json_encode_cyr($str)
{
	$arr_replace_utf = array('\u0410', '\u0430','\u0411','\u0431','\u0412','\u0432','\u0413','\u0433','\u0414','\u0434','\u0415','\u0435','\u0401','\u0451','\u0416','\u0436','\u0417','\u0437','\u0417','\u0438','\u0419','\u0439','\u041a','\u043a','\u041b','\u043b','\u041c','\u043c','\u041d','\u043d','\u041e','\u043e','\u041f','\u043f','\u0420','\u0440','\u0421','\u0441','\u0422','\u0442','\u0423','\u0443','\u0424','\u0444','\u0425','\u0445','\u0426','\u0446','\u0427','\u0447','\u0428','\u0448','\u0429','\u0449','\u042a','\u044a','\u042b','\u044b','\u042c','\u044c','\u042d','\u044d','\u042e','\u044e','\u042f','\u044f');
	$arr_replace_cyr = array('А', 'а', 'Б', 'б', 'В', 'в', 'Г', 'г', 'Д', 'д', 'Е', 'е','Ё', 'ё', 'Ж','ж','З','з','И','и','Й','й','К','к','Л','л','М','м','Н','н','О','о','П','п','Р','р','С','с','Т','т','У','у','Ф','ф','Х','х','Ц','ц','Ч','ч','Ш','ш','Щ','щ','Ъ','ъ','Ы','ы','Ь','ь','Э','э','Ю','ю','Я','я');
	$str1 = json_encode($str);
	$str2 = str_replace($arr_replace_utf,$arr_replace_cyr,$str1);
	return $str2;
}
?>