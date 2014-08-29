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
	
	/**
	* <p>Конструктор Proxy для Forge.</p>
	* <p>В нем регистрируем обработчик общих событий (<b>CommonEventHandler</b>);</p>
	* @author TaoGunner
	*/
	public CommonProxy()
	{
		MinecraftForge.EVENT_BUS.register(new CommonEventHandler());
	}

	/**
	* <p>Регистрируем Entity.</p>
	* @author TaoGunner
	 */
	public void registerEntity()
	{
		EntityRegistry.registerModEntity(EntityNPC.class, "NPC", 1, SimpleQuestMod.instance, 64, 1, true);
	}

	/**
	 * <p>Регистрируем рендеры.</p>
	 * <p>В общем Proxy метод должен быть пустым; 
	 * Метод должен быть переопределен (<b>@Override</b>) в клиентском Proxy;</p>
	 * @author TaoGunner
	 */
	public void registerRenderer() {}
	
	/**
	 * <p>Регистрируем сетевые пакеты.</p>
	 * @author TaoGunner
	 */
	public void registerPackets()
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
