package com.example.unomemo.login


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import com.example.unomemo.R
import com.example.unomemo.data.Source
import com.example.unomemo.databinding.FragmentLoginBinding
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app

/**
 * @author svein
 *
 * */
class LoginFragment : Fragment() {

    private lateinit var auth:FirebaseAuth

    private lateinit var storage:FirebaseFirestore

    private lateinit var firebaseUser: FirebaseUser
    private var _binding: FragmentLoginBinding? = null
    private lateinit var navController: NavController

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onStart() {
        super.onStart()
        if (auth.currentUser!=null) {
            firebaseUser = auth.currentUser!!
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        auth = Source(
            this.requireContext(),
            Firebase.app.name,
            FirebaseOptions.fromResource(this.requireContext())!!
        ).firebaseAuth()

        storage = Source(
                this.requireContext(),
        Firebase.app.name,
        FirebaseOptions.fromResource(this.requireContext())!!
        ).storage()

        navController = findNavController(this)
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val usernameEditText = binding.username
        val passwordEditText = binding.password
        val loginButton = binding.login
        //val loadingProgressBar = binding.loading
        //midlertidig TODO slette og implimentere viewmodel med login methode og registrerings methode

        lateinit var email:String
        lateinit var pass:String

        // end
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                email =  usernameEditText.text.toString()
                pass = passwordEditText.text.toString()
            }
        }

        usernameEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                email = usernameEditText.text.toString()
                pass = passwordEditText.text.toString()
            }
            false
        }

        loginButton.setOnClickListener {
                //loadingProgressBar.visibility = View.VISIBLE
                //updateUiWithUser()
            auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this.requireActivity()) { taskCreate ->
                    if (taskCreate.isSuccessful) {
                        updateUiWithUser()
                        firebaseUser = auth.currentUser!!
                        storage.collection("user").document(firebaseUser.uid).set(
                            hashMapOf(
                                "navn" to email.split("@")[0],
                                "id" to email
                            )
                        ).addOnFailureListener(requireActivity()) { e ->
                            Log.w("LoginFragment ", "Error user registering", e)
                        }.addOnSuccessListener(requireActivity()) {
                            Log.d("LoginFragment ", "successful user creation")
                        }
                        findNavController(this).popBackStack()
                    }else {
                        auth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(this.requireActivity()) { taskLogin ->
                                if (taskLogin.isSuccessful) {
                                    firebaseUser = auth.currentUser!!
                                    findNavController(this).popBackStack()
                                }
                            }
                    }
                }
        }



        binding.extendedFabSkip.setOnClickListener {
            findNavController(this).popBackStack(R.id.gamemenuFragment, false)
        }

    }

    private fun updateUiWithUser() {
        val welcome = getString(R.string.welcome)
        // TODO : initiate successful logged in experience
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, welcome, Toast.LENGTH_LONG).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        val appContext = context?.applicationContext ?: return
        Toast.makeText(appContext, errorString, Toast.LENGTH_LONG).show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
