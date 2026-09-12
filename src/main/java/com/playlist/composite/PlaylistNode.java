package com.playlist.composite;

import com.playlist.core.Track;
import java.util.List;
import java.util.ArrayList;

public class PlaylistNode implements MediaItem {

  private String name;
  private List<MediaItem> children = new ArrayList<>();

  public PlaylistNode(String name) {
    if (name == null || name.isEmpty()) {
      throw new IllegalArgumentException("O nome não pode estar vazio");
    }
    this.name = name;
  }

  public PlaylistNode add(MediaItem item) {
    if (item == null || item == this || (item instanceof PlaylistNode && ((PlaylistNode) item).contains(this))) {
      throw new IllegalArgumentException("item não pode ser nulo, a própria playlist ou conter a própria playlist");
    }

    this.children.add(item);

    return this;
  }

  public boolean remove(MediaItem item) {
    return children.remove(item);
  }

  public List<MediaItem> getChildren() {
    return List.copyOf(children);
  }

  public boolean contains(MediaItem item) {
    for (MediaItem child : children) {
      if (child == item) {
        return true;
      }
      if (child instanceof PlaylistNode && ((PlaylistNode) child).contains(item)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public String getName() {
    return this.name;
  }


  @Override
  public int getDurationSeconds() {
    int totalDuration = 0;

    for (MediaItem child : children) {
      totalDuration += child.getDurationSeconds();
    }

    return totalDuration;
  }

  @Override
  public int getTrackCount() {
    int totalCount = 0;

    for (MediaItem child : children) {
      totalCount += child.getTrackCount();
    }
    
    return totalCount;
  }

  @Override
  public List<Track> flatten() {
    List<Track> tracks = new ArrayList<>();

    for (MediaItem child : children) {
      tracks.addAll(child.flatten());
    }

    return tracks;
  }
}