/*
 * MIT License
 *
 * Copyright (c) 2023 Brandon Li
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
package io.github.pulsebeat02.deluxemediaplugin.command.video.output.video;

import static io.github.pulsebeat02.deluxemediaplugin.utility.nullability.ArgumentUtils.requiresPlayer;
import static io.github.pulsebeat02.ezmediacore.pipeline.output.DelayConfiguration.ofDelay;
import static io.github.pulsebeat02.ezmediacore.dimension.Dimension.ofDimension;
import static io.github.pulsebeat02.ezmediacore.player.SoundKey.ofSound;

import io.github.pulsebeat02.deluxemediaplugin.DeluxeMediaPlugin;
import io.github.pulsebeat02.deluxemediaplugin.command.video.ScreenConfig;
import io.github.pulsebeat02.ezmediacore.EzMediaCore;
import rewrite.callback.video.BlockHighlightCallback;
import rewrite.callback.video.BlockHighlightCallback.Builder;

import java.util.Collection;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DebugOutput extends VideoOutput {

  public DebugOutput() {
    super("DEBUG_HIGHLIGHTS");
  }

  @Override
  public boolean createVideoPlayer(
      @NotNull final DeluxeMediaPlugin plugin,
      @NotNull final ScreenConfig attributes,
      @NotNull final CommandSender sender,
      @NotNull final Collection<? extends Player> players) {

    if (requiresPlayer(plugin, sender)) {
      return false;
    }

    final VideoBuilder videoBuilder = this.createVideoBuilder(plugin, attributes, (Player) sender);
    attributes.setPlayer(videoBuilder.build());

    return true;
  }

  @NotNull
  private VideoBuilder createVideoBuilder(
      @NotNull final DeluxeMediaPlugin plugin,
      @NotNull final ScreenConfig attributes,
      @NotNull final Player player) {

    final EzMediaCore core = plugin.library();
    final BlockHighlightCallback.Builder builder =
        this.createBlockHighlightBuilder(attributes, player);

    final VideoBuilder videoBuilder = this.getBuilder(attributes);
    videoBuilder.dims(
        ofDimension(attributes.getResolutionWidth(), attributes.getResolutionHeight()));
    videoBuilder.audio(attributes.getAudioOutput().build(core));
    videoBuilder.video(builder.build(core));

    return videoBuilder;
  }

  @NotNull
  private BlockHighlightCallback.Builder createBlockHighlightBuilder(
      @NotNull final ScreenConfig attributes, @NotNull final Player player) {

    final Builder builder = VideoCallbackBuilder.blockHighlight();
    builder.location(player.getLocation());
    builder.dims(ofDimension(attributes.getResolutionWidth(), attributes.getResolutionHeight()));
    builder.delay(ofDelay(40L));

    return builder;
  }
}
