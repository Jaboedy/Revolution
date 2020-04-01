package com.packrat.revolution


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
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
        val binding = DataBindingUtil.inflate<FragmentListsBinding>(inflater,
            R.layout.fragment_lists,container,false)
        binding.listButton.setOnClickListener{ view : View ->
            view.findNavController().navigate(R.id.action_listsFragment_to_itemsFragment)
        }
        return binding.root
    }


}
