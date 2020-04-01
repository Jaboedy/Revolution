package com.packrat.revolution


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.packrat.revolution.databinding.FragmentLoginBinding

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(inflater,
            R.layout.fragment_login,container,false)
        binding.loginButton.setOnClickListener{ view : View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_listsFragment)
        }
        binding.createAccountButton.setOnClickListener{ view : View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_newUserFragment)
        }
        return binding.root
    }


}
