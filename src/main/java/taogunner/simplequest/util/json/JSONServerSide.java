package taogunner.simplequest.util.json;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import taogunner.simplequest.entity.player.ExtendedPlayer;
import taogunner.simplequest.util.json.JSONFullScript.JFSDialog.JFSAnswer;
import taogunner.simplequest.util.json.JSONFullScript.JFSNPC;

public class JSONServerSide
{
	public JFSNPC npc;
	public String text;
	public ArrayList<JSSAnswer> answers = new ArrayList<JSSAnswer>();
	
	public class JSSAnswer
	{
		public String text;
		public int jump;
		
		public JSSAnswer(String par1String, int par2int)
		{
			this.text = par1String;
			this.jump = par2int;
		}
	}
	
	public JSONServerSide(JSONFullScript JFS, EntityPlayer player, int quest_pos)
	{
		/**
		 * Здесь будет обработка событий
		 */
		if (JFS.dialogs[quest_pos].actions != null)
		{
			for (String action: JFS.dialogs[quest_pos].actions)
			{
				DoAction(action, player, JFS.npc.quest_id);
			}
		}
		
		/**
		 * Формируем диалог для отправки
		 */
		this.npc = JFS.npc;
		this.text = JFS.dialogs[quest_pos].text;
		for ( JFSAnswer answer: JFS.dialogs[quest_pos].answers)
		{
			boolean addAnswer = true;
			if (answer.conditions != null)
			{
				for (String condition: answer.conditions)
				{
					if (!CheckCondition(condition, player, JFS.npc.quest_id)) {addAnswer = false;}
				}
			}
			if (addAnswer) { answers.add( new JSSAnswer(answer.text, answer.jump)); }
		}
	}

	private boolean CheckCondition(String condition, EntityPlayer player, int quest_num)
	{
		String[] param = condition.split(":");
		/**
		 * Проверка на наличие предметов в инвентаре
		 * item:<item_id>:<item_count>
		 */
		if (param[0].equals("item") & param.length == 3)
		{
			int item_id = Integer.parseInt(param[1]);
			int item_count = Integer.parseInt(param[2]);
			ItemStack itemStack = new ItemStack(Item.getItemById(item_id), item_count);
			if (!player.inventory.hasItemStack(itemStack)) return false;
		}

		/**
		 * Проверка параметров персонажа
		 * param:<param_id>:<param_value>:<param_logic>
		 */
		if (param[0].equals("param") & param.length == 4)
		{
			int param_id = Integer.parseInt(param[1]);
			int param_value = Integer.parseInt(param[2]);
			int param_logic = Integer.parseInt(param[3]);
			
			int check = 0;
			switch (param_id)
			{
				case 0:
				default: break;
				case 1: check = (int) player.getHealth(); break;
				case 2: check = player.getFoodStats().getFoodLevel(); break;
				case 3: check = (int) player.experienceTotal; break;
			}
			switch (param_logic)
			{
				default: break;
				case -1: if (check >= param_value) { return false; }; break;
				case 0: if (check != param_value) { return false; }; break;
				case 1: if (check <= param_value) { return false; }; break;
			}
		}
		
		/**
		 * Проверка состояния квестов
		 * quest:<quest_id>:<quest_value>:<quest_logic>
		 */
		if (param[0].equals("quest") & param.length == 4)
		{
			int quest_id = Integer.parseInt(param[1]);
			int quest_value = Integer.parseInt(param[2]);
			int quest_logic = Integer.parseInt(param[3]);
			switch (quest_logic)
			{
				default: break;
				case -1: if (ExtendedPlayer.get(player).quest_position[quest_id] >= quest_value) { return false; }; break;
				case 0: if (ExtendedPlayer.get(player).quest_position[quest_id] != quest_value) { return false; }; break;
				case 1: if (ExtendedPlayer.get(player).quest_position[quest_id] <= quest_value) { return false; }; break;
			}
		}

		/**
		 * Проверка временной метки
		 * timestamp:<timestamp_logic>
		 */
		if (param[0].equals("timestamp") & param.length == 2)
		{
			int timestamp_logic = Integer.parseInt(param[1]);
			if ((System.currentTimeMillis()/1000) >= (ExtendedPlayer.get(player).quest_timestamp[npc.quest_id]) & (timestamp_logic < 0)) return false;
			if ((System.currentTimeMillis()/1000) < (ExtendedPlayer.get(player).quest_timestamp[npc.quest_id]) & (timestamp_logic > 0)) return false;
		}
		
		return true;
	}

