package com.packrat.revolution


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.packrat.revolution.databinding.FragmentAddCollectionBinding

/**
 * A simple [Fragment] subclass.
 */
class AddCollectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val user = User(arguments?.getString("usernameArg")!!, arguments?.getString("authTokenArg")!!)
        val binding = DataBindingUtil.inflate<FragmentAddCollectionBinding>(inflater,
            R.layout.fragment_add_collection,container,false)
        // Inflate the layout for this fragment
        binding.createCollectionButton.setOnClickListener {view : View ->
            view.run {
                val dc = DatabaseConnection()
                dc.createCollection(user.AuthToken, binding.textboxNewCollectionName.text.toString(), binding.textboxNewCollectionDesc.text.toString())
                findNavController().navigate(R.id.action_addCollectionFragment_to_listsFragment, bundleOf("usernameArg" to user.Username, "authTokenArg" to user.AuthToken))
            }

        }
        return binding.root
    }


}
