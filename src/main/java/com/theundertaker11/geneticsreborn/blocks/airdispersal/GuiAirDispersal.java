package com.theundertaker11.geneticsreborn.blocks.airdispersal;

import java.io.IOException;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.blocks.GuiBase;
import com.theundertaker11.geneticsreborn.packets.GeneticsRebornPacketHandler;
import com.theundertaker11.geneticsreborn.packets.GuiMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class GuiAirDispersal extends GuiBase {
	private GRTileEntityAirDispersal tileEntity;
	private final Item[] guess = new Item[4];
	private boolean initNoPower;


	public GuiAirDispersal(InventoryPlayer invPlayer, GRTileEntityAirDispersal tileInventory){
		super(new ContainerAirDispersal(invPlayer, tileInventory), tileInventory);
		this.tileEntity = tileInventory;
		texture = new ResourceLocation(Reference.MODID, "textures/gui/airdispersal.png");
		hasProgressArrow = false;
		ENERGY_OFFSET_Y = -14;
		useDefaultText = false;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		initNoPower = !tileEntity.hasPower();
		if (!initNoPower)
			this.buttonList.add(new GuiButton( 1, guiLeft+109, guiTop+53, 58, 20, tileEntity.isLocked() ? "Unlock" : "Lock"));
	}	

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int x, int y) {
		super.drawGuiContainerBackgroundLayer(partialTicks, x, y);		
		if (tileEntity.hasPower()) {
			if (initNoPower) {
				this.buttonList.clear();
				this.buttonList.add(new GuiButton( 1, guiLeft+109, guiTop+53, 58, 20, tileEntity.isLocked() ? "Unlock" : "Lock"));
				initNoPower = false;
			}
			int[] digits = timeToDigits(tileEntity.timeLeft());
			for (int i=0; i<4; i++) {
				drawTexturedModalRect(guiLeft+110 + (i*14), guiTop+33, 2 + (digits[i]*14), 169, 14, 19);
			}
		} else {
			if (!initNoPower) {
				this.buttonList.clear();
				initNoPower = true;
			}
		}

		Item[] lock = (tileEntity.isLocked()) ? guess : tileEntity.getLock();
		if (lock[0] != null) itemRender.renderItemIntoGUI(new ItemStack(lock[0], 1), guiLeft+87, guiTop+9); 
		if (lock[1] != null) itemRender.renderItemIntoGUI(new ItemStack(lock[1], 1), guiLeft+108, guiTop+9); 
		if (lock[2] != null) itemRender.renderItemIntoGUI(new ItemStack(lock[2], 1), guiLeft+129, guiTop+9); 
		if (lock[3] != null) itemRender.renderItemIntoGUI(new ItemStack(lock[3], 1), guiLeft+150, guiTop+9);
		
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		super.drawGuiContainerForegroundLayer(x, y);
	}
	
	private int[] timeToDigits(int ticks) {
		int[] digits = new int[4];
		int time = ticks / 20;
		int min = time / 60;
		int sec = time % 60;
		
		digits[0] = (min / 10) % 10;
		digits[1] = min % 10;
		digits[2] = (sec / 10) % 10;
		digits[3] = sec % 10;
		return digits;
	}
	
	private int digitsToTime(int[] digits) {
		return (digits[3] +
			    digits[2] * 10 + 
			    digits[1] * 60 + 
			    digits[0] * 60 * 10) * 20;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (tileEntity.isLocked()) {
			Item[] lock = tileEntity.getLock();
			for (int i=0;i<4;i++)
				if (lock[i] != null && lock[i] != guess[i]) return;
			//success!
			tileEntity.setLocked(false);
			tileEntity.clearLock();
		} else {
			tileEntity.setLocked(true);
		}
		GeneticsRebornPacketHandler.INSTANCE.sendToServer(new GuiMessage(tileEntity, 4, tileEntity.getState()));
		this.buttonList.clear();
		this.buttonList.add(new GuiButton( 1, guiLeft+109, guiTop+53, 58, 20, tileEntity.isLocked() ? "Unlock" : "Lock"));
	}
	
	private void addTicks(int pos) {
		int[] digits = timeToDigits(tileEntity.timeLeft());
		if (pos == 2) digits[pos] = (digits[pos] + 1) % 6;
		else digits[pos] = (digits[pos] + 1) % 10;
		tileEntity.setTimeLeft(digitsToTime(digits));
		GeneticsRebornPacketHandler.INSTANCE.sendToServer(new GuiMessage(tileEntity, 3, tileEntity.getTimeLeft()));
	}
	
	private void setLock(int pos) {
		Item item = Minecraft.getMinecraft().player.inventory.getItemStack().getItem();
		if (tileEntity.isLocked()) guess[pos] = item;
		else tileEntity.setLock(pos, item);
	}
	
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (tileEntity.isLocked()) return;
		if ((isPointInRegion(110, 33, 14, 19, mouseX, mouseY) && (mouseButton == 0))) addTicks(0);
		if ((isPointInRegion(124, 33, 14, 19, mouseX, mouseY) && (mouseButton == 0))) addTicks(1);
		if ((isPointInRegion(138, 33, 14, 19, mouseX, mouseY) && (mouseButton == 0))) addTicks(2);
		if ((isPointInRegion(152, 33, 14, 19, mouseX, mouseY) && (mouseButton == 0))) addTicks(3);

		if ((isPointInRegion(87, 9, 16, 16, mouseX, mouseY) && (mouseButton == 0))) setLock(0);
		if ((isPointInRegion(108, 9, 16, 16, mouseX, mouseY) && (mouseButton == 0))) setLock(1);
		if ((isPointInRegion(129, 9, 16, 16, mouseX, mouseY) && (mouseButton == 0))) setLock(2);
		if ((isPointInRegion(150, 9, 16, 16, mouseX, mouseY) && (mouseButton == 0))) setLock(3);
	}
	
	
}
