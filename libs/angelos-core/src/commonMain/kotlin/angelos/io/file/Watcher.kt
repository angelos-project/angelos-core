/**
 * Copyright (c) 2022 by Kristoffer Paulsson <kristoffer.paulsson@talenten.se>.
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
package angelos.io.file

interface Watcher {

    fun poll() {
        // First poll in POSIX
        val event = PollAction(1,2)
        suspend {
            when {
                streams.contains(event.descriptor) -> onStream(event.descriptor) {

                }
                files.contains(event.descriptor) -> onFile(event.descriptor) {

                }
                sockets.contains(event.descriptor) -> onSocket(event.descriptor) {

                }
                else -> throw WatcherException("Watcher with descriptor doesn't exist")
            }
        }
    }

    fun register(watchable: Watchable) {
        when (watchable.type) {
            WatchableTypes.STREAM -> streams.add(watchable)
            WatchableTypes.FILE -> files.add(watchable)
            WatchableTypes.SOCKET -> sockets.add(watchable)
            else -> throw WatcherException("Watcher can not handle type ${watchable.type}")
        }
        descriptors.add(watchable.descriptor)
    }

    fun remove(descriptor: Int) {
        when {
            streams.contains(descriptor) -> streams.remove(descriptor)
            files.contains(descriptor) -> files.remove(descriptor)
            sockets.contains(descriptor) -> sockets.remove(descriptor)
        }
        descriptors.remove(descriptor)
    }
    fun remove(watchable: Watchable) = remove(watchable.descriptor)

    suspend fun onStream(descriptor: Int, s: suspend (watchable: Watchable) -> Unit) { s(streams.get(descriptor)!!) }
    suspend fun onFile(descriptor: Int, f: suspend (it: Watchable) -> Unit)  { f(streams.get(descriptor)!!) }
    suspend fun onSocket(descriptor: Int, s: suspend (it: Watchable) -> Unit)  { s(streams.get(descriptor)!!) }

    companion object {
        private val descriptors = mutableListOf<Int>()

        protected val streams = object: WatchableContainer {
            override val descriptors: MutableMap<Int, Watchable> = mutableMapOf()
        }
        protected val files = object: WatchableContainer {
            override val descriptors: MutableMap<Int, Watchable> = mutableMapOf()
        }
        protected val sockets = object: WatchableContainer {
            override val descriptors: MutableMap<Int, Watchable> = mutableMapOf()
        }
    }
}