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
        return "To start the game click on one of the images in the game menu. \n" +
                "The goal when playing is to find the pair of each card. \n" +
                "When every pair has been releaved click the button at the bottom, and you will see the leaderbaord. \n" +
                "The score is based on how few cards you had to turn to find each matching pair. \n" +
                "\n" +
                "Good luck!"
    }
}