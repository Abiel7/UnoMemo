package com.example.unomemo.spillKort

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.spilldata.Vanskelighetsgrad
import com.example.unomemo.R
import com.example.unomemo.databinding.FragmentSpillKortBinding
import com.example.unomemo.spilldata.KortInfo
import com.example.unomemo.spilldata.START_FLAGS


class SpillKort : Fragment() {

    private lateinit var recyc: RecyclerView
    private lateinit var parTextView: TextView
    private lateinit var bevegTextView: TextView

    private var _binding : FragmentSpillKortBinding?=null
    private val binding get()= _binding!!

    private lateinit var spilAdapter :SpillBrettAdatper

    private var brettSto : Vanskelighetsgrad = Vanskelighetsgrad.ENKEL

    private var kortInfo:List<KortInfo>? =null
   // private val _kortInfo get() =  kortInfo

    private var index :Int? =  null

    private var antPar =  0;

    companion object{
        private const val  TAG  = "SpillKort"
    }

    interface Click{
        fun onCardClicked(pos :Int)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSpillKortBinding.inflate(inflater,container,false)


        recyc =  binding.spillKortRecycler//every  recycler view have to core compo8nents one is the adapter  and layoutManager(measures  and positions  item view)
        parTextView =  binding.antallPar
        bevegTextView =  binding.antallBevegelser

        recyc.addItemDecoration(DefualtDecorator(15,15))

        val flags:List<Int> =  START_FLAGS.shuffled().take(antallPar())

        val shuffleIMG :List<Int> = (flags + flags).shuffled()

        // lag ny liste av shuffleIMG og send det in til kort info data
        kortInfo = shuffleIMG.map { KortInfo(it) }

        spilAdapter =  SpillBrettAdatper(requireContext(),brettSto,kortInfo!!, object : Click{
            override fun onCardClicked(pos: Int) {
                Log.i(TAG,"$pos")
                oppAdapter(pos)
            }

        })

        recyc.adapter = spilAdapter  // adapter provides a binding for the data set to the views of the recycler view
        recyc.setHasFixedSize(true)
        recyc.layoutManager = GridLayoutManager(this.context,brettSto.getBredde())

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

        // 8 kort 4 svar
        // 12 kort  6  svar
        //16 kort 8 svar

    fun antallPar() : Int{
        return  brettSto.antallkort / 2
    }

    private fun oppAdapter(pos:Int){

        flipKort(pos)
        // informing the adapter about changes
        spilAdapter.run { notifyDataSetChanged() }
    }


    private fun flipKort(pos: Int){
        val kort  = kortInfo?.get(pos)

        if(index == null){
            // this means they were either  0 cards previosuly flipped over
                // or there were 2 cards previously flipped over and they are not matched
                    // trun them back again by seting isup to false
                        // and update the index to the postion taht is clicked
            restKort();
            index = pos;

        } else {
                check(index!!,pos)
            index=null;
        }
        kort!!.isUp = !kort.isUp
    }


    private fun check(pos1:Int,pos2:Int) :Boolean {
        val idIfno1  =  kortInfo!![pos1].id
        val idIfno2 = kortInfo!![pos2].id

        if(kortInfo!![pos1].id != kortInfo!![pos2].id){
            Log.i(TAG, "$idIfno1 $idIfno2")
            return false;
        }
        kortInfo!![pos1].isaMatch =  true;
        kortInfo!![pos2].isaMatch =  true;
        antPar++;
        print( "this is antal kort $antPar" )
        return true

    }


    private fun restKort() {
        for (i in kortInfo!! ){
            if(!i.isaMatch){
                i.isUp= false;
            }
        }
    }




}