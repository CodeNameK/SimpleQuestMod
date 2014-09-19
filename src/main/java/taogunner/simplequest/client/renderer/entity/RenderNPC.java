package taogunner.simplequest.client.renderer.entity;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import taogunner.simplequest.SimpleQuestMod;
import taogunner.simplequest.entity.passive.EntityNPC;

public class RenderNPC extends RenderBiped
{
	private static final ResourceLocation[] npcSkin = new ResourceLocation[24];

	public RenderNPC(ModelBiped par1ModelBiped, float par2float)
	{
		super(par1ModelBiped, par2float);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity par1Entity)
	{
		return this.getEntityTexture((EntityNPC)par1Entity);
	}

    protected ResourceLocation getEntityTexture(EntityNPC par1EntityNPC)
    {
    	int skin = par1EntityNPC.getSkin();
    	npcSkin[skin] = new ResourceLocation(String.format(SimpleQuestMod.MODID + ":textures/entity/npc/npc_skin_%02d.png", new Object[] {skin}));
    	return npcSkin[skin];
    }
}
