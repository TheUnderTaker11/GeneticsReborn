package com.theundertaker11.geneticsreborn.blocks.coalgenerator;

import com.theundertaker11.geneticsreborn.Reference;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.config.GuiUtils;

import java.util.ArrayList;
import java.util.List;

public class GuiCoalGenerator extends GuiContainer {

    public static ResourceLocation background = new ResourceLocation(Reference.MODID, "textures/gui/coal_generator_gui.png");
    public static final int WIDTH = 176;
    public static final int HEIGHT = 166;
    public GRTileEntityCoalGenerator tile;

    public GuiCoalGenerator(ContainerCoalGenerator containerCoalGenerator, GRTileEntityCoalGenerator te) {
        super(containerCoalGenerator);
        this.tile = te;
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.guiLeft = (this.width - this.xSize) / 2;
        this.guiTop = (this.height - this.ySize) / 2;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

        renderProgress();

        if (this.isPointInRegion(104, 20, 14, 42, mouseX, mouseY)) {
            List<String> energy = new ArrayList<String>();
            energy.add(tile.storage.getEnergyStored() + " / " + tile.storage.getMaxEnergyStored() + "  FE");
            GuiUtils.drawHoveringText(energy, mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
        }

        if (tile.burnTime > 0) {
            if (this.isPointInRegion(76, 35, 22, 15, mouseX, mouseY)) {
                List<String> energy = new ArrayList<String>();
                energy.add(tile.percentage() + " %");
                GuiUtils.drawHoveringText(energy, mouseX, mouseY, mc.displayWidth, mc.displayHeight, -1, mc.fontRenderer);
            }
        }
    }

    public void renderProgress() {
        if (tile.burnTime > 0) {
            int i = 22;
            int j = tile.burnTime * i / tile.currentItemBurnTime;
            drawTexturedModalRect(guiLeft + 76, guiTop + 35, 178, 61, j, 16);
        }

        if (tile.burnTime > 0) {
            int i = 14;
            int j = tile.burnTime * i / tile.currentItemBurnTime;
            drawTexturedModalRect(guiLeft + 53, guiTop + 66 - j, 177, 59 - j, 14, j);
        }

        if (tile.storage.getEnergyStored() > 0) {
            int i = 40;
            int j = tile.storage.getEnergyStored() * i / tile.storage.getMaxEnergyStored();
            drawTexturedModalRect(guiLeft + 105, guiTop + 61 - j, 178, 44 - j, 12, j);
        }
    }
}
