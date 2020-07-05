package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.databinding.FragmentFirstBinding
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val dealsListViewModel: DealsListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val indexBuffer = mapOf<Int, Int>(
            Pair(1, 4)
        )

        val dealRecyclerAdapter = DealRecyclerAdapter(view.context)
        val fancyGridLayoutManager = GridLayoutManager(view.context, 16) //TODO(Span dynamically)
        fancyGridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (position) {
                    0 -> 16
                    1 -> 8
                    else -> 12
                }
            }

            override fun getSpanIndex(position: Int, spanCount: Int): Int {
                val index = super.getSpanIndex(position, spanCount)
                val buffer: Int? = indexBuffer[position]
                buffer?.let { return index + buffer }
                return index
            }
        }


        binding.mainCycler.run {
            adapter = dealRecyclerAdapter
            layoutManager = fancyGridLayoutManager
        }

        dealsListViewModel.dealsList.observe(this, Observer { dealsList ->
            dealRecyclerAdapter.deals = dealsList
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}