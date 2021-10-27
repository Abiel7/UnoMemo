package com.example.unomemo

import android.os.*
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.unomemo.databinding.FragmentEntryBinding


/**
 * [Fragment] subklasse.
 *  @author svein
 */
class EntryPointFragment() : Fragment() {
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
            navController.popBackStack(R.id.gamemenuFragment,false)
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