package com.playlist.decorator;
import java.util.Locale;

public final class VolumeEffect extends AudioEffect {

  private final double factor;

  public VolumeEffect(AudioTrack wrapped, double factor) {
    super(wrapped);
    this.factor = factor;
  }

  @Override
  protected String describe() {
    return String.format(Locale.ROOT, "volume(%.1f)", factor);
  }

  @Override
  public double[] getSamples() {
    double[] samples = wrapped.getSamples();
    double[] newSamples = new double[samples.length];

    for (int i = 0; i < samples.length; i++) {
      newSamples[i] = samples[i] * factor;
      if (newSamples[i] > 1.0) {
        newSamples[i] = 1.0;
      } else if (newSamples[i] < -1.0) {
        newSamples[i] = -1.0;
      }
    }
    return newSamples;
  }
}