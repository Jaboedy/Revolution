package com.packrat.revolution


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.packrat.revolution.databinding.FragmentItemsBinding
import kotlinx.android.synthetic.main.fragment_items.*

/**
 * A simple [Fragment] subclass.
 */
class ItemsFragment : Fragment() {
    val buttons = mutableListOf<Button>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val items = mutableListOf(Item(1, "1234", arguments!!.getString("listArg")!!, "First Item"), Item(2, "4321", "Item 2", "Second Item"))
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentItemsBinding>(inflater,
            R.layout.fragment_items,container,false)
        for (item in items) {
            val button = Button(binding.itemsContainer.context)
            button.text = item.ItemName
            button.setOnClickListener { Toast.makeText(this.context, item.Description, Toast.LENGTH_LONG).show()}
            binding.itemsContainer.addView(button)
        }
        return binding.root
    }


}
