package com.theundertaker11.geneticsreborn.packets;

import com.theundertaker11.geneticsreborn.GeneticsReborn;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class StepHeightChange implements IMessage {
	private float new_height;
	
	public StepHeightChange() {
	}

	public StepHeightChange(float h) {
		new_height = h;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		new_height = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeFloat(new_height);
	}

	public static class Handler3 implements IMessageHandler<StepHeightChange, IMessage> {

		@Override
		public IMessage onMessage(final StepHeightChange message, final MessageContext ctx) {
		    if (ctx.side != Side.CLIENT) {
		        GeneticsReborn.log.error("StepHeightChange received on wrong side:" + ctx.side);
		        return null;
		    }		    
		    
		    EntityPlayer player = Minecraft.getMinecraft().player;
		    player.stepHeight = message.new_height;
		    
		    return null;
		}
	}
}
