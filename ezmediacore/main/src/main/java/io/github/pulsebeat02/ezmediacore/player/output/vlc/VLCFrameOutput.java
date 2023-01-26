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
package io.github.pulsebeat02.ezmediacore.player.output.vlc;

import com.google.common.base.Preconditions;
import io.github.pulsebeat02.ezmediacore.player.output.ConsumableOutput;
import io.github.pulsebeat02.ezmediacore.player.output.vlc.sout.VLCTranscoderOutput;
import org.jetbrains.annotations.NotNull;

public final class VLCFrameOutput implements VLCOutput {

  private ConsumableOutput output;

  private VLCTranscoderOutput transcoder;
  private VLCStandardOutput standard;

  public VLCFrameOutput() {}

  public void setTranscoder(@NotNull final VLCTranscoderOutput transcoder) {
    this.transcoder = transcoder;
  }

  public void setStandard(@NotNull final VLCStandardOutput standard) {
    this.standard = standard;
  }

  @Override
  public @NotNull ConsumableOutput getResultingOutput() {
    return this.output;
  }

  @Override
  public void setOutput(@NotNull final Object output) {
    Preconditions.checkArgument(output instanceof ConsumableOutput);
    this.output = (ConsumableOutput) output;
  }

  @Override
  public String toString() {
    return ":sout=#%s:%s".formatted(this.transcoder, this.standard);
  }
}
