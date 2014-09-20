package taogunner.simplequest.entity.player;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.IExtendedEntityProperties;
import taogunner.simplequest.common.CommonProxy;

public class ExtendedPlayer implements IExtendedEntityProperties
{
	private final static String EXT_PROP_NAME = "QuestMod";
	public int quest_position[];
	public int quest_timestamp[];

	public ExtendedPlayer(EntityPlayer player)
	{
		this.quest_position = new int[500];
		this.quest_timestamp = new int[500];
	}
	
	public static final void registerNBT(EntityPlayer player)
	{
		player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player));
	}
	
	public static final ExtendedPlayer get(EntityPlayer player)
	{
		return (ExtendedPlayer) player.getExtendedProperties(EXT_PROP_NAME);
	}
	
	public static final void saveProxyData(EntityPlayer player)
	{
		NBTTagCompound savedData = new NBTTagCompound();
		ExtendedPlayer.get(player).saveNBTData(savedData);
		CommonProxy.storeEntityData(getSaveKey(player), savedData);
	}

	public static final void loadProxyData(EntityPlayer player)
	{
		ExtendedPlayer playerData = ExtendedPlayer.get(player);
		NBTTagCompound savedData = CommonProxy.getEntityData(getSaveKey(player));
		if (savedData != null) { playerData.loadNBTData(savedData); }
	}
	
	private static final String getSaveKey(EntityPlayer player)
	{
		return player.getCommandSenderName() + ":" + EXT_PROP_NAME;
	}
	
	@Override
	public void saveNBTData(NBTTagCompound compound)
	{
		NBTTagCompound questNBT = new NBTTagCompound();
		questNBT.setIntArray("QuestSavePoint", this.quest_position);
		questNBT.setIntArray("QuestTimestamp", this.quest_timestamp);
		compound.setTag(EXT_PROP_NAME, questNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound compound)
	{
		NBTTagCompound questNBT = (NBTTagCompound) compound.getTag(EXT_PROP_NAME);
		this.quest_position = questNBT.getIntArray("QuestSavePoint");
		this.quest_timestamp = questNBT.getIntArray("QuestTimestamp");
	}

	@Override
	public void init(Entity entity, World world)
	{
	}
}