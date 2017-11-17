package com.theundertaker11.geneticsreborn.blocks;

import com.theundertaker11.geneticsreborn.items.GRItems;
import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class StorageBlockBase extends BlockBase {
	
	public static final PropertyDirection FACING = PropertyDirection.create("facing");
	
	public StorageBlockBase(String name, Material material, float hardness, float resistance) 
	{
		super(name, material, hardness, resistance);
		//this.isBlockContainer=true;
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}
	public StorageBlockBase(String name)
	{
		this(name, Material.IRON, 0.5f, 0.5f);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
    {
        return true;
    }
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state){return null;}
	
	/////////BEGIN CODE TO MAKE IT FACE TOWARDS THE PLAYER WHEN PLACED/////////
	@Override
    public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
        world.setBlockState(pos, state.withProperty(FACING, getFacingFromEntity(pos, placer)), 2);
    }
	
	public static EnumFacing getFacingFromEntity(BlockPos clickedBlock, EntityLivingBase entity) {
        return EnumFacing.getFacingFromVector(
             (float) (entity.posX - clickedBlock.getX()),
             0,//(float) (entity.posY - clickedBlock.getY()),
             (float) (entity.posZ - clickedBlock.getZ()));
    }

	@Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.getFront(meta & 7));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
    /////////END CODE TO MAKE IT FACE TOWARDS THE PLAYER WHEN PLACED/////////
    
    @Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
    	
        TileEntity tile = worldIn.getTileEntity(pos);	
        //All the null checks are just in case and probably not actually needed
        if (tile!=null&&tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)!=null&&tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN)!=null
        	&&tile instanceof GRTileEntityBasicEnergyReceiver)
        {
        	GRTileEntityBasicEnergyReceiver tileentity = (GRTileEntityBasicEnergyReceiver)tile;
        	IItemHandler input = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        	IItemHandler output = tileentity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.DOWN);
        	if(!input.getStackInSlot(0).isEmpty())
        	{
        		ItemStack inputstack = input.getStackInSlot(0);
        		EntityItem entityinput = new EntityItem(tileentity.getWorld(), tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), inputstack);
        		tileentity.getWorld().spawnEntity(entityinput);
        	}
        	if(!output.getStackInSlot(0).isEmpty())
        	{
        		ItemStack outputstack = output.getStackInSlot(0);
        		EntityItem entityoutput = new EntityItem(tileentity.getWorld(), tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), outputstack);
        		tileentity.getWorld().spawnEntity(entityoutput);
        	}
        	if(tileentity.getOverclockerCount()>0)
        	{
            	ItemStack overclockers = new  ItemStack(GRItems.Overclocker, tileentity.getOverclockerCount());
            	EntityItem entityoverclockers = new EntityItem(tileentity.getWorld(), tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), overclockers);
        		tileentity.getWorld().spawnEntity(entityoverclockers);
        	}
        }
        super.breakBlock(worldIn, pos, state);
    }
}
