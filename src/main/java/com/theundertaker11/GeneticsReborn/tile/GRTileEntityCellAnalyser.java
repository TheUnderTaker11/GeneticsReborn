package com.theundertaker11.GeneticsReborn.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class GRTileEntityCellAnalyser extends TileEntity{
	
	public GRTileEntityCellAnalyser(){
		super();
	}

	@Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
        super.writeToNBT(compound);
        return compound;
        
    }
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
	}
}
