package com.packrat.revolution


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.packrat.revolution.databinding.FragmentListsBinding

/**
 * A simple [Fragment] subclass.
 */
class ListsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val lists = mutableListOf(arguments?.getString("usernameArg"), "List 2", "List 3")
        val binding = DataBindingUtil.inflate<FragmentListsBinding>(inflater,
            R.layout.fragment_lists,container,false)
        for (list in lists) {
            val button = Button(binding.listsContainer.context)
            button.text = list
            button.setOnClickListener { view : View ->
                view.run{
                    var l = button.text.toString()
                    findNavController().navigate(R.id.action_listsFragment_to_itemsFragment, bundleOf("listArg" to list))
                }
            }
            binding.listsContainer.addView(button)
        }
        return binding.root
    }


}
