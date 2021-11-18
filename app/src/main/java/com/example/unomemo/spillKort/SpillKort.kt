package com.example.unomemo.spillKort

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.spilldata.Vanskelighetsgrad
import com.example.unomemo.R
import com.example.unomemo.databinding.FragmentSpillKortBinding
import com.example.unomemo.media.Media
import com.example.unomemo.spilldata.KortInfo
import com.example.unomemo.spilldata.START_FLAGS
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class SpillKort : Fragment() {

    private lateinit var recyc: RecyclerView
    private lateinit var root : ConstraintLayout
    private lateinit var nextBtn: Button
    private var _binding : FragmentSpillKortBinding?=null
    private val binding get()= _binding!!

    private lateinit var gameAdappeter :SpillBrettAdatper

    private var gameSize : Vanskelighetsgrad = Vanskelighetsgrad.ENKEL

    private lateinit var media: Media

    private val db =  FirebaseFirestore.getInstance()

    private val auth = FirebaseAuth.getInstance()

    private var imagesURL : List<String>? ? = null;

    companion object{
        private const val  TAG  = "SpillKort"
    }

    interface Click{
        fun onCardClicked(pos :Int)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        _binding = FragmentSpillKortBinding.inflate(inflater,container,false)


        recyc =  binding.spillKortRecycler//every  recycler view have to core components one is the adapter  and layoutManager(measures  and positions  item view)
        root =  binding.constraintLayout
        nextBtn =  binding.gVidere

        setupGameAgain()
        setHasOptionsMenu(true)



        return  binding.root
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {


        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.option_menu,menu)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

      /*  when(gameSize) {
            DifLEvel.EASY -> R.id.itemEasy
            DifLEvel.MEDIUM -> R.id.itemMedium
            DifLEvel.HARD -> R.id.itemHard
        }

        item.setOnMenuItemClickListener {
            gameSize =  when(item.itemId){
                R.id.itemEasy -> DifLEvel.EASY
                R.id.itemMedium -> DifLEvel.MEDIUM

                else -> DifLEvel.HARD
            }
            setupGameAgain()
            true
        }

       */


        return  super.onOptionsItemSelected(item)

    }

    private fun setupGameAgain() {
    media = Media(gameSize,imagesURL)
        recyc.addItemDecoration(DefualtDecorator(15,15))
        gameAdappeter =  SpillBrettAdatper(requireContext(),gameSize,
            media.cardINFO, object : Click{
                override fun onCardClicked(pos: Int) {

                    updateAdappter(pos)
                }

            })

        recyc.adapter = gameAdappeter  // adapter provides a binding for the data set to the views of the recycler view
        recyc.setHasFixedSize(true)
        recyc.layoutManager = GridLayoutManager(this.context,gameSize.getBredde())
    }




    /**
     * oppdaterer adaptere
     */
    private fun updateAdappter(pos:Int){


        if (media.matchPares()) {
            return
        }
        if (media.isUpCards(pos)){
            return
        }
        if(media.flipCards(pos)){
            nextBtn.setOnClickListener {
                //Snackbar.make(root,"Bli ferdig med Spillet",Snackbar.LENGTH_SHORT).show()
                if(!media.matchPares()){
                    Snackbar.make(root,"Bli ferdig med Spillet",Snackbar.LENGTH_SHORT).show()
                }
                writeMovesToDB()
                it.findNavController().navigate(SpillKortDirections.actionSpillKortToLeaderboardFragment2())


            }

        }

        gameAdappeter.run { notifyDataSetChanged() }
    }





    fun writeMovesToDB(){

        val dbCollection =  db.collection("LeaderBoard").document()
        val bruker = auth.currentUser
        val data =  hashMapOf(
            "navn" to bruker?.email.toString(),
            "poengsum" to media.getAmountMoves(),
            "uid" to "${UUID.randomUUID()}"
        )

        dbCollection.set(data).addOnSuccessListener { documentReference ->
            Log.d(TAG,"DocumentSnapshot written with ID:${documentReference}")
        }
            .addOnFailureListener{
                Log.w(TAG,"Error adding Document")

            }
    }



}