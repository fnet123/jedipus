package com.fabahaba.jedipus.concurrent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

public class StaticDelayFunction implements LongFunction<Duration> {

  private final Duration[] delays;
  private final Duration maxDelay;

  private StaticDelayFunction(final List<Duration> delayDurations, final Duration maxDelay) {

    this.delays = delayDurations.toArray(new Duration[delayDurations.size()]);
    this.maxDelay = maxDelay;
  }

  public static StaticDelayFunction create(final LongFunction<Duration> delayFunction,
      final Duration maxDelay) {

    final List<Duration> delayDurations = new ArrayList<>();

    for (long retry = 0;; retry++) {

      final Duration delay = delayFunction.apply(retry);
      if (delay.compareTo(maxDelay) >= 0) {
        break;
      }

      delayDurations.add(delay);
    }

    return new StaticDelayFunction(delayDurations, maxDelay);
  }

  @Override
  public Duration apply(final long retry) {

    return retry >= delays.length ? maxDelay : delays[(int) retry];
  }
}
