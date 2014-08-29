package taogunner.simplequest.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import taogunner.simplequest.client.gui.GuiScreenNPCDialog;
import taogunner.simplequest.client.renderer.entity.RenderNPC;
import taogunner.simplequest.common.CommonProxy;
import taogunner.simplequest.entity.passive.EntityNPC;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	@Override
	public void registerRenderer()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityNPC.class, new RenderNPC(new ModelBiped(), 0.5F));
	}

	public static void openGUI(int par1int, String jsonString)
	{
		switch (par1int)
		{
		case 1:
		default:
			Minecraft.getMinecraft().displayGuiScreen(new GuiScreenNPCDialog(jsonString));
		}
	}
}
