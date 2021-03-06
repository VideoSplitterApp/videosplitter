package io.github.videosplitterapp

import android.os.Bundle
import android.util.Log
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.github.videosplitterapp.splitsManager.SplitsManager
import io.github.videosplitterapp.splitsManager.SplitsManagerImpl

abstract class BaseTopFragment : Fragment() {

    protected val splitsManager: SplitsManagerImpl by activityViewModels()

    companion object {
        private val TAG = BaseTopFragment::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(owner = this) {
            Log.d(TAG, "onBackPressedDispatcher() called")
            backPressed()
        }
    }

    abstract fun checkProgress()

    fun backPressed() {
        if (splitsManager.state.value == SplitsManager.State.SPLITTING) {
            MaterialAlertDialogBuilder(requireContext())
                .setMessage("Split in progress! Are you sure you sure want to abort this and go back?")
                .setNegativeButton("Check Progress") { _, _ ->
                    checkProgress()
                }
                .setPositiveButton("Abort") { _, _ ->
                    splitsManager.abort()
                    findNavController().navigateUp()
                }
                .show()
        } else {
            splitsManager.abort()
            findNavController().navigateUp()
        }
    }
}