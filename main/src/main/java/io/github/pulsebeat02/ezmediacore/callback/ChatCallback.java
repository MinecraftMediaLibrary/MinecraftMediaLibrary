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
package io.github.pulsebeat02.ezmediacore.callback;

import io.github.pulsebeat02.ezmediacore.MediaLibraryCore;
import io.github.pulsebeat02.ezmediacore.callback.entity.NamedEntityString;
import io.github.pulsebeat02.ezmediacore.dimension.Dimension;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ChatCallback extends FrameCallback implements ChatCallbackDispatcher {

  private final NamedEntityString character;

  ChatCallback(
      @NotNull final MediaLibraryCore core,
      @NotNull final Viewers viewers,
      @NotNull final Dimension dimension,
      @NotNull final NamedEntityString character,
      @NotNull final DelayConfiguration delay) {
    super(core, viewers, dimension, delay);
    this.character = character;
  }

  @Override
  public void process(final int[] data) {
    final long time = System.currentTimeMillis();
    if (time - this.getLastUpdated() >= this.getDelayConfiguration().getDelay()) {
      this.setLastUpdated(time);
      final Dimension dimension = this.getDimensions();
      final int width = dimension.getWidth();
      final int height = dimension.getHeight();
      for (int y = 0; y < height; ++y) {
        int before = -1;
        final StringBuilder msg = new StringBuilder();
        for (int x = 0; x < width; ++x) {
          final int rgb = data[width * y + x];
          if (before != rgb) {
            msg.append(ChatColor.of("#" + "%08x".formatted(rgb).substring(2)));
          }
          msg.append(this.character.getName());
          before = rgb;
        }
        for (final Player player : this.getWatchers().getPlayers()) {
          player.sendMessage(msg.toString());
        }
      }
    }
  }

  @Override
  public @NotNull NamedEntityString getChatCharacter() {
    return this.character;
  }
}
