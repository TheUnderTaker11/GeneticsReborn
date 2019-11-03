package com.theundertaker11.geneticsreborn.packets;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.theundertaker11.geneticsreborn.GeneticsReborn;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class ClientGeneChange implements IMessage {	
	public static final List<UUID> climbingPlayers = new ArrayList<UUID>();  
	
	private float new_value;
	private int gene;  //1 = step height, 2 = climb walls
	
	public ClientGeneChange() {
	}

	public ClientGeneChange(int g, float h) {
		gene = g;
		new_value = h;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		gene = buf.readInt();
		new_value = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(gene);
		buf.writeFloat(new_value);
	}

	public static class Handler3 implements IMessageHandler<ClientGeneChange, IMessage> {

		@Override
		public IMessage onMessage(final ClientGeneChange message, final MessageContext ctx) {
		    if (ctx.side != Side.CLIENT) {
		        GeneticsReborn.log.error("StepHeightChange received on wrong side:" + ctx.side);
		        return null;
		    }		    

		    EntityPlayer player = GeneticsReborn.proxy.getClientPlayer();
		    if (message.gene == 1) {
			    player.stepHeight = message.new_value;
		    } else if (message.gene == 2){
		    	if (message.new_value == 0.0) climbingPlayers.remove(player.getUniqueID());
		    	else climbingPlayers.add(player.getUniqueID());
		    }
		    return null;
		}
	}
}
