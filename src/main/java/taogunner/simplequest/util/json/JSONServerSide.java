package taogunner.simplequest.util.json;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import taogunner.simplequest.entity.player.ExtendedPlayer;
import taogunner.simplequest.util.json.JSONFullScript.JFSDialog.JFSAction.JFSAItem;
import taogunner.simplequest.util.json.JSONFullScript.JFSDialog.JFSAction.JFSAParam;
import taogunner.simplequest.util.json.JSONFullScript.JFSDialog.JFSAction.JFSAQuest;
import taogunner.simplequest.util.json.JSONFullScript.JFSDialog.JFSAnswer;
import taogunner.simplequest.util.json.JSONFullScript.JFSDialog.JFSAnswer.JFSCondition.JFSCItem;
import taogunner.simplequest.util.json.JSONFullScript.JFSDialog.JFSAnswer.JFSCondition.JFSCParam;
import taogunner.simplequest.util.json.JSONFullScript.JFSDialog.JFSAnswer.JFSCondition.JFSCQuest;
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
		 * ===============================================
		 * ���� ���� �����-�� ��������, �� ������������ ��.
		 */
		if (JFS.dialogs[quest_pos].actions != null)
		{
			/**
			 * ����������\�������� ���������
			 */
			if (JFS.dialogs[quest_pos].actions.item != null)
			{
				for (JFSAItem item: JFS.dialogs[quest_pos].actions.item)
				{
					if (item.value > 0)
					{
						player.inventory.addItemStackToInventory(new ItemStack(Item.getItemById(item.item_id), item.value));
						player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "�������� " + item.value + " " + Item.getItemById(item.item_id).getUnlocalizedName().substring(5)));
					}
					if (item.value < 0)
					{
						for (int i=0; i>item.value; i--) { player.inventory.consumeInventoryItem(Item.getItemById(item.item_id)); }
						player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "������ " + Math.abs(item.value) + " " + Item.getItemById(item.item_id).getUnlocalizedName().substring(5)));
					}
				}
			}
			
			/**
			 * ����������\���������� ���������� ������
			 */
			if (JFS.dialogs[quest_pos].actions.param != null)
			{
				for (JFSAParam param: JFS.dialogs[quest_pos].actions.param)
				{
					/**
					 * 1 - ��������
					 * 2 - �����
					 * 3 - ����
					 */
					switch (param.param_id)
					{
						case 0:
						default: break;
						case 1: player.heal(param.value); break;
						case 2: player.getFoodStats().addStats(param.value, param.value); break;
						case 3:
							player.addExperience(param.value);
							if (param.value > 0) player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "�������� " + param.value + " ����� �����"));
							else player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "�������� " + param.value + " ����� �����"));
							break;
					}
				}
			}

			/**
			 * ��������� ��������� �������
			 */
			if (JFS.dialogs[quest_pos].actions.quest != null)
			{
				for (JFSAQuest quest: JFS.dialogs[quest_pos].actions.quest)
				{
					ExtendedPlayer.get(player).quest_position[quest.quest_id] = quest.value;
				}
			}

			/**
			 * ��������� ������������
			 */
			if (JFS.dialogs[quest_pos].actions.teleport != null)
			{
				player.setPositionAndUpdate(JFS.dialogs[quest_pos].actions.teleport.x, JFS.dialogs[quest_pos].actions.teleport.y, JFS.dialogs[quest_pos].actions.teleport.z);
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + "�� ���������������"));
			}

			/**
			 * ��������� ������ �������
			 */
			if (JFS.dialogs[quest_pos].actions.timestamp != null)
			{
				ExtendedPlayer.get(player).quest_timestamp[JFS.npc.quest_id] = (int)(System.currentTimeMillis() / 1000);
			}
			
			/**
			 * ��������� ��������� ��� ������
			 */
			if (JFS.dialogs[quest_pos].actions.message != null)
			{
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.ITALIC + JFS.dialogs[quest_pos].actions.message));
			}
		}
		
		/**
		 * ��������� ��������� �����
		 */
		this.npc = JFS.npc;
		this.text = JFS.dialogs[quest_pos].text;
		for ( JFSAnswer answer: JFS.dialogs[quest_pos].answers)
		{
			/**
			 * ==================================
			 * ��������� ������� ��������� ������
			 */
			if (answer.conditions != null)
			{
				boolean addAnswer = true;
				
				/**
				 * �������� ��������� �� ����������� ��������
				 */
				if (answer.conditions.item != null)
				{
					for (JFSCItem item: answer.conditions.item)
					{
						ItemStack itemStack = new ItemStack(Item.getItemById(item.item_id), item.value);
						if (!player.inventory.hasItemStack(itemStack)) { addAnswer = false; }
					}
				}

				/**
				 * �������� �� ��������� ������
				 */
				if (answer.conditions.param != null)
				{
					for (JFSCParam param: answer.conditions.param)
					{
						int check = 0;
						switch (param.param_id)
						{
							case 0:
							default: break;
							case 1: check = (int) player.getHealth(); break;
							case 2: check = player.getFoodStats().getFoodLevel(); break;
							case 3: check = (int) player.experienceTotal; break;
						}
						switch (param.logic)
						{
							default: break;
							case -1: if (check >= param.value) {addAnswer = false; }; break;
							case 0: if (check != param.value) {addAnswer = false; }; break;
							case 1: if (check <= param.value) {addAnswer = false; }; break;
						}
					}
				}

				/**
				 * �������� �� ��������� �������
				 */
				if (answer.conditions.quest != null)
				{
					for (JFSCQuest quest: answer.conditions.quest)
					{
						switch (quest.logic)
						{
							default: break;
							case -1: if (ExtendedPlayer.get(player).quest_position[quest.quest_id] >= quest.value) { addAnswer = false; }; break;
							case 0: if (ExtendedPlayer.get(player).quest_position[quest.quest_id] != quest.value) { addAnswer = false; }; break;
							case 1: if (ExtendedPlayer.get(player).quest_position[quest.quest_id] <= quest.value) { addAnswer = false; }; break;
						}
					}
				}

				/**
				 * �������� ������ �������
				 */
				if (answer.conditions.timestamp != null)
				{
					if ((ExtendedPlayer.get(player).quest_timestamp[npc.quest_id] >= (System.currentTimeMillis()/1000)) & (answer.conditions.timestamp.logic)) { addAnswer = false; }
					if ((ExtendedPlayer.get(player).quest_timestamp[npc.quest_id] < (System.currentTimeMillis()/1000)) & (!answer.conditions.timestamp.logic)) { addAnswer = false; }
				}
				if (addAnswer) { answers.add( new JSSAnswer(answer.text, answer.jump)); }
			}
			else { answers.add( new JSSAnswer(answer.text, answer.jump)); }
		}
	}
}