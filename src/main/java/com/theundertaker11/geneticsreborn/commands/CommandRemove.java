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
		List<EntityLivingBase> entities;
		IGenes genes;
		int count = 0;

		if (args.length >= 2) {
			entities = CommandTree.getTargets(server, args[0], sender);
			for(EntityLivingBase entity : entities) {
				genes = ModUtils.getIGenes(entity);
				if ("all".equals(args[1])) {
					for (EnumGenes g : genes.getGeneList())
						PlayerTickEvent.geneChanged(entity, g, false);
					genes.removeAllGenes();
					sender.sendMessage(new TextComponentString("Removed all genes from " + entity.getName()));
					if(sender.getName() != entity.getName()) entity.sendMessage(new TextComponentString("Removed all genes from you"));
				} else {
					for (int i = 1; i < args.length; i++) {
						EnumGenes gene = EnumGenes.fromGeneName(args[i]);
						if (gene == null) //if no gene with name requested
							sender.sendMessage(new TextComponentString("No gene found called " + args[i] + ", so not removed"));
						else if (genes.hasGene(gene)) { //remove gene
							genes.removeGene(gene);
							PlayerTickEvent.geneChanged(entity, gene, false);
							count++;
						}
						else //if player doesn't have requested gene
							sender.sendMessage(new TextComponentString(String.format("%s doesn't have %s, so not removed", entity.getName(), args[i])));
					}
					sender.sendMessage(new TextComponentString(String.format("Removed %d genes from %s", count, entity.getName())));
					if(sender.getName() != entity.getName()) entity.sendMessage(new TextComponentString(String.format("Removed %d genes from you",  count)));
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
		return CommandTree.getTabCompletions(server, args, this.getName());
	}

}
