package pl.balif.test.vavr.collection;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Thread)
public class TestMethods {


    @State(Scope.Thread)
    public static class TabState {
        private Long[] tab;
        @Param({"10","100","1000","10000","100000"})
        int tabSize;

        @Setup(Level.Trial)
        public void
        initialize() {
            tab = createTab();
        }

        private Long[] createTab() {
            Random r = new Random();
            Long[] tab = new Long[tabSize];
            for (int i = 0; i < tab.length; i++) {
                tab[i] = System.currentTimeMillis() + r.nextInt(100);
            }
            return tab;
        }

        public Long[] getTab() {
            return tab;
        }
    }

    @Benchmark
    public Object testVavrLListCrateMetod(TabState tab, Blackhole blackhole) {
        io.vavr.collection.List<Long> vavrListCreate = null;
        vavrListCreate = io.vavr.collection.List.of(tab.getTab());
        blackhole.consume(vavrListCreate.peek());
        return vavrListCreate;
    }

    @Benchmark
    public Object testJavaLList(TabState tab, Blackhole blackhole) {
        List<Long> javaLList = new LinkedList<>();
        for (Long v : tab.getTab()) {
            javaLList.add(v);
        }
        blackhole.consume(((LinkedList<Long>) javaLList).peek());
        return javaLList;
    }

    @Benchmark
    public Object testVavrLList(TabState tab, Blackhole blackhole) {
        io.vavr.collection.List<Long> vavrList = io.vavr.collection.List.empty();
        for (Long v : tab.getTab()) {
            vavrList = vavrList.push(v);
        }
        blackhole.consume(vavrList.peek());
        return vavrList;
    }


}
