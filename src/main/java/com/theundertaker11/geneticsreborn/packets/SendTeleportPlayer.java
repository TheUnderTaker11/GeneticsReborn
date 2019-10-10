package com.theundertaker11.geneticsreborn.packets;

import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.event.GREventHandler;
import com.theundertaker11.geneticsreborn.util.ModUtils;
import com.theundertaker11.geneticsreborn.util.PlayerCooldowns;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * This checks if the player has the gene so I don't have to try and check clientside.
 */
public class SendTeleportPlayer implements IMessage {

	public SendTeleportPlayer() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {
	}

	@Override
	public void toBytes(ByteBuf buf) {
	}

	public static class Handler implements IMessageHandler<SendTeleportPlayer, IMessage> {

		@Override
		public IMessage onMessage(final SendTeleportPlayer message, final MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getEntityWorld();
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					net.minecraft.entity.player.EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
					boolean allowteleport = true;
					for (int i = 0; i < GREventHandler.cooldownList.size(); i++) {
						PlayerCooldowns cooldownEntry = GREventHandler.cooldownList.get(i);
						if ("teleport".equals(cooldownEntry.getName()) && serverPlayer.getName().equals(cooldownEntry.getPlayerName())) {
							allowteleport = false;
							break;
						}
					}
					if (EnumGenes.TELEPORTER.isActive()  && allowteleport && ModUtils.getIGenes(serverPlayer) != null) {
						IGenes genes = ModUtils.getIGenes(serverPlayer);
						if (genes.hasGene(EnumGenes.TELEPORTER)) {
							int distance = 6;
							Vec3d lookVec = serverPlayer.getLookVec();
							Vec3d start = new Vec3d(serverPlayer.posX, serverPlayer.posY + serverPlayer.getEyeHeight(), serverPlayer.posZ);
							if (serverPlayer.isSneaking()) {
								distance /= 2;
							}
							Vec3d end = start.addVector(lookVec.x * distance, lookVec.y * distance, lookVec.z * distance);

							serverPlayer.setPositionAndUpdate(end.x, end.y, end.z);
							GREventHandler.cooldownList.add(new PlayerCooldowns(serverPlayer, "teleport", 10));
						}
					}
				}
			});
			return null;
		}
	}
}
