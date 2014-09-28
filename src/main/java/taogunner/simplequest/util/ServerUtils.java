package taogunner.simplequest.util;

import java.io.IOException;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import taogunner.simplequest.common.CommonProxy;
import taogunner.simplequest.entity.passive.EntityNPC;
import taogunner.simplequest.entity.player.ExtendedPlayer;
import taogunner.simplequest.network.Packet01SendJSONToClient;
import taogunner.simplequest.util.json.JSONFullScript;
import taogunner.simplequest.util.json.JSONQuery;
import taogunner.simplequest.util.json.JSONServerSide;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class ServerUtils
{
	public static EntityLiving GetTargetEntityLiving(World world, EntityPlayer player, int scanRadius)
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

	public static void getJSONQuest(EntityPlayer player, int npcID, int quest_pos)
	{
		if ((player.getEntityWorld().getEntityByID(npcID) instanceof EntityNPC) && (player.getEntityWorld().getEntityByID(npcID).getDistanceToEntity(player) < 5.0f))
		{
			JSONFullScript jsonfull = null;
			JSONServerSide jsonserver = null;
			EntityNPC npc = (EntityNPC)player.getEntityWorld().getEntityByID(npcID);
			try { jsonfull = new Gson().fromJson(JSONQuery.JSONReadFile(npc.quest), JSONFullScript.class); }
			catch (JsonSyntaxException e) { e.printStackTrace(); }
			if (quest_pos != -1)
			{
				String[] check = ExtendedPlayer.get(player).quest_current.split(":");
				for (int i=1; i<check.length; i++)
				{
					if ((Integer.parseInt(check[0]) == npcID) && (Integer.parseInt(check[i]) == quest_pos))
					{
						jsonserver = new JSONServerSide(jsonfull, player, quest_pos, npcID);
						CommonProxy.INSTANCE.sendTo(new Packet01SendJSONToClient(new Gson().toJson(jsonserver)), (EntityPlayerMP) player);
						return;
					}
				}
			}
			else
			{
				jsonserver = new JSONServerSide(jsonfull, player, ExtendedPlayer.get(player).quest_position[npc.quest], npcID);
				CommonProxy.INSTANCE.sendTo(new Packet01SendJSONToClient(new Gson().toJson(jsonserver)), (EntityPlayerMP) player);
			}
		}
	}
}
