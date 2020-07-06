package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.myapplication.shelfs.DealRecyclerAdapter
import com.example.myapplication.databinding.FragmentFirstBinding
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.java.KoinJavaComponent.getKoin

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

        val dealRecyclerAdapter =
            DealRecyclerAdapter(
                view.context,
                getKoin().get()
            )
        val fancyGridLayoutManager = GridLayoutManager(view.context, 1) //TODO(Span dynamically)


        binding.mainCycler.run {
            adapter = dealRecyclerAdapter
            layoutManager = fancyGridLayoutManager
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