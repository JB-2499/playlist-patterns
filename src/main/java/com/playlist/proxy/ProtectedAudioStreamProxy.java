package com.playlist.proxy;

import com.playlist.core.AccessDeniedException;
import com.playlist.core.Subscription;
import com.playlist.core.Track;
import java.util.function.Supplier;
import java.util.Arrays;

public class ProtectedAudioStreamProxy implements AudioStream {

  private final Track track;
  private byte[] cachedBytes;
  private AudioStream realStream;
  private final Subscription plan;
  private final Supplier<AudioStream> loader;

  public ProtectedAudioStreamProxy(Track track, Subscription plan, Supplier<AudioStream> loader) {
    if (track == null || plan == null || loader == null) {
      throw new IllegalArgumentException("track, plan e loader não podem ser nulos");
    }

    this.track = track;
    this.plan = plan;
    this.loader = loader;
  }

  public ProtectedAudioStreamProxy(Track track, Subscription plan) {
    this(track, plan, () -> new RemoteAudioStream(track));
  }

  public boolean isLoaded() {
    return realStream != null;
  }

  @Override
  public String getTrackId() {
    return this.track.id();
  }

  @Override
  public byte[] readBytes() {
    if (plan == Subscription.FREE && track.premium()) {
      throw new AccessDeniedException("faixa premium não disponível para plano gratuito");
    }

    if (cachedBytes != null) {
      return Arrays.copyOf(cachedBytes, cachedBytes.length);
    }

    if (realStream == null) {
      realStream = loader.get();
    }

    cachedBytes = realStream.readBytes();
    return Arrays.copyOf(cachedBytes, cachedBytes.length);
  }
}