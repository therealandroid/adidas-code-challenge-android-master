package gitlab.therealdroid.com.addidaschallenge.ui.rewards

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.squareup.picasso.Picasso
import gitlab.therealdroid.com.addidaschallenge.R
import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal
import kotlinx.android.synthetic.main.activity_profile_and_rewards.*
import kotlinx.android.synthetic.main.empty_view.*

class RewardsActivity : AppCompatActivity(), RewardsContract {


    val rewardsAdapter = RewardsAdapterAdapter(this)
    val rewardsPresenter = RewardsPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_and_rewards)
        setupView()
    }

    override fun onResume() {
        super.onResume()
        rewardsPresenter.fetchCompletedGoals()
    }

    private fun setupView() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = resources.getString(R.string.TOOLBAR_rewards_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        rewardsList.layoutManager = LinearLayoutManager(this)
        rewardsList.adapter = rewardsAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            super.onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun showLoadingCompletedGoals() {
        rewardsLoading.visibility = View.VISIBLE
    }

    override fun hideLoadingCompletedGoals() {
        rewardsLoading.visibility = View.INVISIBLE
    }

    override fun fetchAllCompletedGoalsSuccess(goals: List<Goal>) {
        rewardsAdapter.setRewards(goals)
        rewardsAdapter.notifyDataSetChanged()

        emptyView.visibility = View.INVISIBLE
    }

    override fun fetchAllCompletedGoalsEmpty() {
        emptyView.visibility = View.VISIBLE
        emptyTitle.text = getString(R.string.empty_view_no_rewards_title)
        emptyDescription.text = getString(R.string.empty_view_no_rewards_description)
        Picasso.get().load(R.drawable.ic_running).into(emptyImage)
    }

}