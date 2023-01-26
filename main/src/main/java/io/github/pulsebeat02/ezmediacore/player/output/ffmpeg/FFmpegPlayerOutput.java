package io.github.pulsebeat02.ezmediacore.player.output.ffmpeg;

import org.jetbrains.annotations.NotNull;

public final class FFmpegPlayerOutput implements FFmpegOutput {

  private final TcpFFmpegOutput tcp;
  private final FFmpegOutputConfiguration varied; // doesn't have to be stdout

  FFmpegPlayerOutput(@NotNull final TcpFFmpegOutput tcp, @NotNull final FFmpegOutputConfiguration varied) {
    this.tcp = tcp;
    this.varied = varied;
  }

  FFmpegPlayerOutput() {
    this(new TcpFFmpegOutput(), new StdoutFFmpegOutput());
  }

  public static @NotNull FFmpegPlayerOutput of() {
    return new FFmpegPlayerOutput();
  }

  public static @NotNull FFmpegPlayerOutput of(
      @NotNull final TcpFFmpegOutput tcp, @NotNull final FFmpegOutputConfiguration varied) {
    return new FFmpegPlayerOutput(tcp, varied);
  }

  public @NotNull TcpFFmpegOutput getTcp() {
    return this.tcp;
  }

  public @NotNull FFmpegOutputConfiguration getVariedOutput() {
    return this.varied;
  }

  /*
  No physical output as the ConfiguredOutput (TcpFFmpegOutput and StdoutFFmpegOutput) already
  have their respective output methods.
   */
  @Override
  public @NotNull Void getResultingOutput() {
    return null;
  }

  @Override
  public void setOutput(@NotNull final Object output) {}

  @Override
  public String toString() {
    return "-c:v libx264 -c:a aac -f tee -map 0 \"%s|%s\"".formatted(this.tcp, this.varied);
  }
}
