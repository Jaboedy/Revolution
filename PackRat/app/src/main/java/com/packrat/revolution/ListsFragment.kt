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
        val user = User(arguments?.getString("usernameArg")!!, arguments?.getString("authTokenArg")!!)
        val binding = DataBindingUtil.inflate<FragmentListsBinding>(inflater,
            R.layout.fragment_lists,container,false)
        val dc = DatabaseConnection()
        dc.setCollections(user.AuthToken)
        var collections = dc.waitForCollections()
        for (list in collections) {

            val button = Button(binding.listsContainer.context)
            button.text = list.name
            button.setOnClickListener { view : View ->
                view.run{
                    findNavController().navigate(R.id.action_listsFragment_to_itemsFragment, bundleOf("listIdArg" to list.id.toString(), "authTokenArg" to user.AuthToken))
                }
            }
            binding.listsContainer.addView(button)
        }
        val button = Button(binding.listsContainer.context)
        button.text = "Add New Collection"
        button.setOnClickListener { view : View ->
            view.run{
                findNavController().navigate(R.id.action_listsFragment_to_addCollectionFragment, bundleOf("usernameArg" to user.Username, "authTokenArg" to user.AuthToken))
            }
        }
        binding.listsContainer.addView(button)
        return binding.root
    }


}
