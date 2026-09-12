package com.playlist.facade;

import com.playlist.adapter.TrackCatalog;
import com.playlist.composite.PlaylistNode;
import com.playlist.composite.TrackItem;
import com.playlist.core.Subscription;
import com.playlist.core.Track;
import com.playlist.core.TrackNotFoundException;
import com.playlist.decorator.AudioTrack;
import com.playlist.decorator.FadeInEffect;
import com.playlist.decorator.RawAudioTrack;
import com.playlist.decorator.VolumeEffect;
import com.playlist.proxy.ProtectedAudioStreamProxy;

import java.util.HashMap;
import java.util.Map;

public class PlaylistFacade {

  private final TrackCatalog catalog;
  private final Subscription subscription;
  private final Map<String, ProtectedAudioStreamProxy> proxies = new HashMap<>();

  public PlaylistFacade(TrackCatalog catalog, Subscription subscription) {
    if (catalog == null || subscription == null) {
      throw new IllegalArgumentException("catalog e subscription não podem ser nulos");
    }

    this.catalog = catalog;
    this.subscription = subscription;
  }

  public PlaylistNode buildLibrary(String name) {
    PlaylistNode playlist = new PlaylistNode(name);

    for (Track track : catalog.findAll()) {
      playlist.add(new TrackItem(track));
    }

    return playlist;
  }

  public byte[] listen(String trackId) {
    Track track = catalog.findById(trackId)
        .orElseThrow(() -> new TrackNotFoundException(trackId));

    ProtectedAudioStreamProxy proxy = proxies.get(trackId);

    if (proxy == null) {
      proxy = new ProtectedAudioStreamProxy(track, subscription);
      proxies.put(trackId, proxy);
    }

    return proxy.readBytes();
  }

  public AudioTrack preview(String trackId, double volume, int fadeInSamples) {
    Track track = catalog.findById(trackId)
        .orElseThrow(() -> new TrackNotFoundException(trackId));

    byte[] bytes = listen(trackId);

    double[] samples = new double[bytes.length];

    for (int i = 0; i < bytes.length; i++) {
      samples[i] = bytes[i] / 128.0;
    }

    AudioTrack raw = new RawAudioTrack(track.title(), samples);
    AudioTrack withVolume = new VolumeEffect(raw, volume);

    return new FadeInEffect(withVolume, fadeInSamples);
  }
}