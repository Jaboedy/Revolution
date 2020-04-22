package com.packrat.revolution


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.packrat.revolution.databinding.FragmentNewItemBinding

/**
 * A simple [Fragment] subclass.
 */
class NewItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val aToken = arguments?.getString("authTokenArg")!!
        val listId = arguments?.getString("listIdArg")!!
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentNewItemBinding>(inflater,
            R.layout.fragment_new_item,container,false)
        binding.addItemButton.setOnClickListener {view: View ->
            view.run {
                var dc = DatabaseConnection()
                dc.addItemsByBarcode(binding.textboxItemBarcode.text.toString(),binding.textboxItemName.text.toString(),binding.textboxItemDesc.text.toString())
                var id = dc.waitForAddedItemsByBarcode()
                if(id != null) {
                    dc.addItemsInCollection(aToken, listId, id.toString())
                }
                else {
                    Toast.makeText(this.context, "Item already exists or failed to add", Toast.LENGTH_LONG).show()
                }
                findNavController().navigate(R.id.action_newItemFragment_to_itemsFragment, bundleOf("authTokenArg" to aToken, "listIdArg" to listId))
            }

        }
        return binding.root
    }


}
