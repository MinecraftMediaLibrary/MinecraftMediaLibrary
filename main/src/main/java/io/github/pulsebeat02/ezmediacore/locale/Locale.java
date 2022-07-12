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
package io.github.pulsebeat02.ezmediacore.locale;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

import static io.github.pulsebeat02.ezmediacore.locale.MessageLoader.key;

public interface Locale {
  NullComponent SYSTEM_DIAGNOSTIC = toNullComponent(key("ezmediacore.log.diagnostic"));
  NullComponent PACKET_HANDLER = toNullComponent(key("ezmediacore.log.handle"));
  NullComponent LOOKUP_CACHE = toNullComponent(key("ezmediacore.log.cache"));
  NullComponent EVENT_REGISTRATION = toNullComponent(key("ezmediacore.log.event"));
  NullComponent FILE_CREATION = toNullComponent(key("ezmediacore.log.folder"));
  NullComponent DEPENDENCY_LOADING = toNullComponent(key("ezmediacore.log.dependency"));
  NullComponent NATIVE_LOG_REGISTRATION = toNullComponent(key("ezmediacore.log.native"));
  NullComponent PLAYER_RELEASE = toNullComponent(key("ezmediacore.log.release"));
  NullComponent PLAYER_PAUSE = toNullComponent(key("ezmediacore.log.pause"));
  NullComponent PLAYER_RESUME = toNullComponent(key("ezmediacore.log.resume"));
  NullComponent SERVER_SOFTWARE = toNullComponent(key("ezmediacore.log.server.software"));
  NullComponent PACKET_COMPRESSION = toNullComponent(key("ezmediacore.log.packet.compression"));
  NullComponent SPOTIFY_AUTHENTICATION = toNullComponent(key("ezmediacore.log.spotify.auth"));
  UniComponent<String> UNSUPPORTED_SERVER = key("ezmediacore.error.server")::formatted;
  UniComponent<String> DEPENDENCY_LOAD = key("ezmediacore.log.dependency.load")::formatted;
  BiComponent<String, String> PLAYER_START = key("ezmediacore.log.start")::formatted;
  BiComponent<String, Path> BINARY_PATHS = key("ezmediacore.log.binary")::formatted;
  TriComponent<String, String, Boolean> SERVER_INFO = key("ezmediacore.log.http")::formatted;
  QuadComponent<String, String, String, String> SYSTEM_INFO =
      key("ezmediacore.log.system")::formatted;
  HeptaComponent<String, String, Boolean, Path, Path, Path, Path> PLUGIN_INFO =
      key("ezmediacore.log.plugin")::formatted;

  @Contract(pure = true)
  static @NotNull NullComponent toNullComponent(@NotNull final String key) {
    return () -> key;
  }

  @FunctionalInterface
  interface NullComponent {

    String build();
  }

  @FunctionalInterface
  interface UniComponent<A0> {

    String build(A0 arg0);
  }

  @FunctionalInterface
  interface BiComponent<A0, A1> {

    String build(A0 arg0, A1 arg1);
  }

  @FunctionalInterface
  interface TriComponent<A0, A1, A2> {

    String build(A0 arg0, A1 arg1, A2 arg2);
  }

  @FunctionalInterface
  interface QuadComponent<A0, A1, A2, A3> {

    String build(A0 arg0, A1 arg1, A2 arg2, A3 arg3);
  }

  @FunctionalInterface
  interface PentaComponent<A0, A1, A2, A3, A4> {

    String build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4);
  }

  @FunctionalInterface
  interface HexaComponent<A0, A1, A2, A3, A4, A5> {

    String build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5);
  }

  @FunctionalInterface
  interface HeptaComponent<A0, A1, A2, A3, A4, A5, A6> {

    String build(A0 arg0, A1 arg1, A2 arg2, A3 arg3, A4 arg4, A5 arg5, A6 arg6);
  }
}
