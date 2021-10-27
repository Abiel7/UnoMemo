package com.example.unomemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unomemo.databinding.FragmentGamemenuBinding
import com.example.unomemo.kategori.KategoriListeAdapter



/**
 * 
 * */
class GamemenuFragment() : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = DataBindingUtil
            .inflate<FragmentGamemenuBinding>(
                inflater,
                R.layout.fragment_gamemenu,
                container,
                false
            )
        val recyclerView: RecyclerView = binding.kategoriRecyclerView
        val listAdapter = KategoriListeAdapter()

        recyclerView.adapter = listAdapter
        val layoutManager = GridLayoutManager(activity, 2)
        recyclerView.layoutManager = layoutManager
        return binding.root
    }


    override fun onAttach(context: Context) {
        //TODO check for login
        super.onAttach(context)
        var sharedPreferences = context
            .getSharedPreferences("prefs",Context.MODE_PRIVATE)
        var value = sharedPreferences.all["splashScreenPlayed"]
            .toString() ?.toBooleanStrictOrNull() ?: nullErr(
            sharedPreferences.all["splashScreenPlayed"].toString()
        )
        if(!value){
            findNavController(this).navigate(GamemenuFragmentDirections.actionGamemenuFragmentToEntryPointFragment())
            sharedPreferences.edit().putBoolean("splashScreenPlayed", true).commit()
        }

        //for debugging og feilsøking TODO sanitize
        Log.d(
            "KategoryFragment:onAttach; context.getSharedPrefrense().all['splashScreenPlayed']",
            "value: $value"
        )
    }

    override fun onResume() {
        super.onResume()
        //TODO check for login


        var sharedPreferences = requireContext()
            .getSharedPreferences("prefs",Context.MODE_PRIVATE)
        var showedLoginFirstTime = sharedPreferences.all["showedLoginFirstTime"]
            .toString() ?.toBooleanStrictOrNull() ?: nullErr(
                sharedPreferences.all["showedLoginFirstTime"].toString()
            )
        if (!showedLoginFirstTime) {
            findNavController(this).navigate(
                GamemenuFragmentDirections.actionGamemenuFragmentToLoginFragment()
            )
            sharedPreferences.edit().putBoolean("showedLoginFirstTime", true).commit()
        }

        //for debugging og feilsøking TODO sanitize
        Log.v(
            "KategoryFragment:onAttach; requireContext().getSharedPrefrense().all['showedLoginFirstTime']",
            "value: $showedLoginFirstTime"
        )

    }

    // for debugging og feilsøking
    private fun nullErr(s :String?): Boolean {
        Log.e(
            "KategoryFragment",
            "getSharedPrefrences()"
                    + " null is not a valid value in"
                    + " this context, try a valid key",
            Throwable("invalid key, input a key that is defined"))
        return true
    }

}