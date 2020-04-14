package com.theundertaker11.geneticsreborn.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

import com.theundertaker11.geneticsreborn.Reference;
import com.theundertaker11.geneticsreborn.api.capability.genes.EnumGenes;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
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
        this.addSubcommand(new CommandList());
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
    
	public static List<String> getTabCompletions(MinecraftServer server, String[] args, String cmd) {
		List<String> result = new ArrayList<String>();

        String match = args[args.length - 1].toLowerCase();
		if (args.length == 1) { //argument of what player/players to operate on
			if("@p".startsWith(match)) result.add("@p");
			if("@r".startsWith(match)) result.add("@r");
			if("@a".startsWith(match)) result.add("@a");

			for (EntityPlayer p : server.getPlayerList().getPlayers()) 
				if (p.getName().toLowerCase().startsWith(match)) result.add(p.getName());
		} else if (args.length >= 2 && !cmd.equals("list") && !args[1].equals("all")) { //only for add/remove commands
			if (args.length == 2 && ("".equals(match) || "all".startsWith(match))) result.add("all");
			for (EnumGenes g : EnumGenes.values())
				if (g.name().toLowerCase().startsWith(match)) result.add(g.name());
		}
		
		return result;
	}

	public static List<EntityLivingBase> getTargets(MinecraftServer server, String arg, ICommandSender sender) throws CommandException{
        List<EntityLivingBase> targets = new ArrayList<>();
        List<EntityPlayerMP> temp;
        Random rand = new Random();
        switch(arg) { //make list of players to act upon
            case "@p": //select executing player
                targets.add((EntityLivingBase) sender.getCommandSenderEntity());
                if(targets.get(0) == null) throw new CommandException("Can't get executing entity");
                break;
            case "@a": //select all players
                for(EntityLivingBase player : server.getPlayerList().getPlayers()) {targets.add(player);}
                if(targets.size() == 0) throw new CommandException("No player found"); //CHECK LATER
                break;
            case "@r": //select a random player
                temp = server.getPlayerList().getPlayers();
                if(temp.size() == 0) throw new CommandException("No player found");
                targets.add((EntityLivingBase)temp.get(rand.nextInt(temp.size())));
                break;
            default: //select player of given name
                targets.add(server.getPlayerList().getPlayerByUsername(arg));
                if(targets.get(0) == null) throw new CommandException("Player not found: " + arg);
                break;
        }
        return targets;
    }
}
