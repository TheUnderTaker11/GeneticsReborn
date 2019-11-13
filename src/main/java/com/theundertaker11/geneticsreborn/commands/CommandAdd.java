package com.theundertaker11.geneticsreborn.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.Genes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.event.PlayerTickEvent;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandAdd extends CommandBase {

	@Override
	public String getName() {
		return "add";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		EntityLivingBase entity;
		IGenes genes;
        if (args.length >= 2) {
        	if ("@p".equals(args[0])) {
        		entity = (EntityLivingBase) sender.getCommandSenderEntity();
        	} else {
        		entity = server.getPlayerList().getPlayerByUsername(args[0]);
        	}
        	if (entity == null) throw new CommandException("Player not found: "+args[0]);

        	if ("all".equals(args[1])) {
    			genes = ModUtils.getIGenes(entity);
    			for (EnumGenes g : EnumGenes.values()) 
    				if (!g.isNegative() && !genes.hasGene(g)) {
    					PlayerTickEvent.geneChanged(entity, g, true);
    					genes.addGene(g);
    				}
    			sender.sendMessage(new TextComponentString("Added all genes to " + sender.getName()));
        	} else {
      			genes = ModUtils.getIGenes(entity);
            	for (int i=1; i< args.length;i++) {
            		EnumGenes gene = EnumGenes.fromGeneName(args[i]);
            		if (gene == null) throw new CommandException("No gene found named: "+args[i]);
           			if (!genes.hasGene(gene)) {
           				genes.addGene(gene);
                   		PlayerTickEvent.geneChanged(entity, gene, true);
           			}
        			sender.sendMessage(new TextComponentString(String.format("Added %d genes to %s",  args.length-1, sender.getName())));
            	}
        	}
        } else 
            throw new WrongUsageException(this.getUsage(sender));
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/" + Reference.MODID + " add <player> [ all | <gene_name>...]";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,	BlockPos targetPos) {
		return CommandTree.getTabCompletions(server, args);
	}
}
