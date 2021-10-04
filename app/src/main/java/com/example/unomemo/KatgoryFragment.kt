package com.example.unomemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.databinding.FragmentKatgoryBinding
import com.example.unomemo.kategori.KategoriListeAdapter
import kotlin.properties.Delegates


class KatgoryFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil
            .inflate<FragmentKatgoryBinding>(
                inflater,
                R.layout.fragment_katgory,
                container,
                false
            )

        val recyclerView: RecyclerView = binding.kategoriRecyclerView
        val listAdapter = KategoriListeAdapter()

        recyclerView.adapter = listAdapter
        val layoutManager = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = layoutManager
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args:KatgoryFragmentArgs by navArgs()
        var navControl = findNavController(this)

        if(!args.splashScreenShowed){
            navControl.navigate(KatgoryFragmentDirections.actionKatgoryFragmentToEntryPointFragment())
        }
        if(!args.selfNavigated) {
            navControl.navigate(KatgoryFragmentDirections.actionKatgoryFragmentSelf(true))
            navControl.popBackStack(R.id.katgoryFragment, true)
        }
    }

}