package taogunner.simplequest.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import taogunner.simplequest.entity.passive.EntityNPC;
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
		return "/sqm <§6spawn§r,§6info§r> <§6quest_id§r>";
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
					EntityLiving entityLiving = GetTargetEntityLiving(par1sender.getEntityWorld(), (EntityPlayer) par1sender, 3);
					if (entityLiving != null & entityLiving instanceof EntityNPC)
					{
						par1sender.addChatMessage(new ChatComponentText("NPC §6" + entityLiving.getCustomNameTag() + "§r был удалён"));
						entityLiving.setDead();
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
					catch (FileNotFoundException e) { par1sender.addChatMessage(new ChatComponentText("File §6quest_" + Integer.parseInt(par2String[1]) + ".json§r not found or corrupted!")); }
					catch (IOException e) { e.printStackTrace(); }
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

	public EntityLiving GetTargetEntityLiving(World world, EntityPlayer player, int scanRadius)
    {
		double targetDistance = Math.pow(scanRadius,2);
		EntityLiving target = null;
		List lst = world.getEntitiesWithinAABBExcludingEntity(player, AxisAlignedBB.getBoundingBox(player.posX-scanRadius, player.posY-scanRadius, player.posZ-scanRadius, player.posX+scanRadius, player.posY+scanRadius, player.posZ+scanRadius));
		for (int i = 0; i < lst.size(); i ++)
		{
			Entity ent = (Entity) lst.get(i);
			if (ent instanceof EntityLiving && ent!=null && ent.boundingBox != null)
			{
				float distance = player.getDistanceToEntity(ent) + 0.1f;
				float angle = player.rotationYawHead;
				float pitch = player.rotationPitch;
                Vec3 look = player.getLookVec();
                Vec3 targetVec = Vec3.createVectorHelper(player.posX + look.xCoord * distance, player.getEyeHeight() + player.posY + look.yCoord * distance, player.posZ + look.zCoord * distance);

                if (ent.boundingBox.isVecInside(targetVec))
                {
                	if (distance < targetDistance && distance > 0)
                	{
                		targetDistance = distance;
                		target = (EntityLiving) ent;
                	}
                }
			}
        }
        return target;
    }
}
