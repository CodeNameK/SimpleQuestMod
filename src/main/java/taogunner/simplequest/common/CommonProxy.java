package taogunner.simplequest.common;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import taogunner.simplequest.SimpleQuestMod;
import taogunner.simplequest.entity.passive.EntityNPC;
import taogunner.simplequest.network.Packet00GetJSONFromServer;
import taogunner.simplequest.network.Packet01SendJSONToClient;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.relauncher.Side;

public class CommonProxy
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(SimpleQuestMod.MODID.toLowerCase());
	private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();
	
	public CommonProxy()
	{
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
	}

	public void registerEntity()
	{
		EntityRegistry.registerModEntity(EntityNPC.class, "NPC", 1, SimpleQuestMod.instance, 64, 1, true);
	}

	public void registerRenderer() {}

	public void registerPacket()
	{
		INSTANCE.registerMessage(Packet00GetJSONFromServer.class, Packet00GetJSONFromServer.class, 0, Side.SERVER);
		INSTANCE.registerMessage(Packet01SendJSONToClient.class, Packet01SendJSONToClient.class, 1, Side.CLIENT);
	}

	public static void storeEntityData(String name, NBTTagCompound compound)
	{
		extendedEntityData.put(name, compound);
	}

	public static NBTTagCompound getEntityData(String name)
	{
		return extendedEntityData.remove(name);
	}
}
