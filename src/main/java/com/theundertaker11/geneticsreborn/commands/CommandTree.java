package com.theundertaker11.geneticsreborn.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandTree extends CommandTreeBase {
    private static final List<String> aliases = new ArrayList<>();
    
    static {
        aliases.add("gene");
    }
    
    public CommandTree() {        
        this.addSubcommand(new CommandAdd());
        this.addSubcommand(new CommandRemove());
    }    
    
	@Override
	public String getName() {		
		return Reference.MODID;
	}

	@Override
	public String getUsage(ICommandSender sender) {
        final StringJoiner joiner = new StringJoiner("\n");
        for (final ICommand command : getSubCommands()) {
            joiner.add(command.getUsage(sender));
        }
        return joiner.toString();
	}
	
    @Override
    public void execute (MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length <= 0) sender.sendMessage(new TextComponentString(this.getUsage(sender)));
        else super.execute(server, sender, args);
    }
    
    @Override
    public boolean checkPermission (MinecraftServer server, ICommandSender sender) {
        return this.getRequiredPermissionLevel() <= 0 || super.checkPermission(server, sender);
    }
    
    @Override
    public List<String> getAliases() {
        return aliases;
    }	
    
	public static List<String> getTabCompletions(MinecraftServer server, String[] args) {
		List<String> result = new ArrayList<String>();
		
		if (args.length == 1) {
			String match = args[0].toLowerCase();
			if ("".equals(match)) result.add("@p");
			for (EntityPlayer p : server.getPlayerList().getPlayers()) 
				if ("".equals(match) || p.getName().toLowerCase().startsWith(match)) result.add(p.getName());										
		} else if (args.length == 2) {
			String match = args[1].toLowerCase();
			if ("".equals(match) ||"all".startsWith(match)) result.add("all");
			for (EnumGenes g : EnumGenes.values())
				if ("".equals(match) || g.name().toLowerCase().startsWith(match)) result.add(g.name());
		}
		
		return result;
	}
    
}
