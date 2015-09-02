package io.github.hsyyid.spongeessentialcmds.cmdexecutors;

import io.github.hsyyid.spongeessentialcmds.Main;

import java.util.ArrayList;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.command.CommandException;
import org.spongepowered.api.util.command.CommandResult;
import org.spongepowered.api.util.command.CommandSource;
import org.spongepowered.api.util.command.args.CommandContext;
import org.spongepowered.api.util.command.source.CommandBlockSource;
import org.spongepowered.api.util.command.source.ConsoleSource;
import org.spongepowered.api.util.command.spec.CommandExecutor;

public class MessageExecutor implements CommandExecutor
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		Player recipient = ctx.<Player>getOne("recipient").get();
		String message = ctx.<String>getOne("message").get();
		
		ArrayList<Player> socialSpies = new ArrayList<Player>();
		
		for(Player player : Main.game.getServer().getOnlinePlayers())
		{
			if(Main.socialSpies.contains(player.getUniqueId()))
			{
				socialSpies.add(player);
			}
		}
		
		if(recipient.equals(src))
		{
			src.sendMessage(Texts.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You cannot send a private message to yourself!"));
			return CommandResult.success();
		}
		
		if(src instanceof Player)
		{
			Player player = (Player) src;
			src.sendMessage(Texts.of(TextColors.GREEN,"Success! ", TextColors.YELLOW, "Sent message to ", TextColors.RED, recipient.getName()));
			recipient.sendMessage(Texts.of(TextColors.GOLD, "[", TextColors.RED, player.getName(), TextColors.GOLD, " > ", TextColors.RED, recipient.getName(), TextColors.GOLD, "]: ", TextColors.GRAY, message));
			
			for(Player socialspy : socialSpies)
			{
				socialspy.sendMessage(Texts.of(TextColors.GOLD, "[", TextColors.RED, player.getName(), TextColors.GOLD, " > ", TextColors.RED, recipient.getName(), TextColors.GOLD, "]: ", TextColors.GRAY, message));
			}
		}
		else if(src instanceof ConsoleSource)
		{
			src.sendMessage(Texts.of(TextColors.GREEN,"Success! ", TextColors.RED, "Sent message to " + recipient.getName()));
			recipient.sendMessage(Texts.of(TextColors.GOLD, "[", TextColors.RED, "Console", TextColors.GOLD, " > ", TextColors.RED, recipient.getName(), TextColors.GOLD, "]: ", TextColors.GRAY, message));
		
			for(Player socialspy : socialSpies)
			{
				socialspy.sendMessage(Texts.of(TextColors.GOLD, "[", TextColors.RED, "Console", TextColors.GOLD, " > ", TextColors.RED, recipient.getName(), TextColors.GOLD, "]: ", TextColors.GRAY, message));
			}
		}
		else if(src instanceof CommandBlockSource)
		{
			src.sendMessage(Texts.of(TextColors.GREEN,"Success! ", TextColors.RED, "Sent message to " + recipient.getName()));
			recipient.sendMessage(Texts.of(TextColors.GOLD, "[", TextColors.RED, "CommandBlock", TextColors.GOLD, " > ", TextColors.RED, recipient.getName(), TextColors.GOLD, "]: ", TextColors.GRAY, message));
			
			for(Player socialspy : socialSpies)
			{
				socialspy.sendMessage(Texts.of(TextColors.GOLD, "[", TextColors.RED, "CommandBlock", TextColors.GOLD, " > ", TextColors.RED, recipient.getName(), TextColors.GOLD, "]: ", TextColors.GRAY, message));
			}
		}

		return CommandResult.success();
	}
}