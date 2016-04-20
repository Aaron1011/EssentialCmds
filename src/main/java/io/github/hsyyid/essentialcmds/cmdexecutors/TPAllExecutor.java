/*
 * This file is part of EssentialCmds, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015 - 2015 HassanS6000
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.hsyyid.essentialcmds.cmdexecutors;

import io.github.hsyyid.essentialcmds.internal.AsyncCommandExecutorBase;
import io.github.hsyyid.essentialcmds.utils.Utils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;

public class TPAllExecutor extends AsyncCommandExecutorBase
{
	@Override
	public void executeAsync(CommandSource src, CommandContext ctx)
	{
		if (src instanceof Player)
		{
			Player player = (Player) src;

			for (Player p : Sponge.getServer().getOnlinePlayers())
			{
				Utils.setLastTeleportOrDeathLocation(p.getUniqueId(), p.getLocation());

				if (p.getWorld().getUniqueId() != player.getWorld().getUniqueId())
				{
					p.transferToWorld(player.getWorld().getUniqueId(), player.getLocation().getPosition());
				}
				else
				{
					p.setLocation(player.getLocation());
				}

				p.sendMessage(Text.of(TextColors.GREEN, "You were teleported to ", TextColors.YELLOW, player.getName()));
			}

			src.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Teleported all players to you."));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "Must be an in-game player to use /tpall!"));
		}
	}

	@Nonnull
	@Override
	public String[] getAliases()
	{
		return new String[] { "tpall" };
	}

	@Nonnull
	@Override
	public CommandSpec getSpec()
	{
		return CommandSpec.builder()
			.description(Text.of("TP All Command"))
			.permission("essentialcmds.tpall.use")
			.executor(this)
			.build();
	}
}