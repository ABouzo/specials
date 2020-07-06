package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentFirstBinding
import org.koin.android.ext.android.inject
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

        val dealRecyclerAdapter: DealsRecyclerAdapter by inject()
        val dealLayoutManager: RecyclerView.LayoutManager by inject()


        binding.mainCycler.run {
            adapter = dealRecyclerAdapter.adapter
            layoutManager = dealLayoutManager
        }

        dealsListViewModel.dealsList.observe(this, Observer { dealsList ->
            dealRecyclerAdapter.deals = dealsList
        })

        dealsListViewModel.canvasUnit.observe(this, Observer { canvasInfo ->
            if (dealRecyclerAdapter.canvasInfo.canvasUnit != canvasInfo.canvasUnit) {
                dealRecyclerAdapter.canvasInfo = canvasInfo
                binding.mainCycler.removeAllViews()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}