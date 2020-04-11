package com.theundertaker11.geneticsreborn.blocks.coalgenerator;

import com.theundertaker11.geneticsreborn.blocks.BaseContainer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerCoalGenerator extends BaseContainer {

    public ContainerCoalGenerator(InventoryPlayer player, GRTileEntityCoalGenerator te) {
        super();
        attachPlayerInventory(player);
        addSlotToContainer(new SlotItemHandler(te.inventory, 0, 52, 34));
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

@Override
    protected boolean canAcceptItem(Slot slot) {
		return TileEntityFurnace.isItemFuel(slot.getStack());
	}

}
