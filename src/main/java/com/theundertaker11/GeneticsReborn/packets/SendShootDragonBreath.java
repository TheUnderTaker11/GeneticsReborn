package com.theundertaker11.geneticsreborn.packets;

import com.theundertaker11.geneticsreborn.GeneticsReborn;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.GeneCapabilityProvider;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.event.GREventHandler;
import com.theundertaker11.geneticsreborn.util.ModUtils;
import com.theundertaker11.geneticsreborn.util.PlayerCooldowns;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.projectile.EntityDragonFireball;
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
public class SendShootDragonBreath implements IMessage, IMessageHandler<SendShootDragonBreath, IMessage>{
	
	public SendShootDragonBreath() { }

	@Override
	public void fromBytes(ByteBuf buf){}

	@Override
	public void toBytes(ByteBuf buf){}

	@Override
    public IMessage onMessage(final SendShootDragonBreath message, final MessageContext ctx) {
        IThreadListener mainThread = (WorldServer) ctx.getServerHandler().playerEntity.worldObj;
        mainThread.addScheduledTask(new Runnable() {
            @Override
            public void run() {
            	net.minecraft.entity.player.EntityPlayerMP serverPlayer = ctx.getServerHandler().playerEntity;
            	boolean allow = true;
    			for(int i=0; i<GREventHandler.cooldownList.size();i++)
    			{
    				if("dragonsbreath".equals(GREventHandler.cooldownList.get(i).getName())&&serverPlayer.getName().equals(GREventHandler.cooldownList.get(i).getPlayerName()))
    				{
    					allow = false;
    					break;
    				}
    			}
            	if(GeneticsReborn.enableDragonsBreath&&allow&&serverPlayer.hasCapability(GeneCapabilityProvider.GENES_CAPABILITY, null))
            	{
            		IGenes genes = ModUtils.getIGenes(serverPlayer);
            		if(genes.hasGene(EnumGenes.DRAGONS_BREATH))
            		{
            			Vec3d v3 = serverPlayer.getLookVec();
            			EntityDragon dragon = new EntityDragon(serverPlayer.getEntityWorld());
        	            EntityDragonFireball dragonfireball = new EntityDragonFireball(serverPlayer.getEntityWorld(), serverPlayer, v3.xCoord, v3.yCoord, v3.zCoord);
        	            dragonfireball.shootingEntity = dragon;
        	            dragonfireball.posX = serverPlayer.posX + v3.xCoord;
        	            dragonfireball.posY = serverPlayer.posY + serverPlayer.eyeHeight + v3.yCoord;
        	            dragonfireball.posZ = serverPlayer.posZ + v3.zCoord;
        	            dragonfireball.accelerationX = v3.xCoord*0.1;
        	            dragonfireball.accelerationY = v3.yCoord*0.1;
        	            dragonfireball.accelerationZ = v3.zCoord*0.1;
        	            serverPlayer.getEntityWorld().spawnEntityInWorld(dragonfireball);
            			GREventHandler.cooldownList.add(new PlayerCooldowns(serverPlayer, "dragonsbreath", 300));
            		}
            	}
            }
        });
        return null; // no response
    }
}
