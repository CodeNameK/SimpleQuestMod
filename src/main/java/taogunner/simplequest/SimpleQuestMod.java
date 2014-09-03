package taogunner.simplequest;

import taogunner.simplequest.common.CommonProxy;
import taogunner.simplequest.server.ServerCommand;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = SimpleQuestMod.MODID, version = SimpleQuestMod.VERSION)
public class SimpleQuestMod
{
	public static final String MODID = "SimpleQuest";
	public static final String VERSION = "24";
	
	@Instance(MODID)
	public static SimpleQuestMod instance;
	
	@SidedProxy(clientSide="taogunner.simplequest.client.ClientProxy", serverSide="taogunner.simplequest.common.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.registerEntity();
		proxy.registerRenderer();
		proxy.registerPackets();
	}
	
	@EventHandler
	public void serverLoad(FMLServerStartingEvent event)
	{
		event.registerServerCommand(new ServerCommand());
	}
}
