package taogunner.simplequest.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import taogunner.simplequest.entity.passive.EntityNPC;
import taogunner.simplequest.entity.player.ExtendedPlayer;
import taogunner.simplequest.util.ServerUtils;
import taogunner.simplequest.util.json.JSONFullScript;
import taogunner.simplequest.util.json.JSONQuery;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ServerCommand implements ICommand
{
	@Override
	public int compareTo(Object arg0)
	{
		return 0;
	}

	@Override
	public String getCommandName() {
		return "sqm";
	}

	@Override
	public String getCommandUsage(ICommandSender par1sender)
	{
		return "/sqm <§6spawn§r,§6info§r,§6remove§r,§6refresh§r> <§6quest_id§r>";
	}

	@Override
	public List getCommandAliases()
	{
		return null;
	}

	@Override
	public void processCommand(ICommandSender par1sender, String[] par2String)
	{
		switch (par2String.length)
		{
			case 0:
			default:
				par1sender.addChatMessage(new ChatComponentText(this.getCommandUsage(par1sender)));
				break;
			case 1:
				if (par2String[0].equals("remove"))
				{
					EntityLiving entityLiving = ServerUtils.GetTargetEntityLiving(par1sender.getEntityWorld(), (EntityPlayer) par1sender, 3);
					if (entityLiving != null & entityLiving instanceof EntityNPC)
					{
						par1sender.addChatMessage(new ChatComponentText("NPC §6" + entityLiving.getCustomNameTag() + "§r был удалён"));
						entityLiving.setDead();
					}
				}
				if (par2String[0].equals("refresh"))
				{
					EntityLiving entityLiving = ServerUtils.GetTargetEntityLiving(par1sender.getEntityWorld(), (EntityPlayer) par1sender, 3);
					if ((entityLiving != null & entityLiving instanceof EntityNPC) && (par1sender instanceof EntityPlayer))
					{
						EntityNPC npc = (EntityNPC) entityLiving;
						EntityPlayer player = (EntityPlayer) par1sender;
						ExtendedPlayer.get(player).quest_position[npc.quest] = 0;
						ExtendedPlayer.get(player).quest_timestamp[npc.quest] = 0;
						par1sender.addChatMessage(new ChatComponentText("Квест §6" + npc.quest + "§r был обнулён"));
					}
				}
				break;
			case 2:
				if (par2String[0].equals("spawn") | par2String[0].equals("info"))
				{
					try
					{
						int quest_id = Integer.parseInt(par2String[1]);
						JSONFullScript json = new Gson().fromJson(JSONQuery.JSONReadFile(quest_id), JSONFullScript.class);
						if (par2String[0].equals("spawn")) par1sender.getEntityWorld().spawnEntityInWorld(new EntityNPC((EntityPlayer) par1sender, quest_id, json.npc.texture_id, json.npc.item_id, json.npc.name));
						if (par2String[0].equals("info")) { par1sender.addChatMessage(new ChatComponentText("Quest #" + quest_id + " : " + json.npc.description)); }
					}
					catch (NumberFormatException e) { par1sender.addChatMessage(new ChatComponentText(this.getCommandUsage(par1sender))); }
					catch (JsonSyntaxException e) { par1sender.addChatMessage(new ChatComponentText("File §6quest_" + Integer.parseInt(par2String[1]) + ".json§r is invalid!")); }
				}
				break;
		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender par1sender)
	{
		if (par1sender instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) par1sender;
			if (player.capabilities.isCreativeMode) return true;
			else return false;
		}
		else
		{
			par1sender.addChatMessage(new ChatComponentText("That command for Players ONLY!"));
			return false;
		}
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_)
	{
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_)
	{
		return false;
	}
}
