package taogunner.simplequest.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import taogunner.simplequest.entity.player.ExtendedPlayer;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class CommonEventHandler
{
	/**
	* <p>Событие при создании Entity.</p>
	* <p>Если Entity - это игрок (<b>EntityPlayer</b>), то добавляем ему NBT-параметры;</p>
	* @author TaoGunner
	*/
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event)
	{
		if (event.entity instanceof EntityPlayer)
		{
			if (ExtendedPlayer.get((EntityPlayer) event.entity) == null) ExtendedPlayer.registerNBT((EntityPlayer) event.entity);
		}
	}

	/**
	* <p>Событие при появлении Entity в мире.</p>
	* <p>Если Entity - это игрок (<b>EntityPlayer</b>), то загружаем в него NBT-параметры;</p>
	* @author TaoGunner
	*/
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
		{
			ExtendedPlayer.loadProxyData((EntityPlayer) event.entity);
		}
	}

	/**
	* <p>Событие при смерти Entity.</p>
	* <p>Если Entity - это игрок (<b>EntityPlayer</b>), то сохраняем его NBT-параметры;</p>
	* @author TaoGunner
	*/
	@SubscribeEvent
	public void onLivingDeathEvent(LivingDeathEvent event)
	{
		if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
		{
			ExtendedPlayer.saveProxyData((EntityPlayer) event.entity);
		}
	}
}
