package angelos.sys

import kotlin.test.Test

class BenchmarkTest {

    @Test
    fun measure() {
        println(Benchmark.measure {
            val data = mutableListOf<String>()
            for(idx in 0..50_000){
                data.add("Number $idx")
            }
        })
    }
}