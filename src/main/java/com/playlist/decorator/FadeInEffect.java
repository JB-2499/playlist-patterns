package com.playlist.decorator;
import java.util.Locale;

public final class FadeInEffect extends AudioEffect {

  private final int sampleCount;

  public FadeInEffect(AudioTrack wrapped, int sampleCount) {
    super(wrapped);
    this.sampleCount = sampleCount;
  }

  @Override
  protected String describe() {
    return String.format(Locale.ROOT, "fadeIn(%d)", sampleCount);
  }

  @Override
  public double[] getSamples() {
      double[] samples = wrapped.getSamples();

      if (sampleCount <= 0) {
          return samples;
      }

      double[] newSamples = new double[samples.length];
      for (int i = 0; i < samples.length; i++) {
          if (i < sampleCount) {
              newSamples[i] = samples[i] * ((double) i / sampleCount);
          } else {
              newSamples[i] = samples[i];
          }
      }

      return newSamples;
  }
}