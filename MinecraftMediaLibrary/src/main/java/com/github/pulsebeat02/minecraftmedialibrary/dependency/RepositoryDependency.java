/*
 * ============================================================================
 * Copyright (C) PulseBeat_02 - All Rights Reserved
 *
 * This file is part of MinecraftMediaLibrary
 *
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 *
 * Written by Brandon Li <brandonli2006ma@gmail.com>, 3/2/2021
 * ============================================================================
 */

package com.github.pulsebeat02.minecraftmedialibrary.dependency;

import org.jetbrains.annotations.NotNull;

public enum RepositoryDependency {

  /** VLCJ Maven Dependency */
  VLCJ("uk{}co{}caprica", "vlcj", "4{}6{}0"),

  /** VLCJ Natives Maven Dependency */
  VLCJ_NATIVES("uk{}co{}caprica", "vlcj-natives", "4{}1{}0"),

  /** Youtube Downloader Maven Dependency */
  YOUTUBE_DOWNLOADER("com{}github{}sealedtx", "java-youtube-downloader", "2{}4{}6"),

  /** Jave Core Maven Dependency */
  JAVE_CORE("ws{}schild", "jave-core", "3{}0{}1"),

  /** Apache Commons Compression Maven Dependency */
  COMMONS_COMPRESSION("org{}apache{}commons", "commons-compress", "1{}20"),

  /** Compression Maven Dependency */
  COMPRESSION("com{}github{}PulseBeat02", "jarchivelib", "master-SNAPSHOT"),

  /** Compression XZ Maven Dependency */
  XZ("org{}tukaani", "xz", "1{}0"),

  /** ASM Maven Dependency */
  ASM("org{}ow2{}asm", "asm", "9{}1"),

  /** ASM Commons Maven Dependency */
  ASM_COMMONS("org{}ow2{}asm", "asm-commons", "9{}1"),

  /** JNA Maven Dependency */
  JNA("net{}java{}dev{}jna", "jna", "5{}7{}0");

  //  JAVACV("org{}bytedeco", "javacv-platform", "1{}5{}4");

  private final String group;
  private final String artifact;
  private final String version;

  RepositoryDependency(
      @NotNull final String group, @NotNull final String artifact, @NotNull final String version) {
    this.group = group.replaceAll("\\{}", ".");
    this.artifact = artifact.replaceAll("\\{}", ".");
    this.version = version.replaceAll("\\{}", ".");
  }

  /**
   * Gets group.
   *
   * @return the group
   */
  public String getGroup() {
    return group;
  }

  /**
   * Gets artifact.
   *
   * @return the artifact
   */
  public String getArtifact() {
    return artifact;
  }

  /**
   * Gets version.
   *
   * @return the version
   */
  public String getVersion() {
    return version;
  }
}
