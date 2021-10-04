package com.example.unomemo

import android.app.TaskStackBuilder
import android.os.Bundle
import android.os.DropBoxManager
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.unomemo.databinding.FragmentEntryBinding


/**
 * A simple [Fragment] subclass.
 * Use the [EntryPointFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EntryPointFragment : Fragment() {
    lateinit var binding : FragmentEntryBinding
    lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_entry, container, false)
        navController = findNavController(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //TODO fix full screen
        // flagger bars og hånterer depricated flagging
        //fullscreen(window = requireActivity().window, 0)
        //TODO animation of logo when ending, navigate to @id/loginFragment
        Handler(Looper.getMainLooper()).postDelayed({
            navController = findNavController(this)
            navController.navigate(EntryPointFragmentDirections.actionEntryPointFragmentToKatgoryFragment(true))
        },3000)

        super.onCreate(savedInstanceState)
    }



/*
    @Suppress("DEPRECATION")
    private fun fullscreen(window: Window, systemBarsBehavior: Int ){

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.R){
            var controller = window.insetsController
            controller?.hide((statusBars() and  navigationBars()) or navigationBars() or statusBars() )
            controller?.systemBarsBehavior = systemBarsBehavior
        }
        else{
            window.setFlags(
                FLAG_FULLSCREEN,
                FLAG_FULLSCREEN
            )
        }
    }
*/

}