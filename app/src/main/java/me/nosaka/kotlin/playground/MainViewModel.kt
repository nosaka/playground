package me.nosaka.kotlin.playground

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


class MainViewModel constructor(
    application: Application
) : AndroidViewModel(application) {

    val testFlow: Flow<String>

    val testSharedFlow: SharedFlow<String>

    var testStateFlow: MutableStateFlow<String>

    sealed class CounterMsg {
        object IncCounter : CounterMsg() // one-way message to increment counter
        class GetCounter(val response: CompletableDeferred<Int>) :
            CounterMsg() // a request with reply
    }

    fun counterActor() {
        viewModelScope.launch {
            actor<CounterMsg> {
                var counter = 0 // actor state
                for (msg in channel) { // iterate over incoming messages
                    when (msg) {
                        is CounterMsg.IncCounter -> counter++
                        is CounterMsg.GetCounter -> msg.response.complete(counter)
                    }
                }
            }
        }
    }

    init {
        testFlow = initFlow()
        testSharedFlow = initSharedFlow()
        testStateFlow = initStateFlow()
    }

    // Cold
    private fun initFlow(): Flow<String> {
        return flow {
            var i = 0
            while (true) {
                this.emit(i.toString())
                i++
                delay(TimeUnit.SECONDS.toMillis(1))
            }
        }
    }

    // Hot
    private fun initSharedFlow(): SharedFlow<String> {
        val flow = MutableSharedFlow<String>()
        viewModelScope.launch(Dispatchers.IO) {
            var i = 0
            while (true) {
                flow.emit(i.toString())
                i++
                delay(TimeUnit.SECONDS.toMillis(1))
            }
        }
        return flow
    }

    // Hot
    private fun initStateFlow(): MutableStateFlow<String> {
        val flow = MutableStateFlow("")
        viewModelScope.launch(Dispatchers.IO) {
            var i = 0
            while (true) {
                flow.emit(i.toString())
                i++
                delay(TimeUnit.SECONDS.toMillis(1))
            }
        }
        return flow
    }

}
