/**
 * Copyright (c) 2021 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
 *
 * This software is available under the terms of the MIT license. Parts are licensed
 * under different terms if stated. The legal terms are attached to the LICENSE file
 * and are made available on:
 *
 *      https://opensource.org/licenses/MIT
 *
 * SPDX-License-Identifier: MIT
 *
 * Contributors:
 *      Kristoffer Paulsson - initial implementation
 */
package angelos.io

import org.junit.Test

class FileTest {

    fun createFile(): File = File(RealPath("/",listOf("tmp"), PathSeparator.POSIX))
    fun createDir(): Dir = Dir(RealPath("/",listOf(), PathSeparator.POSIX))

    @Test
    fun getReadable() {
        kotlin.test.assertTrue(createFile().readable)
        /*createDir().walk().forEach{
            when (it) {
                is Dir -> println("${it.lastAccessed} - ${it.path}")
            }
        }*/
    }

    @Test
    fun getWritable() {
        kotlin.test.assertTrue(createFile().writable)
    }

    @Test
    fun getExecutable() {
        kotlin.test.assertTrue(createFile().executable)
    }
}