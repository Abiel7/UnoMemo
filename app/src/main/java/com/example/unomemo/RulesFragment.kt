package com.example.unomemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.unomemo.databinding.FragmentRedigerBrukerBinding
import com.example.unomemo.databinding.FragmentRulesBinding

/*
* Author: Kim Andre Undal
* Dette fragmentet skal vise reglene til spillet
* */
class RulesFragment: Fragment(){
    var rules: String = ""
   lateinit var textView: TextView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rulesFragment = DataBindingUtil.inflate<FragmentRulesBinding>(
            inflater,
            R.layout.fragment_rules,
            container,
            false
        )
        rules = getRules()
        val textView = rulesFragment.rulesContainer.findViewById<TextView>(R.id.rules_tv)
        textView.text = rules
        return rulesFragment.root
    }

    @JvmName("getRules1")
    private fun getRules(): String {
        return "For å starte spillet klikk på et av bildene i spillmenyen.\n" +
                "Målet med spillet er å finne parene til hvert kort.\n" +
                "Når hvert par har blitt funnet klikk på knappen i bunn av skjermen, og du vil se leaderboardet. \n" +
                "Poengsummen er basert på hvor få ganger du måtte snu kortene for å finne det matchende paret. \n" +
                "\nLykke til!"
    }
}