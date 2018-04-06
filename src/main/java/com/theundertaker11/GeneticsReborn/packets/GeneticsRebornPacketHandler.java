package com.theundertaker11.geneticsreborn.packets;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class GeneticsRebornPacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("geneticsreborn");
	
	public static void init(){
		INSTANCE.registerMessage(SendTeleportPlayer.class, SendTeleportPlayer.class, 0, Side.SERVER);
		INSTANCE.registerMessage(SendShootDragonBreath.class, SendShootDragonBreath.class, 1, Side.SERVER);
	}
}
