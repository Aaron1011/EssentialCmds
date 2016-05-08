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

import io.github.hsyyid.essentialcmds.internal.CommandExecutorBase;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.entity.ExperienceHolderData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import javax.annotation.Nonnull;

public class ExpExecutor extends CommandExecutorBase
{
	public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException
	{
		if(src instanceof Player)
		{
			Player player = (Player) src;
			ExperienceHolderData expHolderData = player.getOrCreate(ExperienceHolderData.class).get();
			player.sendMessage(Text.of(TextColors.GOLD, "Your current experience: ", TextColors.GRAY, expHolderData.totalExperience().get()));
			player.sendMessage(Text.of(TextColors.GOLD, "Experience to next level: ", TextColors.GRAY, expHolderData.getExperienceBetweenLevels().get() - expHolderData.experienceSinceLevel().get()));
		}
		else
		{
			src.sendMessage(Text.of(TextColors.DARK_RED, "Error! ", TextColors.RED, "You must be an in-game player to use this command!"));
		}
		
		return CommandResult.success();
	}

	@Nonnull
	@Override
	public String[] getAliases() {
		return new String[] { "exp", "experience" };
	}

	@Nonnull
	@Override
	public CommandSpec getSpec() {
		return CommandSpec.builder()
				.description(Text.of("Experience Command"))
				.children(getChildrenList(new GiveExecutor(), new SetExecutor(), new TakeExecutor()))
				.permission("essentialcmds.exp.use")
				.executor(this)
				.build();
	}

	private static class GiveExecutor extends CommandExecutorBase {

		@Nonnull
		@Override
		public String[] getAliases() {
			return new String[] { "give" };
		}

		@Nonnull
		@Override
		public CommandSpec getSpec() {
			return CommandSpec.builder()
					.description(Text.of("Experience Give Command"))
					.permission("essentialcmds.exp.give.use")
					.arguments(GenericArguments.seq(
							GenericArguments.player(Text.of("target")),
							GenericArguments.integer(Text.of("exp"))))
					.executor(this)
					.build();
		}

		@Override
		public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
			int expLevel = ctx.<Integer> getOne("exp").get();
			Player player = ctx.<Player> getOne("target").get();
			player.offer(Keys.TOTAL_EXPERIENCE, player.get(Keys.TOTAL_EXPERIENCE).get() + expLevel);
			src.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Gave " + player.getName() + " " + expLevel + " experience."));
			return CommandResult.success();
		}
	}

	private static class SetExecutor extends CommandExecutorBase {

		@Nonnull
		@Override
		public String[] getAliases() {
			return new String[] { "set" };
		}

		@Nonnull
		@Override
		public CommandSpec getSpec() {
			return CommandSpec.builder()
					.description(Text.of("Experience Set Command"))
					.permission("essentialcmds.exp.set.use")
					.arguments(GenericArguments.seq(
							GenericArguments.player(Text.of("target")),
							GenericArguments.integer(Text.of("exp"))))
					.executor(this)
					.build();
		}

		@Override
		public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
			int expLevel = ctx.<Integer> getOne("exp").get();
			Player player = ctx.<Player> getOne("target").get();
			player.offer(Keys.TOTAL_EXPERIENCE, expLevel);
			src.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Set " + player.getName() + "'s experience level to " + expLevel + "."));
			return CommandResult.success();
		}
	}

	private static class TakeExecutor extends CommandExecutorBase {

		@Nonnull
		@Override
		public String[] getAliases() {
			return new String[] { "take" };
		}

		@Nonnull
		@Override
		public CommandSpec getSpec() {
			return CommandSpec.builder()
				.description(Text.of("Experience Take Command"))
				.permission("essentialcmds.exp.take.use")
				.arguments(GenericArguments.seq(
						GenericArguments.player(Text.of("target")),
						GenericArguments.integer(Text.of("exp"))))
				.executor(this)
				.build();
		}

		@Override
		public CommandResult execute(CommandSource src, CommandContext ctx) throws CommandException {
			int expLevel = ctx.<Integer> getOne("exp").get();
			Player player = ctx.<Player> getOne("target").get();
			player.offer(Keys.TOTAL_EXPERIENCE, player.get(Keys.TOTAL_EXPERIENCE).get() - expLevel);
			src.sendMessage(Text.of(TextColors.GREEN, "Success! ", TextColors.YELLOW, "Took " + expLevel + " experience from " + player.getName() + "."));
			return CommandResult.success();
		}
	}
}
