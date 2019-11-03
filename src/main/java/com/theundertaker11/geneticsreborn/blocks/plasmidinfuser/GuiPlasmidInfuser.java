package com.theundertaker11.geneticsreborn.blocks.plasmidinfuser;

import java.awt.Color;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.blocks.GuiBase;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiPlasmidInfuser extends GuiBase {
    private final int NUMCOUNT_XPOS = 124;
    GRTileEntityPlasmidInfuser tileEntity;
    
    public GuiPlasmidInfuser(InventoryPlayer invPlayer, GRTileEntityPlasmidInfuser tileInventory) {
        super(new ContainerPlasmidInfuser(invPlayer, tileInventory), tileInventory);
        texture = new ResourceLocation(Reference.MODID, "textures/gui/guiplasmidinfuser.png");
        tileEntity = (GRTileEntityPlasmidInfuser) tileInventory;
    }


    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
    	super.drawGuiContainerForegroundLayer(mouseX, mouseY);
        fontRenderer.drawString(tileEntity.num + "/" + tileEntity.numNeeded, NUMCOUNT_XPOS, OVERCLOCKERCOUNT_YPOS, Color.darkGray.getRGB());
    }
}
