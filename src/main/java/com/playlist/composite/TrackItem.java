package com.playlist.composite;

import com.playlist.core.Track;
import java.util.List;
import java.util.ArrayList;

public class TrackItem implements MediaItem {

  private final Track track;

  public TrackItem(Track track) {
    if (track == null) {
      throw new IllegalArgumentException("track não pode ser nula");
    }
    this.track = track;
  }

  public Track getTrack() {
    return this.track;
  }

  @Override
  public String getName() {
    return this.track.title();
  }

  @Override
  public int getDurationSeconds() {
    return this.track.durationSeconds();
  }

  @Override
  public int getTrackCount() {
    return 1;
  }

  @Override
  public List<Track> flatten() {
    List<Track> tracks = new ArrayList<>();
    tracks.add(this.track);
    return tracks;
  }
}