package com.example.unomemo.spillKort

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
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
import com.squareup.picasso.Picasso
import java.util.*

// <https://square.github.io/picasso/
class SpillKort : Fragment() {

    private lateinit var recyc: RecyclerView
    private lateinit var root : ConstraintLayout
    private lateinit var nextBtn: Button
    lateinit var imageListSearch : EditText
    private var _binding : FragmentSpillKortBinding?=null
    private val binding get()= _binding!!

    private lateinit var gameAdappeter :SpillBrettAdatper

    private var gameSize : Vanskelighetsgrad = Vanskelighetsgrad.ENKEL
    private var gameName :  String ? =  null
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
        imageListSearch =  binding.getImages

        imageListSearch.setOnClickListener {
            searchImages()
        }
        recyc.addItemDecoration(DefualtDecorator(R.dimen.rec_fragment_horizontal_margin,R.dimen.rec_fragment_vertical_margin))
        setupGameAgain()

        setHasOptionsMenu(true)



        return  binding.root
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {


        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.option_menu,menu)



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(gameSize) {
            Vanskelighetsgrad.ENKEL -> R.id.lett
            Vanskelighetsgrad.MIDDELS -> R.id.medium
            Vanskelighetsgrad.KRVENDE -> R.id.Vanskelig
        }

        gameSize =  when(item.itemId){
                R.id.lett -> Vanskelighetsgrad.ENKEL
                R.id.medium -> Vanskelighetsgrad.MIDDELS
                else -> Vanskelighetsgrad.KRVENDE
                }

        when(item.itemId) {
           R.id.startPåNytt ->{
               setupGameAgain()
           }
        }

            imagesURL =  null;
            setupGameAgain()





        return  super.onOptionsItemSelected(item)

    }

    private fun setupGameAgain() {
    media = Media(gameSize,imagesURL)

        gameAdappeter =  SpillBrettAdatper(requireContext(),gameSize,
            media.cardINFO!!, object : Click{
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

    private fun getImageUrl (imageurl : String) {
        db.collection("game").document(imageurl).get().addOnSuccessListener { doc ->
            val imageList = doc.toObject(ImageList :: class.java)
            if(imageList?.images== null) {
                // log det du ser etter finns ikke
            }

            val  cards  =  (imageList?.images?.size?.times(2))
            gameSize = cards?.let { Vanskelighetsgrad.getVal(it) }!!
            imagesURL =  imageList.images;
            gameName = imageurl;

            for (imagesURL in imageList.images) {
                // git repo  for rask hentign av bilder
                Picasso.get().load(imageurl).fetch()
            }
            setupGameAgain()

        }
    }



    private fun  searchImages () {
        imageListSearch = binding.getImages
        val text =  imageListSearch.text.toString().trim()
        getImageUrl(text)
    }
}