	private void DoAction(String action, EntityPlayer player, int quest_num)
	{
		String[] param = action.split(":");
		/**
		 * Добавление\Удаление предметов из инвентаря
		 * item:<item_id>:<item_count>
		 */
		if (param[0].equals("item") & param.length == 3)
		{
			int item_id = Integer.parseInt(param[1]);
			int item_count = Integer.parseInt(param[2]);
			if (item_count > 0)
			{
				player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(item_id), item_count));
				player.addChatMessage(new ChatComponentText("§oПолучено §6" + item_count + " " + Item.getItemById(item_id).getUnlocalizedName().substring(5)));
			}
			if (item_count < 0)
			{
				for (int i=0; i>item_count; i--) { player.inventory.consumeInventoryItem(Item.getItemById(item_id)); }
				player.addChatMessage(new ChatComponentText("§oОтдано §6" + Math.abs(item_count) + " " + Item.getItemById(item_id).getUnlocalizedName().substring(5)));
			}
		}

		/**
		 * Добавление\снятие очков параметров персонажа
		 * param:<param_id>:<param_value>
		 */
		if (param[0].equals("param") & param.length == 3)
		{
			int param_id = Integer.parseInt(param[1]);
			int param_value = Integer.parseInt(param[2]);
			/**
			 * 1 - Здоровье
			 * 2 - Голод
			 * 3 - Опыт
			 */
			switch (param_id)
			{
				case 0:
				default: break;
				case 1: player.heal(param_value); break;
				case 2: player.getFoodStats().addStats(param_value, param_value); break;
				case 3:
					player.addExperience(param_value);
					if (param_value > 0) player.addChatMessage(new ChatComponentText("§oПолучено §6" + param_value + "§f очков опыта"));
					else player.addChatMessage(new ChatComponentText("§oПотеряно §6" + param_value + "§f очков опыта"));
					break;
			}
		}

		/**
		 * Изменение состояния квестов
		 * quest:<quest_id>:<quest_value>
		 */
		if (param[0].equals("quest") & param.length == 3)
		{
			int quest_id = Integer.parseInt(param[1]);
			int quest_value = Integer.parseInt(param[2]);
			ExtendedPlayer.get(player).quest_position[quest_id] = quest_value;
		}

		/**
		 * Телепортация персонажа
		 * teleport:<x>:<y>:<z>
		 */
		if (param[0].equals("teleport") & param.length == 4)
		{
			int posX = Integer.parseInt(param[1]);
			int posY = Integer.parseInt(param[2]);
			int posZ = Integer.parseInt(param[3]);
			player.setPositionAndUpdate(posX, posY, posZ);
			player.addChatMessage(new ChatComponentText("§oВы телепортированы"));
		}

		/**
		 * Выставление временной метки
		 * timestamp:<value_sec>
		 */
		if (param[0].equals("timestamp") & param.length == 2)
		{
			int time_sec = Integer.parseInt(param[1]);
			ExtendedPlayer.get(player).quest_timestamp[quest_num] = (int)(System.currentTimeMillis() / 1000) + time_sec;
		}

		/**
		 * Вывод сообщения игроку
		 * message:<text>
		 */
		if (param[0].equals("message") & param.length == 2)
		{
			player.addChatMessage(new ChatComponentText("§o" + param[1]));
		}
	}
}
