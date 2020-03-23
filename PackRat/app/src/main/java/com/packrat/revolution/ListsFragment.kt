package com.packrat.revolution


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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
        return inflater.inflate(R.layout.fragment_lists, container, false)
    }


}
