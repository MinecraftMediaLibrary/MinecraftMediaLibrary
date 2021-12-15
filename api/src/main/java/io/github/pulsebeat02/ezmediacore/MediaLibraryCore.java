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
package io.github.pulsebeat02.ezmediacore;

import io.github.pulsebeat02.ezmediacore.analysis.Diagnostic;
import io.github.pulsebeat02.ezmediacore.nms.PacketHandler;
import io.github.pulsebeat02.ezmediacore.playlist.spotify.SpotifyClient;
import java.nio.file.Path;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MediaLibraryCore {

  void initialize();

  void shutdown();

  @NotNull
  Plugin getPlugin();

  @NotNull
  PacketHandler getHandler();

  @NotNull
  Path getLibraryPath();

  @NotNull
  Path getHttpServerPath();

  @NotNull
  Path getDependencyPath();

  @NotNull
  Path getImagePath();

  @NotNull
  Path getAudioPath();

  @NotNull
  Path getVideoPath();

  @NotNull
  Path getFFmpegPath();

  void setFFmpegPath(@NotNull final Path path);

  @NotNull
  Path getRTPPath();

  void setRTPPath(@NotNull final Path path);

  @NotNull
  Path getVlcPath();

  void setVlcPath(@NotNull final Path path);

  @NotNull
  Diagnostic getDiagnostics();

  boolean isDisabled();

  @NotNull
  Listener getRegistrationHandler();

  void setRegistrationHandler(@NotNull final Listener listener);

  @NotNull
  LibraryLoader getLibraryLoader();

  @NotNull
  CoreLogger getLogger();

  @Nullable
  SpotifyClient getSpotifyClient();

  boolean isVLCSupported();

  void setVLCStatus(final boolean vlcStatus);
}
