package taogunner.simplequest.server;

import java.io.IOException;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import taogunner.simplequest.entity.passive.EntityNPC;
import taogunner.simplequest.util.json.JSONFullScript;
import taogunner.simplequest.util.json.JSONQuery;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ServerCommand  implements ICommand
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
		return "/sqm <spawn,info> <quest_id>";
	}

	@Override
	public List getCommandAliases()
	{
		return null;
	}

	@Override
	public void processCommand(ICommandSender par1sender, String[] par2String)
	{
		if (par2String.length == 2 & par1sender instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) par1sender;
			try
			{
				String command = par2String[0];
				int quest_id = Integer.parseInt(par2String[1]);
				JSONFullScript json = new Gson().fromJson(new JSONQuery().JSONReadFile(quest_id), JSONFullScript.class);
				if (command.equals("spawn")) { par1sender.getEntityWorld().spawnEntityInWorld(new EntityNPC(player, quest_id, json.npc.texture_id, json.npc.item_id, json.npc.name)); }
				if (command.equals("info")) { par1sender.addChatMessage(new ChatComponentText("Quest #" + quest_id + " : " + json.npc.description)); }
			}
			catch (JsonSyntaxException e) { par1sender.addChatMessage(new ChatComponentText("File quest_" + Integer.parseInt(par2String[1]) + ".json not found or corrupted!")); }
			catch (IOException e) { e.printStackTrace(); }
			catch (NumberFormatException e) { par1sender.addChatMessage(new ChatComponentText(this.getCommandUsage(par1sender))); }
		}
		else { par1sender.addChatMessage(new ChatComponentText(this.getCommandUsage(par1sender))); }
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
