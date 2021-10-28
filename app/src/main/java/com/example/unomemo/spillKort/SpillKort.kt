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

    private var cardINFO:List<KortInfo>? =null


    private var index :Int? =  null

    private var matches =  0

    private var moves = 0

    private val db =  FirebaseFirestore.getInstance()

    private val auth = FirebaseAuth.getInstance()

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
        val flags: List<Int> = START_FLAGS.shuffled().take(sumMuchOnGame())
        val shuffledItems :List<Int> = (flags + flags).shuffled()
        cardINFO = shuffledItems.map { KortInfo(it.hashCode()) }
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

        recyc.addItemDecoration(DefualtDecorator(15,15))
        gameAdappeter =  SpillBrettAdatper(requireContext(),gameSize,
            cardINFO!!, object : Click{
                override fun onCardClicked(pos: Int) {
                    Log.i(TAG," on click ${pos}, ${getMoves()}")
                    updateAdappter(pos)
                }

            })

        recyc.adapter = gameAdappeter  // adapter provides a binding for the data set to the views of the recycler view
        recyc.setHasFixedSize(true)
        recyc.layoutManager = GridLayoutManager(this.context,gameSize.getBredde())
    }


    /**
     *  finner ut hvor mange par det skal være basert på vankelighets grad brukeren har valgt
     *  så 8 kort 4 svar
    12 kort  6  svar
    16 kort 8 svar
     */
    fun sumMuchOnGame() : Int{
        return  gameSize.antallkort / 2

    }


    /**
     * oppdaterer adaptere
     */
    private fun updateAdappter(pos:Int){


        if (matchPares()) {
            return
        }
        if (isUpCards(pos)){
            return
        }
        if(flipCards(pos)){
            nextBtn.setOnClickListener {
                //Snackbar.make(root,"Bli ferdig med Spillet",Snackbar.LENGTH_SHORT).show()
                if(!matchPares()){
                    Snackbar.make(root,"Bli ferdig med Spillet",Snackbar.LENGTH_SHORT).show()
                }
                writeMovesToDB()
                it.findNavController().navigate(SpillKortDirections.actionSpillKortToLeaderboardFragment2())


            }

        }

        gameAdappeter.run { notifyDataSetChanged() }
    }


    /**
     * sjekker hvor mange kort som er flippet over
     * caser vi må sjekke
     * case 1 om 0 kort er flippet, flip kortene tilbake til start bildet
     * case 2 om 1 kort er flippet , flip kort og sjekk om den matcher med den neste kort som blir flippet
     * case 3 om 2 kort er flipet flip kortene tilbake til start bildet
     *
     */
    private fun flipCards(pos: Int):Boolean{
        this.moves++
        val kort  = cardINFO?.get(pos)
        var matchFound = false
        if(index == null){
            // case1 og case2
            resetCard()
            index = pos

            print(" index in first if statement  $index")

        } else {
            matchFound = check(index!!,pos)

            index=null
            print( "Else statment $index")
        }
        kort!!.isUp = !kort.isUp
        return matchFound
    }

    /**
     * metode som sjekker om to kort er likt basert på id
     */
    private fun check(pos1:Int,pos2:Int) :Boolean {
        val idIfno1  =  cardINFO!![pos1].id
        val idIfno2 = cardINFO!![pos2].id

        if(cardINFO!![pos1].id != cardINFO!![pos2].id){
            Log.i(TAG, "$idIfno1 $idIfno2")
            return false
        }
        cardINFO!![pos1].isaMatch =  true
        cardINFO!![pos2].isaMatch =  true
        matches++
        print( "this is antal kort $matches" )
        return true

    }

    /**
     * set kort tilbake til orginal bilde,
     *
     */
    private fun resetCard() {
        for (i in cardINFO!! ){
            if(!i.isaMatch){
                i.isUp= false
            }
        }
    }

    /**
     * hvor mange kort  er oppe
     */
    fun matchPares( ):Boolean{
        return matches == sumMuchOnGame()
    }

    /**
     * om en kort er flipped eller ikke
     */
    fun isUpCards(pos: Int): Boolean {
        return cardINFO?.get(pos)?.isUp == true
    }





    private fun getMoves():Int{
        return this.moves.times(2)
    }

    fun writeMovesToDB(){

        val dbCollection =  db.collection("LeaderBoard").document()
       var bruker = auth.currentUser
        val data =  hashMapOf(
            "navn" to bruker?.email.toString(),
            "poengsum" to getMoves(),
            "uid" to "${UUID.randomUUID()}"
        )

        dbCollection.set(data).addOnSuccessListener { documentReference ->
            Log.d(TAG,"DocumentSnapshot written with ID:${documentReference}")
        }
            .addOnFailureListener{
                Log.w(TAG,"Error adding Document")

            }
    }


    /*
    fun getBrukernavn(): String {
        var navn = ""
        val db = FirebaseFirestore.getInstance()
        db.collection("user")
            .get()
            .addOnSuccessListener { result ->
                var bruker = auth.currentUser
                if (bruker != null) {
                    for (document in result) {
                        if (bruker.email.toString() == document.data["id"].toString()) {
                            navn = document.data["navn"].toString()
                        }
                    }
                }
            }
        return navn
    }
*/

}