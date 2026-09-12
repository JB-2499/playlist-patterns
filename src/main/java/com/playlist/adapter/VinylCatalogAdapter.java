package com.playlist.adapter;

import com.playlist.adapter.external.LegacyVinylCatalog;
import com.playlist.core.Track;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class VinylCatalogAdapter implements TrackCatalog {

  private final LegacyVinylCatalog legacyCatalog;

  public VinylCatalogAdapter(LegacyVinylCatalog legacyCatalog) {
    if (legacyCatalog == null) {
      throw new IllegalArgumentException("O catálogo legado não pode ser nulo");
    }

    this.legacyCatalog = legacyCatalog;
  }

  @Override
  public List<Track> findAll() {
    List<Track> records =  new ArrayList<>();

    for (String record : legacyCatalog.fetchAllRecords()) {
      Track track = convertRecordToTrack(record);

      if (track != null) {
        records.add(track);
      }
    }

    return records;
  }

  @Override
  public Optional<Track> findById(String id) {
    if (id == null || id.isBlank()) {
      return Optional.empty();
    }

    String music = legacyCatalog.findRecordByCatalogNumber(id);
    if (music == null) {
      return Optional.empty();
    }

    Track track = convertRecordToTrack(music);
    return Optional.ofNullable(track);
  }

  public Track convertRecordToTrack(String record) {
    String[] registro = record.split("\\|");

    if (registro.length != 5) {
      return null;
    }

    String id = registro[0].trim();

    if (id.isBlank()) {
      return null;
    }

    String title = formatWord(registro[1]);

    String[] completeName = formatWord(registro[2]).split(", ");
    String artist = completeName[1] + " " + completeName[0];

    int duration;

    try {
        duration = Integer.parseInt(registro[3].trim());
    } catch (NumberFormatException e) {
        return null;
    }

    if (duration < 0) {
        return null;
    }

    duration = duration / 1000;

    boolean premium = (registro[4].trim().equalsIgnoreCase("Y")) ? true : false;

    return new Track(id, title, artist, duration, premium);
  }

  public String formatWord(String word) {
    String[] words = word.trim().toLowerCase().split("\\s+");
    StringBuilder formatted = new StringBuilder();

    for (String w : words) {
      if (!w.isBlank()) {
        formatted.append(Character.toUpperCase(w.charAt(0)))
                 .append(w.substring(1))
                 .append(" ");
      }
    }

    return formatted.toString().trim();
  }
}