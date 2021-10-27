package com.example.unomemo.login


import android.os.Bundle
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

/**
 * @author svein
 *
 * */
class LoginFragment : Fragment() {

    private var auth:FirebaseAuth = Source(
        this.requireContext(),
        "UnoMemo",
        FirebaseOptions.fromResource(this.requireContext())!!
    ).firebaseAuth()

    private var storage = Source(
        this.requireContext(),
        "UnoMemo",
        FirebaseOptions.fromResource(this.requireContext())!!
    ).storage()

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

        /*
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                    usernameEditText.text.toString()
                    passwordEditText.text.toString()
            }
        }*/

        //midlertidig TODO slette og implimentere viewmodel med login methode og registrerings methode
        lateinit var email:String
        lateinit var pass:String
        // end

        //usernameEditText.addTextChangedListener(afterTextChangedListener)
        //passwordEditText.addTextChangedListener(afterTextChangedListener)
        passwordEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                email = usernameEditText.text.toString()
                pass = passwordEditText.text.toString()
            }
            false
        }

        loginButton.setOnClickListener {
                //loadingProgressBar.visibility = View.VISIBLE
                updateUiWithUser()
                auth.createUserWithEmailAndPassword(email,pass).let { taskCreate ->
                    if(taskCreate.isComplete && taskCreate.isSuccessful){
                        firebaseUser = auth.currentUser!!
                        storage.collection("users").document(firebaseUser.tenantId.toString()).set(
                            hashMapOf(
                                "username" to email.split("@")[0],
                                "email" to email
                            )).addOnFailureListener(requireActivity()) { e ->
                                Log.w("LoginFragment ", "Error user registering", e)
                            }.addOnSuccessListener(requireActivity()){
                              Log.d("LoginFragment ","sucsessfull user cration")
                            }
                    }else {
                        auth.signInWithEmailAndPassword(email,pass).let { taskLogin ->
                            if(taskLogin.isComplete && taskLogin.isSuccessful){
                                firebaseUser = auth.currentUser!!
                            }else{
                                TODO("fatal error")
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

    private fun login() {

    }

    private fun register() {

    }

}
