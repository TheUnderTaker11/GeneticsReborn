package com.theundertaker11.GeneticsReborn.packets;

import com.theundertaker11.GeneticsReborn.GREventHandler;
import com.theundertaker11.GeneticsReborn.GeneticsReborn;
import com.theundertaker11.GeneticsReborn.api.capability.genes.EnumGenes;
import com.theundertaker11.GeneticsReborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.GeneticsReborn.api.capability.genes.IGenes;
import com.theundertaker11.GeneticsReborn.util.ModUtils;
import com.theundertaker11.GeneticsReborn.util.PlayerCooldowns;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
/**
 * This checks if the player has the gene so I don't have to try and check clientside.
 * @author TheUnderTaker11
 *
 */
public class SendTeleportPlayer implements IMessage{
	
	public SendTeleportPlayer() { }

	@Override
	public void fromBytes(ByteBuf buf){}
	@Override
	public void toBytes(ByteBuf buf){}

	public static class Handler implements IMessageHandler<SendTeleportPlayer, IMessage> {
	    
	    @Override
	    public IMessage onMessage(final SendTeleportPlayer message, final MessageContext ctx) {
	        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
	        mainThread.addScheduledTask(new Runnable() {
	            @Override
	            public void run() {
	            	net.minecraft.entity.player.EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
	            	boolean allowteleport = true;
	    			for(int i=0; i<GREventHandler.cooldownList.size();i++)
	    			{
	    				if(GREventHandler.cooldownList.get(i).getName().equals("teleport")&&serverPlayer.getName().equals(GREventHandler.cooldownList.get(i).getPlayerName()))
	    				{
	    					allowteleport = false;
	    					break;
	    				}
	    			}
	            	if(GeneticsReborn.enableTeleporter&&allowteleport&&serverPlayer.hasCapability(GeneCapabilityProvider.GENES_CAPABILITY, null))
	            	{
	            		IGenes genes = ModUtils.getIGenes(serverPlayer);
	            		if(genes.hasGene(EnumGenes.TELEPORTER))
	            		{
	            			int distance = 6;
	            			Vec3d lookVec = serverPlayer.getLookVec();
	            			Vec3d start = new Vec3d(serverPlayer.posX, serverPlayer.posY + serverPlayer.getEyeHeight(), serverPlayer.posZ);
	            			if (serverPlayer.isSneaking()) {
	            				distance /= 2;
	            			}
	            			Vec3d end = start.addVector(lookVec.xCoord * distance, lookVec.yCoord * distance, lookVec.zCoord * distance);
	            			RayTraceResult position = serverPlayer.getEntityWorld().rayTraceBlocks(start, end);
	                
	            			serverPlayer.setPositionAndUpdate(end.xCoord, end.yCoord, end.zCoord);
	            			GREventHandler.cooldownList.add(new PlayerCooldowns(serverPlayer, "teleport", 10));
	            		}
	            	}
	            }
	        });
	        return null; // no response
	    }
	}
}
