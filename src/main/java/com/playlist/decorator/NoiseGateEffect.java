package com.playlist.decorator;
import java.util.Locale;

public final class NoiseGateEffect extends AudioEffect {

  private final double treshold;

  public NoiseGateEffect(AudioTrack wrapped, double threshold) {
    super(wrapped);
    this.treshold = threshold;
  }

  @Override
  protected String describe() {
    return String.format(Locale.ROOT, "noiseGate(%.2f)", treshold);
  }

  @Override
  public double[] getSamples() {
    double[] samples = wrapped.getSamples();
    double[] newSamples = new double[samples.length];
    
    for (int i = 0; i < samples.length; i++) {
      if (Math.abs(samples[i]) < treshold) {
        newSamples[i] = 0.0;
      } else {
        newSamples[i] = samples[i];
      }
    }

    return newSamples;
  }
}