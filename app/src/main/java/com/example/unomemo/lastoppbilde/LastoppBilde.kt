package com.example.unomemo.LastOppBilde

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.databinding.FragmentLastoppBildeFragementBinding

import com.example.unomemo.spilldata.Vanskelighetsgrad
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.example.unomemo.R
import com.example.unomemo.spillKort.DefualtDecorator


class LastoppBilde : Fragment() {
    private lateinit var rvChoseImages: RecyclerView
    private lateinit var gameName: EditText
    private lateinit var btnSave: Button


    // identifies where a particular resource lives list of images to be uploaded when  the user have chosen images
    private val chosenImages = mutableListOf<Uri>()

    private var _binding: FragmentLastoppBildeFragementBinding? = null
    private val binding get() = _binding!!

    lateinit var storage: FirebaseStorage

    private val db = Firebase.firestore
    private lateinit var imageLoaderAdapter: VelgBildeAdapter
    lateinit var gameSize: Vanskelighetsgrad
    var numberOfImages = -1

    interface ImageListener {
        fun onImageClick()
    }

    companion object {
        val TAG = "LastoppBilde"
        private val PICK_IMAGE_REQUEST = 22
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentLastoppBildeFragementBinding.inflate(inflater, container, false)
        rvChoseImages = binding.rvLastopp
        gameName = binding.giSpillNavn//view.findViewById(R.id.giSpillNavn)
        btnSave = binding.btnLagre
        gameSize = Vanskelighetsgrad.KRVENDE
        numberOfImages = gameSize.sumMuchOnGame()
        storage = Firebase.storage
        rvChoseImages.addItemDecoration(DefualtDecorator(15, 15))
        imageLoaderAdapter = VelgBildeAdapter(requireContext(), chosenImages, gameSize, object :
            ImageListener {
            override fun onImageClick() {
                imagePickerIntent()
            }
        })
        btnSave.setOnClickListener {
            uploadTofirebase()
        }
        rvChoseImages.adapter = imageLoaderAdapter
        rvChoseImages.layoutManager = GridLayoutManager(this.context, gameSize.getBredde())
        setHasOptionsMenu(true)

        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.option_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (gameSize) {
            Vanskelighetsgrad.ENKEL -> R.id.lett
            Vanskelighetsgrad.MIDDELS -> R.id.medium
            Vanskelighetsgrad.KRVENDE -> R.id.Vanskelig
        }

        gameSize = when (item.itemId) {
            R.id.lett -> Vanskelighetsgrad.ENKEL
            R.id.medium -> Vanskelighetsgrad.MIDDELS
            else -> Vanskelighetsgrad.KRVENDE
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val picUri = data?.data
            val cdata = data?.clipData
            if (cdata != null) {
                for (i in 0 until cdata.itemCount) {
                    val citem = cdata.getItemAt(i)
                    chosenImages.add(citem.uri)
                    if (chosenImages.size < numberOfImages) {
                        chosenImages.add(citem.uri)
                        println(chosenImages)
                    }
                }
            } else if (picUri != null) {
                chosenImages.add(picUri)
            }
            btnSave.isEnabled = true
            imageLoaderAdapter.notifyDataSetChanged()
        }
    }

    private fun imagePickerIntent() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Velg bilder "), PICK_IMAGE_REQUEST)
    }

    private fun showButton(): Boolean {
        if (chosenImages.size != numberOfImages) {
            return false
        }
        if (gameName.text.isBlank()) {
            return false
        }
        return true
    }

    fun uploadTofirebase() {
        var imagerRef = FirebaseStorage.getInstance().getReference("images/${gameName.text}")
        imagerRef.listAll()
            .addOnSuccessListener {
                for (i in chosenImages)
                    imagerRef.putFile(i)
                Toast.makeText(requireContext(), "file uploaded,", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "FAILED ,", Toast.LENGTH_LONG).show()

            }
    }
}