package com.bouzo.specials

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.bouzo.specials.databinding.FragmentDealListBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class DealListFragment : Fragment() {

    private var _binding: FragmentDealListBinding? = null
    private val binding get() = _binding!!
    private val dealsListViewModel: DealsListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDealListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dealRecyclerAdapter: DealsRecyclerAdapter by inject()
        val dealLayoutManager: RecyclerView.LayoutManager by inject()

        binding.dealListTitle.text = dealsListViewModel.dealListTitle

        binding.dealListRecyclerView.run {
            adapter = dealRecyclerAdapter.adapter
            layoutManager = dealLayoutManager
        }

        dealsListViewModel.dealsList.observe(viewLifecycleOwner, Observer { dealsList ->
            dealRecyclerAdapter.deals = dealsList
        })

        dealsListViewModel.canvasUnit.observe(viewLifecycleOwner, Observer { canvasInfo ->
            if (dealRecyclerAdapter.canvasInfo.canvasUnit != canvasInfo.canvasUnit) {
                dealRecyclerAdapter.canvasInfo = canvasInfo
                binding.dealListRecyclerView.removeAllViews()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}