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
package io.github.pulsebeat02.ezmediacore.callback.video;

import static com.google.common.base.Preconditions.checkNotNull;

import io.github.pulsebeat02.ezmediacore.EzMediaCore;
import io.github.pulsebeat02.ezmediacore.callback.DelayConfiguration;
import io.github.pulsebeat02.ezmediacore.callback.VideoCallback;
import io.github.pulsebeat02.ezmediacore.callback.Viewers;
import rewrite.dimension.Dimension;
import io.github.pulsebeat02.ezmediacore.nms.PacketHandler;
import io.github.pulsebeat02.ezmediacore.player.PlayerControls;
import io.github.pulsebeat02.ezmediacore.player.VideoPlayer;


public abstract class FrameCallback implements VideoCallback {

  private final EzMediaCore core;
  private final DelayConfiguration delay;
  private final Dimension dimension;
  private final Viewers viewers;
  private long lastUpdated;

  public FrameCallback(
       final EzMediaCore core,
       final Viewers viewers,
       final Dimension dimension,
       final DelayConfiguration delay) {
    checkNotNull(core, "EzMediaCore cannot be null!");
    checkNotNull(viewers, "Viewers cannot be null!");
    checkNotNull(dimension, "Dimension cannot be null!");
    checkNotNull(delay, "Delay cannot be null!");
    this.core = core;
    this.viewers = viewers;
    this.dimension = dimension;
    this.delay = delay;
  }

  @Override
  public void preparePlayerStateChange(
       final VideoPlayer player,  final PlayerControls status) {}

  public  DelayConfiguration getDelayConfiguration() {
    return this.delay;
  }

  public long getLastUpdated() {
    return this.lastUpdated;
  }

  public void setLastUpdated(final long lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  @Override
  public  EzMediaCore getCore() {
    return this.core;
  }

  public  PacketHandler getPacketHandler() {
    return this.core.getHandler();
  }

  @Override
  public  Viewers getWatchers() {
    return this.viewers;
  }

  @Override
  public  Dimension getDimensions() {
    return this.dimension;
  }
}
