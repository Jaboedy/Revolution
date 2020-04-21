package com.packrat.revolution


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.packrat.revolution.databinding.FragmentItemsBinding
import kotlinx.android.synthetic.main.fragment_items.*

/**
 * A simple [Fragment] subclass.
 */
class ItemsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var aToken = arguments?.getString("authTokenArg")!!
        var listId = arguments?.getString("listIdArg")!!
        val items = mutableListOf(Item(1, "1234", arguments!!.getString("listIdArg")!!, "First Item"), Item(2, "4321", "Item 2", "Second Item"))
        // Inflate the layout for this fragment
        var dc = DatabaseConnection()
        dc.setItemsInCollection(aToken, listId)
        var listItems = dc.waitForItemsInCollection()
        val binding = DataBindingUtil.inflate<FragmentItemsBinding>(inflater,
            R.layout.fragment_items,container,false)
        for (item in listItems) {
            val button = Button(binding.itemsContainer.context)
            button.text = item.name
            button.setOnClickListener { Toast.makeText(this.context, item.desc, Toast.LENGTH_LONG).show()}
            binding.itemsContainer.addView(button)
        }
        val button = Button(binding.itemsContainer.context)
        button.text = "Add Item"
        button.setOnClickListener {
            view.run {
                findNavController().navigate(R.id.action_itemsFragment_to_newItemFragment, bundleOf("authTokenArg" to aToken, "listIdArg" to listId))
            }
        }
        binding.itemsContainer.addView(button)
        return binding.root
    }


}
