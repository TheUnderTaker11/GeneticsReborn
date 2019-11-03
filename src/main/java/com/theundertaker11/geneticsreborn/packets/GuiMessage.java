package com.theundertaker11.geneticsreborn.packets;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.theundertaker11.geneticsreborn.tile.GRTileEntityBasicEnergyReceiver;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class GuiMessage implements IMessage {
	private BlockPos pos;
	private Map<Integer, Integer> data;
	
	public GuiMessage() {		
	}

	public GuiMessage(TileEntity te, int field, int value) {
		this.pos = te.getPos();
		this.data = new HashMap<Integer, Integer>();
		data.put(field, value);
	}
	
	public GuiMessage(TileEntity te, Map<Integer, Integer> data) {
		this.pos = te.getPos();
		this.data = data;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		int x = buf.readInt();
		int y = buf.readInt();
		int z = buf.readInt();
		this.pos = new BlockPos(x, y, z);
		this.data = new HashMap<Integer, Integer>();
		int size = buf.readInt();
		int key, value;
		for (int i=0;i<size;i++) {
			key = buf.readInt();
			value = buf.readInt();
			this.data.put(key, value);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		// TODO Auto-generated method stub
		buf.writeInt(pos.getX());
		buf.writeInt(pos.getY());
		buf.writeInt(pos.getZ());
		buf.writeInt(data.size());
		
		for (Entry<Integer, Integer> e : data.entrySet()) 
			buf.writeInt(e.getKey()).writeInt(e.getValue());
	}
	
	public static class GuiMessageHandler implements IMessageHandler<GuiMessage, IMessage> {

		@Override
		public IMessage onMessage(GuiMessage message, MessageContext ctx) {
			IThreadListener mainThread = (WorldServer) ctx.getServerHandler().player.getEntityWorld();
			mainThread.addScheduledTask(new Runnable() {
				@Override
				public void run() {
					net.minecraft.entity.player.EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
					GRTileEntityBasicEnergyReceiver te = (GRTileEntityBasicEnergyReceiver)serverPlayer.world.getTileEntity(message.pos);
					for (Entry<Integer, Integer> e : message.data.entrySet())
						te.setField(e.getKey(), e.getValue());
					te.markDirty();					
				}
			});
			return null;
		}
		
	}

}
