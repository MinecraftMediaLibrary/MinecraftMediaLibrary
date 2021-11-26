/*
 * MIT License
 *
 * Copyright (c) 2021 Brandon Li
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.github.pulsebeat02.deluxemediaplugin.bot.command;

import io.github.pulsebeat02.deluxemediaplugin.bot.DiscordLocale;
import io.github.pulsebeat02.deluxemediaplugin.bot.MediaBot;
import io.github.pulsebeat02.deluxemediaplugin.bot.audio.MusicManager;
import java.util.Set;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayAudioCommand extends DiscordBaseCommand {

  public PlayAudioCommand(@NotNull final MediaBot bot) {
    super(bot, "play", Set.of());
  }

  @Override
  public boolean execute(@NotNull final Message executor, final String @Nullable [] arguments) {
    if (this.invalidArguments(arguments)) {
      executor.getChannel().sendMessageEmbeds(DiscordLocale.ERR_INVALID_MRL.build()).queue();
      return false;
    }
    this.joinVoiceChannel(executor, arguments);
    executor.getChannel().sendMessageEmbeds(DiscordLocale.CONNECT_VC_EMBED.build()).queue();
    return true;
  }

  private void joinVoiceChannel(
      @NotNull final Message executor, final String @NotNull [] arguments) {
    final MusicManager manager = this.getBot().getMusicManager();
    manager.joinVoiceChannel();
    manager.addTrack(executor.getChannel(), arguments[0]);
  }

  private boolean invalidArguments(final String @Nullable [] arguments) {
    return arguments == null || arguments.length < 1;
  }
}
