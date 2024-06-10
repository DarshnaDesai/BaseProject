package com.takehomeproject.presentation.repolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.takehomeproject.R
import com.takehomeproject.data.model.UserRepoResponse
import com.takehomeproject.databinding.FragmentUserRepoListBinding
import com.takehomeproject.domain.model.UserRepoUIState
import com.takehomeproject.util.hideKeyboardOnClick
import com.takehomeproject.util.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserRepoListFragment : Fragment() {

    private var _binding: FragmentUserRepoListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserRepoListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserRepoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            rvRepoList.adapter = UserRepoListAdapter()
            (rvRepoList.adapter as UserRepoListAdapter).setOnItemClickListener(
                object : UserRepoListAdapter.OnItemClickListener {
                    override fun onItemClick(userRepo: UserRepoResponse) {
                        navigateToDetails(userRepo)
                    }
                })
            btnSearch.setOnClickListener {
                fetchUserData()
            }
            etInput.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    fetchUserData()
                }
                true
            }
        }

        setObservableFlow()
    }

    private fun fetchUserData() {
        binding.etInput.hideKeyboardOnClick()
        binding.tvUserName.visibility = View.GONE
        binding.ivUserProfile.visibility = View.GONE
        (binding.rvRepoList.adapter as UserRepoListAdapter).clearData()
        viewModel.fetchUser(binding.etInput.text.toString().trim())
    }

    private fun setObservableFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userRepoUIState.collect { state ->
                when (state) {
                    is UserRepoUIState.Loading -> {
                        binding.progress.visibility = state.visibility
                    }

                    is UserRepoUIState.Success -> {
                        state.setDataOnSuccess()
                    }

                    is UserRepoUIState.Error -> {
                        binding.progress.visibility = View.GONE
                        Toast.makeText(context, getString(R.string.no_data), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

            }
        }
    }

    private fun UserRepoUIState.Success.setDataOnSuccess() {
        binding.progress.visibility = View.GONE
        with(binding) {
            tvUserName.visibility = View.VISIBLE
            ivUserProfile.visibility = View.VISIBLE
            tvUserName.text = this@setDataOnSuccess.userData.name
            ivUserProfile.loadImage(this@setDataOnSuccess.userData.avatar_url)
            (rvRepoList.adapter as UserRepoListAdapter).updateData(this@setDataOnSuccess.repoList as ArrayList<UserRepoResponse>)
        }
    }

    private fun navigateToDetails(userRepo: UserRepoResponse) {
        findNavController().navigate(
            UserRepoListFragmentDirections.actionUserRepoListFragmentToUserRepoDetailsFragment(
                userRepo
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}