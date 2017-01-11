package com.theundertaker11.GeneticsReborn.packets;

import com.theundertaker11.GeneticsReborn.packets.SendShootDragonBreath.Handler2;
import com.theundertaker11.GeneticsReborn.packets.SendTeleportPlayer.Handler;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class GeneticsRebornPacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("GeneticsReborn");
	
	public static void init(){
		INSTANCE.registerMessage(Handler.class, SendTeleportPlayer.class, 0, Side.SERVER);
		INSTANCE.registerMessage(Handler2.class, SendShootDragonBreath.class, 1, Side.SERVER);
	}
}
