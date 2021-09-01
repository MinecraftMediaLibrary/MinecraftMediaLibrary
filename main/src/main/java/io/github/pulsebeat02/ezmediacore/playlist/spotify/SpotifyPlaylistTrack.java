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
package io.github.pulsebeat02.ezmediacore.playlist.spotify;

import java.util.Date;
import org.jetbrains.annotations.NotNull;

public class SpotifyPlaylistTrack implements PlaylistTrack {

  private final com.wrapper.spotify.model_objects.specification.PlaylistTrack track;

  public SpotifyPlaylistTrack(
      @NotNull final com.wrapper.spotify.model_objects.specification.PlaylistTrack track) {
    this.track = track;
  }

  @Override
  public @NotNull Date getDateAdded() {
    return this.track.getAddedAt();
  }

  @Override
  public @NotNull User getWhoAdded() {
    return new SpotifyUser(this.track.getAddedBy());
  }

  @Override
  public boolean isLocal() {
    return this.track.getIsLocal();
  }

  @NotNull
  com.wrapper.spotify.model_objects.specification.PlaylistTrack getPlaylistTrack() {
    return this.track;
  }
}
