package com.example.unomemo

import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.unomemo.databinding.FragmentEntryBinding


/**
 * A simple [Fragment] subclass.
 * Use the [EntryPointFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EntryPointFragment : Fragment() {
    lateinit var binding : FragmentEntryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_entry, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        //TODO fix full screen
        // flagger bars og hånterer depricated flagging
        //fullscreen(window = requireActivity().window, 0)

        Handler(Looper.getMainLooper()).postDelayed({
            var navcontroller =  findNavController()
            navcontroller.navigate(R.id.action_entryPointFragment_to_loginFragment)
            //TODO remove fragment from the stack
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