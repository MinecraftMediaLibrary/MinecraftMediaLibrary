package io.github.pulsebeat02.ezmediacore.callback.audio;

import io.github.pulsebeat02.ezmediacore.MediaLibraryCore;
import io.github.pulsebeat02.ezmediacore.callback.Viewers;
import org.jetbrains.annotations.NotNull;

public final class NullCallback extends DataCallback {

  NullCallback(@NotNull final MediaLibraryCore core, @NotNull final Viewers viewers) {
    super(core, viewers);
  }

  @Override
  public void process(final byte @NotNull [] data) {
    // do nothing
  }

  @Override
  public void close() throws Exception {
  }

  public static final class Builder extends AudioCallbackBuilder {

    public Builder() {}

    @Override
    public @NotNull AudioCallback build(@NotNull final MediaLibraryCore core) {
      final Viewers viewers = this.getViewers();
      return new NullCallback(core, viewers);
    }
  }
}
