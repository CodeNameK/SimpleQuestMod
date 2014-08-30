package taogunner.simplequest.client.gui;

import java.io.UnsupportedEncodingException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import taogunner.simplequest.SimpleQuestMod;
import taogunner.simplequest.common.CommonProxy;
import taogunner.simplequest.entity.passive.EntityNPC;
import taogunner.simplequest.network.Packet00GetJSONFromServer;
import taogunner.simplequest.util.json.JSONServerSide;
import taogunner.simplequest.util.json.JSONServerSide.JSSAnswer;

import com.google.gson.Gson;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiScreenNPCDialog extends GuiScreen
{
	private static final ResourceLocation iconLocation = new ResourceLocation(SimpleQuestMod.MODID + ":textures/gui/gui_npcdialog.png");
	private int xSize = 256;
	private int ySize = 232;
	private JSONServerSide dialog;
	private int quest_num = 0;
	private int drag = 0;
	private int xPosX = 0;
	private int xPosY = 0;
	
	public GuiScreenNPCDialog(String jsondialog)
	{
		this.dialog = new Gson().fromJson(jsondialog, JSONServerSide.class);
		this.quest_num = this.dialog.npc.quest_id;
	}
	
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float par3float)
	{
		xPosX = (this.width - this.xSize)/2;
		xPosY = (this.height - this.ySize)/2;
		this.drawGradientRect(0, 0, mc.getMinecraft().displayWidth, mc.getMinecraft().displayHeight, -1072689136, -804253680);
		this.drawGradientRect(xPosX + 18, xPosY, xPosX + xSize - 18, xPosY + ySize - 135, 0xFF000000, 0xFF000000);
		this.drawGradientRect(xPosX + 18, xPosY + 110, xPosX + xSize - 18, xPosY + ySize, 0xFF000000, 0xFF000000);
		super.drawScreen(mouseX, mouseY, par3float);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.getTextureManager().bindTexture(iconLocation);
		drawTexturedModalRect(xPosX,xPosY, 0, 0, this.xSize, this.ySize);
		drag = Mouse.getDWheel();
		if (drag != 0 && this.buttonList.size() > 6)
		{
			for (int i=0; i<this.buttonList.size(); i++)
			{
				if (((GuiButtonNPCDialog)this.buttonList.get(i)).yPosition < xPosY+110 | ((GuiButtonNPCDialog)this.buttonList.get(i)).yPosition > xPosY + 212) { ((GuiButtonNPCDialog)this.buttonList.get(i)).visible = false; }
				else {((GuiButtonNPCDialog)this.buttonList.get(i)).visible = true;}
				
				if  (drag > 0 && ((GuiButtonNPCDialog)this.buttonList.get(0)).yPosition >= xPosY + 120) { ((GuiButtonNPCDialog)this.buttonList.get(i)).yPosition = xPosY + 120 + i*((GuiButtonNPCDialog)this.buttonList.get(i)).height; }
				else if  (drag < 0 && ((GuiButtonNPCDialog)this.buttonList.get(this.buttonList.size()-1)).yPosition <= xPosY + 202) {}
				else { ((GuiButtonNPCDialog)this.buttonList.get(i)).yPosition += drag / 60; }
			}
		}
		EntityNPC npc = new EntityNPC(Minecraft.getMinecraft().thePlayer, quest_num, dialog.npc.texture_id, dialog.npc.item_id, dialog.npc.name);
		GuiInventory.func_147046_a(xPosX - 20, xPosY + 65, 30, - (Mouse.getX() - this.width + 295)/6,(Mouse.getY() - this.height - 180) / 6, npc);
		GuiInventory.func_147046_a(xPosX + 275, xPosY + 165, 30, - (Mouse.getX() - this.width - 295)/6,(Mouse.getY() - this.height) / 6, mc.thePlayer);

		try { this.fontRendererObj.drawSplitString( new String(this.dialog.text.getBytes(),"UTF-8"), xPosX + 25, xPosY + 15, 210, 0xFF00E000); }
		catch ( UnsupportedEncodingException e) { e.printStackTrace(); }
	}

	@Override
	public void initGui()
	{
		xPosX = (this.width - this.xSize)/2;
		xPosY = (this.height - this.ySize)/2;
		for (JSSAnswer answer:dialog.answers)
		{
			try { this.buttonList.add(new GuiButtonNPCDialog(answer.jump, xPosX + 24, xPosY + 120 + this.buttonList.size()*16,210,16, new String(answer.text.getBytes(),"UTF-8"))); }
			catch (UnsupportedEncodingException e) { e.printStackTrace(); }
		}
		for (int i=0; i<this.buttonList.size(); i++) { if (((GuiButtonNPCDialog)this.buttonList.get(i)).yPosition > xPosY + 210) { ((GuiButtonNPCDialog)this.buttonList.get(i)).visible = false; } }
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		if (button.id <= 0) { Minecraft.getMinecraft().thePlayer.closeScreen(); }
		else { CommonProxy.INSTANCE.sendToServer(new Packet00GetJSONFromServer(this.quest_num,button.id)); }
	}
}
