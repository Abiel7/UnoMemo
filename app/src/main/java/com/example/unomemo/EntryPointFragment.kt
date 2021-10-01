package com.example.unomemo

import android.app.Activity
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.ThemeUtils
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
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
        super.onViewCreated(view, savedInstanceState)

        binding.buttonToLoginActivity.setOnClickListener{
            findNavController().navigate(R.id.action_entryPointFragment_to_loginFragment)
        }
        binding.skipGroup.setOnClickListener {
            findNavController().navigate(R.id.action_entryPointFragment_to_katgoryFragment)
        }
    }

}