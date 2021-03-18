package me.nosaka.kotlin.playground

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import me.nosaka.kotlin.playground.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var viewModel: MainViewModel

    private var jobFlow: Job? = null

    private var jobSharedFlow: Job? = null

    private var jobStateFlow: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.doCancelFlow.setOnClickListener {
            doCancelFlow()
        }
        binding.doCancelSharedFlow.setOnClickListener {
            doCancelSharedFlow()
        }
        binding.doCancelStateFlow.setOnClickListener {
            doCancelStateFlow()
        }
        collect()
    }


    private fun doCancelFlow() {
        lifecycleScope.launch {
            jobFlow?.cancelAndJoin()
            jobFlow?.start()
        }
    }

    private fun doCancelSharedFlow() {
        lifecycleScope.launch {
            jobSharedFlow?.cancelAndJoin()
            jobSharedFlow?.start()
        }
    }

    private fun doCancelStateFlow() {
        lifecycleScope.launch {
            jobStateFlow?.cancelAndJoin()
            jobStateFlow?.start()
        }
    }

    private fun collect() {
        jobFlow = lifecycleScope.launchWhenStarted {
            Log.d("Flow", "Thread:=${Thread.currentThread().name}")
            viewModel.testFlow.collect {
                withContext(Dispatchers.Main) {
                    binding.flowValue.text = it
                }
            }
        }
        jobSharedFlow = lifecycleScope.launchWhenStarted {
            Log.d("SharedFlow", "Thread:=${Thread.currentThread().name}")
            viewModel.testSharedFlow.collect {
                withContext(Dispatchers.Main) {
                    binding.flowSharedValue.text = it
                }
            }
        }
        jobStateFlow = lifecycleScope.launchWhenStarted {
            Log.d("StateFlow", "Thread:=${Thread.currentThread().name}")
            viewModel.testStateFlow.collect {
                withContext(Dispatchers.Main) {
                    binding.stateFlowdValue.text = it
                }
            }
        }
    }
}
