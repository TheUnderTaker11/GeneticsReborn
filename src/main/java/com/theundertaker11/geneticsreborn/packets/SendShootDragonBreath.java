package com.theundertaker11.geneticsreborn.packets;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.event.GREventHandler;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This checks if the player has the gene so I don't have to try and check clientside.
 * @author TheUnderTaker11
 *
 */
public class SendShootDragonBreath implements IMessage {

	public SendShootDragonBreath() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler2 implements IMessageHandler<SendShootDragonBreath, IMessage> {

		@Override
		public IMessage onMessage(final SendShootDragonBreath message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getEntityWorld();
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					net.minecraft.entity.player.EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
					long now = serverPlayer.world.getWorldTime();
					boolean allow = GREventHandler.isInCooldown(serverPlayer, "dragonsbreath", now);

					if (EnumGenes.DRAGONS_BREATH.isActive() && allow && ModUtils.getIGenes(serverPlayer) != null) {
						IGenes genes = ModUtils.getIGenes(serverPlayer);
						if (genes.hasGene(EnumGenes.DRAGONS_BREATH)) {
							float x = -MathHelper.sin(serverPlayer.rotationYaw * 0.017453292F) * MathHelper.cos(serverPlayer.rotationPitch * 0.017453292F);
							float y = -MathHelper.sin(serverPlayer.rotationPitch * 0.017453292F);
							float z = MathHelper.cos(serverPlayer.rotationYaw * 0.017453292F) * MathHelper.cos(serverPlayer.rotationPitch * 0.017453292F);
							EntityDragonFireball dragonfireball = new EntityDragonFireball(serverPlayer.getEntityWorld());
							dragonfireball.shootingEntity = serverPlayer;
							dragonfireball.setPosition(serverPlayer.posX, serverPlayer.posY+1.5, serverPlayer.posZ);
							dragonfireball.accelerationX = x * 0.1D;
							dragonfireball.accelerationY = y * 0.1D;
							dragonfireball.accelerationZ = z * 0.1D;

							serverPlayer.getEntityWorld().spawnEntity(dragonfireball);
							GREventHandler.addCooldown(serverPlayer, "dragonsbreath", now, 1);							
						}
					}
				}
			});
			return null;
		}
	}
}
