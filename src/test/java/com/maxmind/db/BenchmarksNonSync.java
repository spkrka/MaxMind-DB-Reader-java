package com.maxmind.db;

import com.fasterxml.jackson.databind.JsonNode;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.annotations.Threads;
import org.openjdk.jmh.annotations.Warmup;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
@Warmup(iterations = 2, time = 2, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 4, time = 2, timeUnit = TimeUnit.SECONDS)
@Fork(value = 1, warmups = 0)
public class BenchmarksNonSync {

  private Reader reader;
  private InetAddress ipAddress;

  @Setup(Level.Trial)
  public void setup() throws IOException {
    reader = new Reader(new File("/usr/share/GeoIP2/GeoIP2-City.mmdb"), false);
    ipAddress = InetAddress.getByName("www.google.com");
  }

  @Benchmark
  @BenchmarkMode({Mode.AverageTime, Mode.Throughput, Mode.SampleTime})
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  @Threads(1)
  public JsonNode measureThreads_01() throws IOException {
    return reader.get(ipAddress);
  }

  @Benchmark
  @BenchmarkMode({Mode.AverageTime, Mode.Throughput, Mode.SampleTime})
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  @Threads(2)
  public JsonNode measureThreads_02() throws IOException {
    return reader.get(ipAddress);
  }

  @Benchmark
  @BenchmarkMode({Mode.AverageTime, Mode.Throughput, Mode.SampleTime})
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  @Threads(4)
  public JsonNode measureThreads_04() throws IOException {
    return reader.get(ipAddress);
  }

  @Benchmark
  @BenchmarkMode({Mode.AverageTime, Mode.Throughput, Mode.SampleTime})
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  @Threads(32)
  public JsonNode measureThreads_32() throws IOException {
    return reader.get(ipAddress);
  }

  @Benchmark
  @BenchmarkMode({Mode.AverageTime, Mode.Throughput, Mode.SampleTime})
  @OutputTimeUnit(TimeUnit.MICROSECONDS)
  @Threads(32)
  public JsonNode measureThreads_64() throws IOException {
    return reader.get(ipAddress);
  }

  @TearDown(Level.Trial)
  public void tearDown() throws Exception {
    reader.close();
  }
}
