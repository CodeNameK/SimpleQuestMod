<?php
Header("Content-Type: text/html;charset=UTF-8");

// Проверка входящего запроса
if (!isset($_GET['quest']) | !isset($_GET['pos'])) die('ERROR #1');
if (!is_numeric($_GET['quest']) | !is_numeric($_GET['pos'])) die('ERROR #2');
if (($_GET['quest'] > 1000) | ($_GET['pos'] > 200)) die('ERROR #3');

// Получаем содержимое квеста
$quest_file = file_get_contents('./quest_'.$_GET['quest'].'.json');
$quest = json_decode($quest_file, true);

$result = array();
$result['text'] = $quest["dialogs"][$_GET['pos']]["text"];

// ОТВЕТЫ
$quest_answers = array();
foreach ($quest["dialogs"][$_GET['pos']]["answer_nums"] as $answer)
{
	$quest_answer = array();
	$quest_answer['text'] = $quest["answers"][$answer]["text"];
	if (isset($quest["answers"][$answer]["jump"])) { $quest_answer['jump'] = $quest["answers"][$answer]["jump"]; }
	if (isset($quest["answers"][$answer]["condition"])) { $quest_answer['condition'] = $quest["answers"][$answer]["condition"]; }
	array_push($quest_answers,$quest_answer);
}
$result['answers'] = $quest_answers;

// ДЕЙСТВИЯ С ЛУТОМ
if (isset($quest["dialogs"][$_GET['pos']]["loot_action"]))
{
	$result['loot_action'] = $quest["loot_actions"][$quest["dialogs"][$_GET['pos']]["loot_action"]];
}

// СОХРАНЕНИЕ ПОЗИЦИИ ДИАЛОГА
if (isset($quest["dialogs"][$_GET['pos']]["dialog_savepoint"]))
{
	$result['dialog_savepoint'] = $quest["dialogs"][$_GET['pos']]["dialog_savepoint"];
}

echo json_encode_cyr($result);

function json_encode_cyr($str)
{
	$arr_replace_utf = array('\u0410', '\u0430','\u0411','\u0431','\u0412','\u0432','\u0413','\u0433','\u0414','\u0434','\u0415','\u0435','\u0401','\u0451','\u0416','\u0436','\u0417','\u0437','\u0417','\u0438','\u0419','\u0439','\u041a','\u043a','\u041b','\u043b','\u041c','\u043c','\u041d','\u043d','\u041e','\u043e','\u041f','\u043f','\u0420','\u0440','\u0421','\u0441','\u0422','\u0442','\u0423','\u0443','\u0424','\u0444','\u0425','\u0445','\u0426','\u0446','\u0427','\u0447','\u0428','\u0448','\u0429','\u0449','\u042a','\u044a','\u042b','\u044b','\u042c','\u044c','\u042d','\u044d','\u042e','\u044e','\u042f','\u044f');
	$arr_replace_cyr = array('А', 'а', 'Б', 'б', 'В', 'в', 'Г', 'г', 'Д', 'д', 'Е', 'е','Ё', 'ё', 'Ж','ж','З','з','И','и','Й','й','К','к','Л','л','М','м','Н','н','О','о','П','п','Р','р','С','с','Т','т','У','у','Ф','ф','Х','х','Ц','ц','Ч','ч','Ш','ш','Щ','щ','Ъ','ъ','Ы','ы','Ь','ь','Э','э','Ю','ю','Я','я');
	$str1 = json_encode($str);
	$str2 = str_replace($arr_replace_utf,$arr_replace_cyr,$str1);
	return $str2;
}
?>