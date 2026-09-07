package info.freelibrary.j2htmx.pico.comparison;

import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.Clock;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@Disabled
@BenchmarkOptions(callgc = false, benchmarkRounds = 20_000, warmupRounds = 200, concurrency = 2, clock = Clock.NANO_TIME)
public class RenderPerformanceComparisonTest {

    @Test
    public void j2htmlPerformance() throws Exception {
        TestJ2html.helloWorld();
        TestJ2html.fiveHundredEmployees();
        TestJ2html.macros();
        TestJ2html.multiplicationTable();
    }

    @Test
    public void velocityPerformance() throws Exception {
        TestVelocity.helloWorld();
        TestVelocity.fiveHundredEmployees();
        TestVelocity.macros();
        TestVelocity.multiplicationTable();
    }

}
