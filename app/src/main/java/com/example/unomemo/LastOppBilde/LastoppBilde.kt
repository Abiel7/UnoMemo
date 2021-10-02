package com.example.unomemo.LastOppBilde

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.R
import com.example.unomemo.databinding.FragmentLastoppBildeFragementBinding
import com.example.unomemo.databinding.FragmentSpillKortBinding


class LastoppBilde : Fragment() {
   private lateinit var rvVelgBilder :RecyclerView
   private lateinit var giSpillNavn :EditText
   private lateinit var btnLagre : Button

   private val antallBilderValgt =  mutableListOf<Uri>() //liste som skal inneholde bilder som er valgt fra brukerens telefon

    private var _binding : FragmentLastoppBildeFragementBinding?=null
    private val binding get()= _binding!!

    private lateinit var lastopp: VelgBildeAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        _binding = FragmentLastoppBildeFragementBinding.inflate(inflater,container,false)


        rvVelgBilder =  binding.rvLastopp
        giSpillNavn =  binding.giSpillNavn//view.findViewById(R.id.giSpillNavn)
        btnLagre =  binding.btnLagre


        val grad =  resources.getStringArray(R.array.Nivå)
        val adapterArray= ArrayAdapter(requireContext(),R.layout.dropdown_item,grad)
        binding.autoCompleteTextView.setAdapter(adapterArray)

        // liste av bilder brukeren har valgt
        lastopp =  VelgBildeAdapter(requireContext(),antallBilderValgt,8)
        rvVelgBilder.adapter = lastopp
        rvVelgBilder.layoutManager =  GridLayoutManager(this.context,2)

        return binding.root

    }


}