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
package io.github.pulsebeat02.ezmediacore.dependency;

import io.github.pulsebeat02.ezmediacore.Logger;
import io.github.pulsebeat02.ezmediacore.MediaLibraryCore;
import io.github.pulsebeat02.ezmediacore.sneaky.ThrowingConsumer;
import io.github.pulsebeat02.ezmediacore.utility.DependencyUtils;
import io.github.pulsebeat02.ezmediacore.utility.FileUtils;
import io.github.pulsebeat02.ezmediacore.utility.HashingUtils;
import io.github.pulsebeat02.ezmediacore.utility.PathUtils;
import io.github.slimjar.relocation.RelocationRule;
import io.github.slimjar.relocation.facade.JarRelocatorFacadeFactory;
import io.github.slimjar.relocation.facade.ReflectiveJarRelocatorFacadeFactory;
import io.github.slimjar.resolver.data.Repository;
import io.github.slimjar.resolver.mirrors.SimpleMirrorSelector;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public final class ArtifactInstaller {

  private static final List<RelocationRule> RELOCATION_RULES;

  static {
    RELOCATION_RULES =
        Arrays.stream(RelocationPaths.values())
            .map(RelocationPaths::getRelocation)
            .collect(Collectors.toList());
  }

  private final JarRelocatorFacadeFactory factory;
  private final ExecutorService service;
  private final Set<Path> jars;
  private final Set<String> hashes;
  private final Path dependencyFolder;
  private final Path relocatedFolder;
  private final Path hashFile;

  public ArtifactInstaller(@NotNull final MediaLibraryCore core)
      throws ReflectiveOperationException, URISyntaxException, NoSuchAlgorithmException,
      IOException {
    this.dependencyFolder = core.getDependencyPath();
    this.relocatedFolder = this.dependencyFolder.resolve("relocated");
    this.factory =
        ReflectiveJarRelocatorFacadeFactory.create(
            this.relocatedFolder,
            Collections.singleton(
                new Repository(new URL(SimpleMirrorSelector.DEFAULT_CENTRAL_MIRROR_URL))));
    this.service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    this.jars = new HashSet<>();
    this.hashes = new HashSet<>();
    this.hashFile = this.dependencyFolder.resolve(".relocated-hash");
  }

  public void start() {
    try {
      this.createFiles();
      this.download();
      this.relocate();
      this.writeHashes();
      this.load();
      this.delete();
      this.shutdown();
    } catch (final IOException | InterruptedException e) {
      Logger.info("A serious exception occurred during dependency instantiation!");
      e.printStackTrace();
    }
  }

  private void createFiles() throws IOException {
    Files.createDirectories(this.dependencyFolder);
    Files.createDirectories(this.relocatedFolder);
    if (!FileUtils.createIfNotExists(this.hashFile)) {
      try (final Stream<String> stream = Files.lines(this.hashFile)) {
        stream.forEach(this.hashes::add);
      }
    }
  }

  private void download() throws InterruptedException {
    this.service.invokeAll(
        Arrays.stream(DependencyInfo.values())
            .filter(this::requiresDownload)
            .map(path -> Executors.callable(() -> this.downloadDependency(path), null))
            .collect(Collectors.toSet()));
  }

  private void relocate() throws InterruptedException {

    Logger.info(
        "Preparing to relocate %d dependencies (%s)".formatted(this.jars.size(), this.jars));

    this.service.invokeAll(
        this.jars.stream()
            .map(path -> Executors.callable(() -> this.relocateFile(path, this.factory)))
            .collect(Collectors.toList()));
  }

  private void writeHashes() throws IOException {

    Logger.info("Recording relocated JAR hashes");

    try (final PrintWriter writer = new PrintWriter(Files.newBufferedWriter(this.hashFile))) {
      this.hashes.forEach(writer::println);
    }
  }

  private void load() throws IOException {

    Logger.info("Preparing to load %d dependencies (%s)".formatted(this.jars.size(), this.jars));

    final Set<Path> invalid =
        Files.walk(this.relocatedFolder, 1)
            .filter(Files::isRegularFile)
            .filter(path -> PathUtils.getName(path).endsWith(".jar"))
            .filter(path -> !this.hashes.contains(HashingUtils.getHash(path)))
            .collect(Collectors.toSet());

    if (!invalid.isEmpty()) {
      for (final Path p : invalid) {
        Logger.warn(
            "Dependency %s has an invalid hash! Downloading dependency again...".formatted(p));
        this.redownload(p, this.getDependency(p).orElseThrow(AssertionError::new));
      }
    }

    new JarLoader(
        Files.walk(this.relocatedFolder, 1)
            .filter(Files::isRegularFile)
            .filter(path -> PathUtils.getName(path).endsWith(".jar"))
            .collect(Collectors.toList()))
        .inject();
  }

  private void delete() {

    Logger.info(
        "Preparing to delete %d stale dependencies (%s)".formatted(this.jars.size(), this.jars));

    this.jars.forEach(ThrowingConsumer.unchecked(Files::delete));

    this.jars.clear();
  }

  private void shutdown() {
    this.service.shutdownNow();
  }

  private boolean requiresDownload(@NotNull final DependencyInfo dependency) {
    try (final Stream<Path> paths = Files.walk(this.relocatedFolder, 1)) {
      return paths
          .filter(Files::isRegularFile)
          .noneMatch(
              path -> {
                final String name = PathUtils.getName(path);
                return name.contains(dependency.getArtifact())
                    && name.contains(dependency.getVersion());
              });
    } catch (final IOException e) {
      e.printStackTrace();
    }
    return false;
  }

  @NotNull
  private Path downloadDependency(@NotNull final DependencyInfo dependency) {

    final Repositories resolution = dependency.getResolution();
    final String artifact = dependency.getArtifact();
    final String path = this.dependencyFolder.toString();

    Optional<Path> file;
    try {
      switch (resolution) {
        case MAVEN -> {
          Logger.info("Checking Maven Central Repository for %s".formatted(artifact));
          file = Optional.of(DependencyUtils.downloadMavenDependency(dependency, path));
        }
        case JITPACK -> {
          Logger.info("Checking Jitpack Central Repository for %s".formatted(artifact));
          file = Optional.of(DependencyUtils.downloadJitpackDependency(dependency, path));
        }
        default -> throw new IllegalStateException(
            "Specified Repository URL Doesn't Exist! (Not Maven/Jitpack)");
      }
    } catch (final IOException e) {
      file = Optional.empty();
      Logger.info(
          "Cannot Resolve Dependency! (Artifact: %s | Repository URL: %s)".formatted(artifact,
              resolution.getUrl()));
      e.printStackTrace();
    }

    if (file.isPresent()) {
      final Path p = file.get();
      if (DependencyUtils.validateDependency(p, dependency)) {
        Logger.info("SHA1 Hash for File %s Succeeded!".formatted(file));
        this.jars.add(p);
      } else {
        try {
          Logger.info(
              "SHA1 Hash for File %s Failed! Downloading the Dependency Again...".formatted(file));
          Files.delete(p);
          return this.downloadDependency(dependency);
        } catch (final IOException e) {
          e.printStackTrace();
        }
      }
    } else {
      /*
      This should never happen. But if it does, it just retries
      Yeah that is a mess, but its a placeholder for the warning.
       */
      return this.downloadDependency(dependency);
    }

    return file.get();
  }

  private void relocateFile(
      @NotNull final Path input, @NotNull final JarRelocatorFacadeFactory factory) {
    final File result = this.relocatedFolder.resolve(input.getFileName()).toFile();
    try {
      factory.createFacade(input.toFile(), result, RELOCATION_RULES).run();
      this.hashes.add(HashingUtils.getHash(result.toPath()));
    } catch (final IOException
        | InvocationTargetException
        | IllegalAccessException
        | InstantiationException e) {
      e.printStackTrace();
    }
  }

  private void redownload(@NotNull final Path invalid, @NotNull final DependencyInfo dependency)
      throws IOException {
    Files.delete(invalid);
    this.relocateFile(this.downloadDependency(dependency), this.factory);
  }

  @NotNull
  private Optional<DependencyInfo> getDependency(@NotNull final Path jar) {
    for (final DependencyInfo dependency : DependencyInfo.values()) {
      final String name = PathUtils.getName(jar);
      if (name.contains(dependency.getArtifact()) && name.contains(dependency.getVersion())) {
        return Optional.of(dependency);
      }
    }
    return Optional.empty();
  }
}
