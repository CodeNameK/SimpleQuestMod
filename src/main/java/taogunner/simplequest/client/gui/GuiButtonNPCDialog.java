package taogunner.simplequest.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiButtonNPCDialog extends GuiButton
{
	public int width;
	public int height;
	public int xPosition;
	public int yPosition;
	public int id;
	public boolean enabled;
	public boolean visible;
	public boolean hover;
	public String displayString;
	
	public GuiButtonNPCDialog(int par1int,int par2int,int par3int,int par4int,int par5int, String par6String)
	{
		super(par1int,par2int,par3int,par4int,par5int,par6String);
		this.enabled = true;
		this.visible = true;
		this.hover = false;
		this.id = par1int;
		this.width = par4int;
		this.height = par5int;
		this.xPosition = par2int;
		this.yPosition = par3int;
		this.displayString = par6String;
	}

	@Override
    public void drawButton(Minecraft mc, int par2int, int par3int)
    {
        if (this.visible)
        {
            FontRenderer fontrenderer = mc.fontRenderer;
            this.hover = par2int >= this.xPosition && par3int >= this.yPosition && par2int < this.xPosition + this.width && par3int < this.yPosition + this.height;
            this.mouseDragged(mc, par2int, par3int);
            int l = 14737632;
            if (this.hover) { l = 0x006eef4d; }
            this.drawCenteredString(fontrenderer, this.displayString, this.xPosition + this.width / 2, this.yPosition + (this.height - 6) / 2, l);
        }
    }
}
