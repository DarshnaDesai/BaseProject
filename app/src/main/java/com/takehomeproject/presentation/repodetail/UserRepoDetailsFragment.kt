package com.takehomeproject.presentation.repodetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.takehomeproject.databinding.FragmentUserRepoDetailsBinding

class UserRepoDetailsFragment : Fragment() {

    private var _binding: FragmentUserRepoDetailsBinding? = null
    private val binding: FragmentUserRepoDetailsBinding get() = _binding!!
    private val args: UserRepoDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserRepoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userRepo = args.userRepo

        userRepo?.let { repo ->
            with(binding) {
                tvTitleLabelValue.text = repo.name
                tvRepoDescLabelValue.text = repo.description
                tvRepoUpdatedLabelValue.text = repo.updated_at
                tvRepoStarGazerCountLabelValue.text = repo.stargazers_count.toString()
                tvRepoForksLabelValue.text = repo.forks_count.toString()
                tvRepoOpenIssueLabelValue.text = repo.open_issues_count.toString()
                if (repo.forks_count > 5000) {
                    tvForksBadge.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}