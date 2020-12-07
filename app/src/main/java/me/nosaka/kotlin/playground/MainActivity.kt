package me.nosaka.kotlin.playground

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nosaka.kotlin.playground.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    private var jobChannel: Job? = null

    private var jobFlow: Job? = null

    private var jobSharedFlow: Job? = null

    private var jobStateFlow: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.doTryChannel.setOnClickListener {
            doTryChannel()
        }
        binding.doCancelChannel.setOnClickListener {
            doCancelChannel()
        }
        binding.doTryFlow.setOnClickListener {
            doTryFlow()
        }
        binding.doCancelFlow.setOnClickListener {
            doCancelFlow()
        }
        binding.doSharedFlow.setOnClickListener {
            doTrySharedFlow()
        }
        binding.doCancelSharedFlow.setOnClickListener {
            doCancelSharedFlow()
        }
        binding.doStateFlow.setOnClickListener {
            doTryStateFlow()
        }
        binding.doStateFlow2.setOnClickListener {
            viewModel.testStateFlow.value = 0
        }
        binding.doCancelStateFlow.setOnClickListener {
            doCancelStateFlow()
        }

    }

    private fun doTryChannel() {
        jobChannel = lifecycleScope.launch(Dispatchers.IO) {
            viewModel.testChannel.consumeEach {
                withContext(Dispatchers.Main) {
                    binding.channelValue.text = "$it"
                }
            }
        }
    }

    private fun doCancelChannel() {
        jobChannel?.cancel()
    }

    private fun doTryFlow() {
        jobFlow = lifecycleScope.launch(Dispatchers.IO) {
            viewModel.testFlow.collect {
                withContext(Dispatchers.Main) {
                    binding.flowValue.text = "$it"
                }
            }
        }
    }

    private fun doCancelFlow() {
        jobFlow?.cancel()
    }

    private fun doTrySharedFlow() {
        jobSharedFlow = lifecycleScope.launch(Dispatchers.IO) {
            viewModel.testSharedFlow.collect {
                withContext(Dispatchers.Main) {
                    binding.flowSharedValue.text = "$it"
                }
            }
        }
    }

    private fun doCancelSharedFlow() {
        jobSharedFlow?.cancel()
    }

    private fun doTryStateFlow() {
        jobStateFlow = lifecycleScope.launch(Dispatchers.IO) {
            viewModel.testStateFlow.collect {
                withContext(Dispatchers.Main) {
                    binding.stateFlowdValue.text = "$it"
                }
            }
        }
    }

    private fun doCancelStateFlow() {
        jobStateFlow?.cancel()
    }
}
