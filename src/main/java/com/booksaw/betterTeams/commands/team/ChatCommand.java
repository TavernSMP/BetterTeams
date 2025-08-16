package com.booksaw.betterTeams.commands.team;

import com.booksaw.betterTeams.*;
import com.booksaw.betterTeams.commands.ParentCommand;
import com.booksaw.betterTeams.commands.presets.TeamSubCommand;
import com.booksaw.betterTeams.message.HelpMessage;
import com.muhammaddaffa.tavernusername.spigot.users.User;
import com.muhammaddaffa.tavernusername.spigot.users.repository.UserRepository;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ChatCommand extends TeamSubCommand {

	private final ParentCommand parentCommand;

	public ChatCommand(ParentCommand parentCommand) {
		this.parentCommand = parentCommand;
	}

	@Override
	public CommandResponse onCommand(TeamPlayer player, String label, String[] args, Team team) {

		if (args.length == 0) {
			// toggle chat
			if (!Main.plugin.getConfig().getBoolean("allowToggleTeamChat")) {
				return new CommandResponse(new HelpMessage(this, label, parentCommand));
			}

			if (player.isInTeamChat()) {
				player.setTeamChat(false);
				return new CommandResponse(true, "chat.disabled");
			} else {
				player.setTeamChat(true);
				player.setAllyChat(false);
				return new CommandResponse(true, "chat.enabled");
			}
		}

		StringBuilder message = new StringBuilder();
		for (String arg : args) {
			// Reverse engineer to replace the real name with fake name
			User user = UserRepository.findRealName(arg);
			if (user != null && user.getUsername() != null) {
				message.append(user.getUsername()).append(" ");
				continue;
			}
			message.append(arg).append(" ");
		}

		team.sendMessage(player, message.toString());
		return new CommandResponse(true);
	}

	@Override
	public String getCommand() {
		return "chat";
	}

	@Override
	public int getMinimumArguments() {
		return 0;
	}

	@Override
	public String getNode() {
		return "chat";
	}

	@Override
	public String getHelp() {
		return "Send a message only to your team";
	}

	@Override
	public String getArguments() {
		return "[message]";
	}

	@Override
	public void onTabComplete(List<String> options, CommandSender sender, String label, String[] args) {
	}

	@Override
	public int getMaximumArguments() {
		return -10;
	}

	@Override
	public PlayerRank getDefaultRank() {
		return PlayerRank.DEFAULT;
	}

}
