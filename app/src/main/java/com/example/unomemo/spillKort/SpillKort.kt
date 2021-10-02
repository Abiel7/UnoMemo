package com.example.unomemo.spillKort

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.R
import com.example.unomemo.databinding.FragmentSpillKortBinding


class SpillKort : Fragment() {
    private lateinit var recyc: RecyclerView
    private lateinit var parTextView: TextView
    private lateinit var bevegTextView: TextView

    private var _binding : FragmentSpillKortBinding?=null
    private val binding get()= _binding!!

    private lateinit var spilAdapter :SpillBrettAdatper

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSpillKortBinding.inflate(inflater,container,false)


        recyc =  binding.spillKortRecycler//every  recycler view have to core compo8nents one is the adapter  and layoutManager(measures  and positions  item view)
        parTextView =  binding.antallPar
        bevegTextView =  binding.antallBevegelser

        spilAdapter =  SpillBrettAdatper(requireContext(),8)

        recyc.adapter = spilAdapter  // adapter provides a binding for the data set to the views of the recycler view
        recyc.setHasFixedSize(true)
        recyc.layoutManager = GridLayoutManager(this.context,2)

        setHasOptionsMenu(true)

        return  binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.option_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }


}