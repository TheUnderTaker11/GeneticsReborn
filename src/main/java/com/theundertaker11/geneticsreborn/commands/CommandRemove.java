package com.theundertaker11.geneticsreborn.commands;

import java.util.List;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.api.capability.genes.IGenes;
import com.theundertaker11.geneticsreborn.event.PlayerTickEvent;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandRemove extends CommandBase {

	@Override
	public String getName() {
		return "remove";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 2) {
        	EntityLivingBase entity;
        	IGenes genes;
        	if ("@p".equals(args[0])) {
        		entity = (EntityLivingBase) sender.getCommandSenderEntity();
        	} else {
        		entity = server.getPlayerList().getPlayerByUsername(args[0]);
        	}
        	if (entity == null) throw new CommandException("Player not found: "+args[0]);

        	if ("all".equals(args[1])) {
    			genes = ModUtils.getIGenes(entity);
            	for (EnumGenes g : genes.getGeneList()) 
            		PlayerTickEvent.geneChanged(entity, g, false);
    			genes.removeAllGenes();
    			sender.sendMessage(new TextComponentString("Removed all genes from " + sender.getName()));
        	} else {
    			genes = ModUtils.getIGenes(entity);
            	for (int i=1; i< args.length;i++) {
            		EnumGenes gene = EnumGenes.fromGeneName(args[i]);
            		if (gene == null) throw new CommandException("No gene found named: "+args[i]);
               		PlayerTickEvent.geneChanged(entity, gene, false);
            		genes.removeGene(gene);
        			sender.sendMessage(new TextComponentString(String.format("Removed %d genes from %s",  args.length-1, sender.getName())));
            	}
        	}
        } else 
            throw new WrongUsageException(this.getUsage(sender));
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "/" + Reference.MODID + " remove <player> all | <gene_name>...";
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
