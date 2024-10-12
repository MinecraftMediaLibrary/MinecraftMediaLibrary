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
package io.github.pulsebeat02.ezmediacore.player.buffered;

import io.github.pulsebeat02.ezmediacore.callback.DelayConfiguration;
import io.github.pulsebeat02.ezmediacore.callback.VideoCallback;
import io.github.pulsebeat02.ezmediacore.callback.Viewers;
import io.github.pulsebeat02.ezmediacore.callback.audio.AudioOutput;
import io.github.pulsebeat02.ezmediacore.callback.audio.AudioSource;
import rewrite.dimension.Dimension;
import io.github.pulsebeat02.ezmediacore.executor.ExecutorProvider;
import io.github.pulsebeat02.ezmediacore.player.FrameConfiguration;
import io.github.pulsebeat02.ezmediacore.player.MediaPlayer;
import io.github.pulsebeat02.ezmediacore.player.VideoBuilder;
import io.github.pulsebeat02.ezmediacore.player.input.Input;
import io.github.pulsebeat02.ezmediacore.player.input.InputParser;
import io.github.pulsebeat02.ezmediacore.player.input.JCodecMediaPlayerInputParser;
import io.github.pulsebeat02.ezmediacore.utility.structure.Pair;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import org.jcodec.api.FrameGrab;
import org.jcodec.api.JCodecException;
import org.jcodec.common.io.FileChannelWrapper;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Size;
import org.jetbrains.annotations.Contract;


/**
 * This video player sucks as it ignores all output configurations, which means that there is not
 * even audio at all...
 */
public final class JCodecMediaPlayer extends BufferedMediaPlayer {

  private FrameGrab grabber;

  JCodecMediaPlayer(
       final VideoCallback video,
       final AudioSource audio,
       final Viewers viewers,
       final Dimension pixelDimension,
       final BufferConfiguration buffer) {
    super(
        video,
        audio,
        viewers,
        pixelDimension,
        buffer,
        new JCodecMediaPlayerInputParser(video.getCore()));
  }

  @Override
  public void release() {
    super.release();
    this.forceStop();
    if (this.grabber != null) {
      this.grabber = null;
    }
  }

  @Override
  public void resume() {
    super.resume();
    this.initializePlayer(DelayConfiguration.ofDelay(this.getStart()));
    this.play();
  }

  @Override
  public void pause() {
    super.pause();
    this.forceStop();
    this.setStart(System.currentTimeMillis());
  }

  @Override
  public void start( final Input mrl,  final Object... arguments) {
    super.start(mrl, arguments);
    if (this.grabber == null) {
      this.initializePlayer(DelayConfiguration.DELAY_0_MS);
    }
    this.play();
    this.setStart(System.currentTimeMillis());
  }

  private void runPlayer() {
    CompletableFuture.runAsync(
        new JCodecFrameConsumer(this, this.grabber), ExecutorProvider.ENCODER_HANDLER);
  }

  private void play() {
    this.runPlayer();
    this.bufferFrames();
    this.startDisplayRunnable();
    this.startWatchdogRunnable();
  }

  public void initializePlayer( final DelayConfiguration configuration) {
    final Dimension dimension = this.getDimensions();
    try {
      this.grabber = this.getGrabber();
      this.grabber.seekToSecondPrecise(configuration.getDelay() / 1000.0F);
      this.grabber.getMediaInfo().setDim(new Size(dimension.getWidth(), dimension.getHeight()));
    } catch (final IOException | JCodecException e) {
      throw new AssertionError(e);
    }
  }

  private  FrameGrab getGrabber() throws IOException, JCodecException {
    return FrameGrab.createFrameGrab(this.parseInput());
  }

  private  FileChannelWrapper parseInput() throws FileNotFoundException {
    final InputParser parser = this.getInputParser();
    final Pair<Object, String[]> pair = parser.parseInput(this.getInput().getDirectVideoMrl());
    return NIOUtils.readableChannel(Path.of((String) pair.getKey()).toFile());
  }

  public static final class Builder extends VideoBuilder {

    private BufferConfiguration bufferSize = BufferConfiguration.BUFFER_15;

    public Builder() {}

    @Contract("_ -> this")
    @Override
    public Builder audio( final AudioOutput audio) {
      super.audio(audio);
      return this;
    }

    @Contract("_ -> this")
    @Override
    public Builder video( final VideoCallback callback) {
      super.video(callback);
      return this;
    }

    @Contract("_ -> this")
    @Override
    public Builder frameRate( final FrameConfiguration rate) {
      super.frameRate(rate);
      return this;
    }

    @Contract("_ -> this")
    @Override
    public Builder dims( final Dimension dims) {
      super.dims(dims);
      return this;
    }

    @Contract("_ -> this")
    public  Builder buffer( final BufferConfiguration bufferSize) {
      this.bufferSize = bufferSize;
      return this;
    }

    @Contract(" -> new")
    @Override
    public  MediaPlayer build() {
      super.init();
      final VideoCallback video = this.getVideo();
      final AudioSource audio = this.getAudio();
      return new JCodecMediaPlayer(
          video, audio, video.getWatchers(), this.getDims(), this.bufferSize);
    }
  }
}
