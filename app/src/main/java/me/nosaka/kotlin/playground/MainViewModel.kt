package me.nosaka.kotlin.playground

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainViewModel constructor(
    application: Application
) : AndroidViewModel(application) {

    val testChannel: ReceiveChannel<Int>

    val testFlow: Flow<Int>

    val testSharedFlow: SharedFlow<Int>

    var testStateFlow: MutableStateFlow<Int>

    init {
        testChannel = initChannel()
        testFlow = initFlow()
        testSharedFlow = initSharedFlow()
        testStateFlow = initStateFlow()
    }

    private fun initChannel(): ReceiveChannel<Int> {
        return viewModelScope.produce {
            var i = 0
            while (true) {
                this.offer(i)
                i++
                delay(TimeUnit.SECONDS.toMillis(1))
            }
        }
    }

    private fun initFlow(): Flow<Int> {
        return flow {
            var i = 0
            while (true) {
                emit(i)
                i++
                delay(TimeUnit.SECONDS.toMillis(1))
            }
        }
    }

    private fun initSharedFlow(): SharedFlow<Int> {
        val flow = MutableSharedFlow<Int>()
        viewModelScope.launch(Dispatchers.IO) {
            var i = 0
            while (true) {
                flow.emit(i)
                i++
                delay(TimeUnit.SECONDS.toMillis(1))
            }
        }
        return flow
    }

    private fun initStateFlow(): MutableStateFlow<Int> {
        val flow = MutableStateFlow(0)
        viewModelScope.launch(Dispatchers.IO) {
            var i = 0
            while (true) {
                flow.emit(i)
                i++
                delay(TimeUnit.SECONDS.toMillis(1))
            }
        }
        return flow
    }

}
