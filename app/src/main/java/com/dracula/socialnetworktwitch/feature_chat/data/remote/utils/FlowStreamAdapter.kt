package com.dracula.socialnetworktwitch.feature_chat.data.remote.utils

import com.tinder.scarlet.Stream
import com.tinder.scarlet.StreamAdapter
import com.tinder.scarlet.utils.getRawType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber
import java.lang.reflect.Type


class FlowStreamAdapter<T> : StreamAdapter<T, Flow<T>> {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun adapt(stream: Stream<T>): Flow<T> {
        return callbackFlow {
            stream.start(object : Stream.Observer<T> {
                override fun onComplete() {
                    Timber.d("Flow Completed")
                    close()
                }

                override fun onError(throwable: Throwable) {
                    Timber.e("Exception: $throwable")
                    close(cause = throwable)
                }

                override fun onNext(data: T) {
                    Timber.d("onNext with data: $data")
                    if (!isClosedForSend) {
                        trySend(data).isSuccess
                    }
                }
            })
            awaitClose { }
        }
    }

    object Factory : StreamAdapter.Factory {
        override fun create(type: Type): StreamAdapter<Any, Any> {
            return when (type.getRawType()) {
                Flow::class.java -> FlowStreamAdapter()
                else -> throw IllegalStateException("Invalid stream adapter")
            }
        }

    }
}