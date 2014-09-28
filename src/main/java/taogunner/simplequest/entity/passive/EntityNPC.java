package taogunner.simplequest.entity.passive;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import taogunner.simplequest.SimpleQuestMod;
import taogunner.simplequest.util.ServerUtils;

public class EntityNPC extends EntityLiving
{
	private final static String EXT_PROP_NAME = SimpleQuestMod.MODID;
	public int quest = 0;

	public EntityNPC(World par1World)
	{
		super(par1World);
		this.setSize(0.6F, 1.8F);
		this.setCustomNameTag("Simple Quest Mod NPC");
		this.isImmuneToFire = true;
	}

	public EntityNPC(EntityPlayer par1EntityPlayer, int par2int, int par3int, int par4int, String par5String)
	{
		super(par1EntityPlayer.getEntityWorld());
		this.setPosition(par1EntityPlayer.posX, par1EntityPlayer.posY, par1EntityPlayer.posZ);
		this.quest = par2int;
		this.setSkin(par3int);
		if (par4int > 0) { this.setCurrentItemOrArmor(0, new ItemStack(Item.getItemById(par4int), 1)); }
		this.setCustomNameTag(par5String);
	}

	@Override
	protected void collideWithEntity(Entity p_82167_1_) { this.applyEntityCollision(this); }

	@Override
	public void applyEntityCollision(Entity p_70108_1_) { }

	@Override
	protected boolean canDespawn() { return false; }

	@Override
    public boolean isEntityInvulnerable() { return true; }

	@Override
	public boolean isInWater() { return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.6D, 0.0D), Material.water, this) && (this.isJumping = false); }

	@Override
    protected boolean interact(EntityPlayer player)
    {
		if (!this.worldObj.isRemote) { ServerUtils.getJSONQuest(player, this.getEntityId(), -1); }
		return false;
    }

	@Override
	public void readEntityFromNBT(NBTTagCompound nbt)
	{
		super.readEntityFromNBT(nbt);
	    NBTTagCompound questNBT = (NBTTagCompound) nbt.getTag(EXT_PROP_NAME);
	    this.quest = questNBT.getInteger("quest");
	    this.setSkin(questNBT.getInteger("texture"));
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound nbt)
	{
		super.writeEntityToNBT(nbt);
		NBTTagCompound questNBT = new NBTTagCompound();
		questNBT.setInteger("quest", this.quest);
		questNBT.setInteger("texture", this.getSkin());
		nbt.setTag(EXT_PROP_NAME, questNBT);
	}

	@Override
	protected void entityInit()
	{
    	super.entityInit();
        this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
	}

	public int getSkin() { return this.dataWatcher.getWatchableObjectByte(18); }

	public void setSkin(int skin_id) { this.dataWatcher.updateObject(18, Byte.valueOf((byte)skin_id)); }
}
