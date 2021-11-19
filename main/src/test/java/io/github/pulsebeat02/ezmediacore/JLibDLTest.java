package io.github.pulsebeat02.ezmediacore;

import io.github.pulsebeat02.ezmediacore.jlibdl.JLibDL;
import io.github.pulsebeat02.ezmediacore.player.MrlConfiguration;
import io.github.pulsebeat02.ezmediacore.utility.media.RequestUtils;
import java.io.IOException;
import java.util.List;

public class JLibDLTest {

  public static void main(final String[] args) {
    //    System.out.println(
    //
    // jLibDL.request("https://www.twitch.tv/shroud").getInfo().getFormats().get(0).getUrl());
    //    System.out.println(
    //        jLibDL
    //            .request("https://www.youtube.com/watch?v=5qap5aO4i9A")
    //            .getInfo()
    //            .getFormats()
    //            .get(0)
    //            .getUrl());
    // video -> https://youtu.be/DHYcRU50yM0
    // stream -> https://www.youtube.com/watch?v=5qap5aO4i9A
    final MrlConfiguration mrl = MrlConfiguration.ofMrl("https://youtu.be/DHYcRU50yM0");
    final boolean stream = RequestUtils.isStream(mrl);
    final List<MrlConfiguration> videoUrls = RequestUtils.getVideoURLs(mrl);
    final List<MrlConfiguration> audioUrls = RequestUtils.getAudioURLs(mrl);
    System.out.printf("Stream? %s%n", stream ? "YES" : "NO");
    System.out.println(videoUrls.get(0));
    System.out.println(audioUrls.get(0));
    System.out.println(RequestUtils.getResult(audioUrls.get(0).getMrl()));
  }
}
