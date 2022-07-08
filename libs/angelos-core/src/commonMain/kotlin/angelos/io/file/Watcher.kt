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

import angelos.interop.Base
import org.angproj.io.sig.SigName

interface Watcher {

    suspend fun poll(sigName: SigName) {
        val event = Base.pollAction()
        println("${event.descriptor}, ${event.action}")
        when {
            files.contains(event.descriptor) -> descriptors[event.descriptor]?.forEach {
                onFile(event.descriptor, it) }
            sockets.contains(event.descriptor) -> descriptors[event.descriptor]?.forEach {
                onSocket(event.descriptor, it) }
            streams.contains(event.descriptor) -> descriptors[event.descriptor]?.forEach {
                onStream(event.descriptor, it) }
            else -> throw WatcherException("Watcher ${event.descriptor} doesn't exist")
        }
    }

    fun register(watchable: Watchable, vararg handlers: WatchableHandler) {
        if(descriptors.containsKey(watchable.descriptor))
            throw WatcherException("Watcher ${watchable.descriptor} already registered")

        when (watchable.type) {
            WatchableTypes.STREAM -> streams.add(watchable)
            WatchableTypes.FILE -> files.add(watchable)
            WatchableTypes.SOCKET -> sockets.add(watchable)
            else -> throw WatcherException("Watcher can not handle type ${watchable.type}")
        }
        descriptors[watchable.descriptor] = handlers.toList()
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

    suspend fun onStream(descriptor: Int, w: WatchableHandler) {
        w(streams.get(descriptor)?: throw WatcherException("Stream watchable $descriptor not registered"))
    }

    suspend fun onFile(descriptor: Int, w: WatchableHandler)  {
        w(files.get(descriptor)?: throw WatcherException("File watchable $descriptor not registered"))
    }

    suspend fun onSocket(descriptor: Int, w: WatchableHandler)  {
        w(sockets.get(descriptor)?: throw WatcherException("Socket watchable $descriptor not registered"))
    }

    companion object {
        private val descriptors = mutableMapOf<Int, List<WatchableHandler>>()

        protected val streams = object: WatchableContainer {
            override val descriptors: MutableMap<Int, Watchable> = mutableMapOf()

            override fun add(w: Watchable): Unit {
                Base.attachStream(w.descriptor)
                super.add(w)
            }
        }

        protected val files = object: WatchableContainer {
            override val descriptors: MutableMap<Int, Watchable> = mutableMapOf()

            override fun add(w: Watchable): Unit {
                // Attach to event queue in Base
                super.add(w)
            }
        }

        protected val sockets = object: WatchableContainer {
            override val descriptors: MutableMap<Int, Watchable> = mutableMapOf()

            override fun add(w: Watchable): Unit {
                Base.attachSocket(w.descriptor)
                super.add(w)
            }
        }
    }
}