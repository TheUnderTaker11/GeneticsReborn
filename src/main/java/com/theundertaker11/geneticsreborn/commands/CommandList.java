package com.theundertaker11.geneticsreborn.commands;

import java.util.List;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;
import com.theundertaker11.geneticsreborn.util.ModUtils;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

public class CommandList extends CommandBase {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        List<EntityLivingBase> entities;

        if (args.length == 1) {
            entities = CommandTree.getTargets(server, args[0], sender);

            for(EntityLivingBase entity : entities) {
                List<EnumGenes> genes = ModUtils.getIGenes(entity).getGeneList();
                String msg = String.format("Player %s has %d genes: ", entity.getName(), genes.size());
                for(EnumGenes gene : genes) {
                    if (gene != genes.get(0)) msg += ", ";
                    msg += gene.getDescription();
                }
                sender.sendMessage(new TextComponentString(msg));
            }
        } else {
            throw new WrongUsageException(this.getUsage(sender));
        }
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + Reference.MODID + " list <player>";
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
