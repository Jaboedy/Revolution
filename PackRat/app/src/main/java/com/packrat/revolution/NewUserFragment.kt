package com.packrat.revolution


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.packrat.revolution.databinding.FragmentNewUserBinding

/**
 * A simple [Fragment] subclass.
 */
class NewUserFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentNewUserBinding>(inflater,
            R.layout.fragment_new_user,container,false)
        binding.newuserButton.setOnClickListener{ view : View ->
            view.findNavController().navigate(R.id.action_newUserFragment_to_listsFragment)
        }
        return binding.root
    }


}