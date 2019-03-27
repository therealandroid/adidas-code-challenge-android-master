package gitlab.therealdroid.com.addidaschallenge.ui.rewards

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import gitlab.therealdroid.com.addidaschallenge.R
import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal
import gitlab.therealdroid.com.addidaschallenge.ui.goals.GoalsListAdapter.Companion.TROPHY_TYPE_BRONZE_MEDAL
import gitlab.therealdroid.com.addidaschallenge.ui.goals.GoalsListAdapter.Companion.TROPHY_TYPE_GOLD_MEDAL
import gitlab.therealdroid.com.addidaschallenge.ui.goals.GoalsListAdapter.Companion.TROPHY_TYPE_SILVER_MEDAL
import gitlab.therealdroid.com.addidaschallenge.ui.goals.GoalsListAdapter.Companion.TROPHY_TYPE_ZOMBIE_HAND
import gitlab.therealdroid.com.addidaschallenge.ui.profile.ProfileFragment
import kotlinx.android.synthetic.main.item_reward_type_list_header.view.*
import kotlinx.android.synthetic.main.item_reward_type_list.view.*

class RewardsAdapterAdapter(val activity: RewardsActivity) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var NORMAL_ITEM = 0
    private var HEADER_ITEM = 1
    private var accomplishedGoals: List<Goal> = mutableListOf()

    fun setRewards(goals: List<Goal>) {
        accomplishedGoals = goals
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HEADER_ITEM) {
            HeaderViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_reward_type_list_header,
                    parent,
                    false
                )
            )
        } else {
            NormalViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_reward_type_list,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is HeaderViewHolder) {
            activity.supportFragmentManager.beginTransaction()
                .add(R.id.profileFragment, ProfileFragment(), ProfileFragment::class.java.simpleName)
                .commit()
        } else if (viewHolder is NormalViewHolder) {
            val goal = accomplishedGoals[position -HEADER_ITEM]
            viewHolder.rewardsTitle.text = goal.title
            viewHolder.rewardsDescription.text = goal.description
            val reward = goal.reward?.points
            viewHolder.goalPointsEarned.text = ("+$reward pts")

            when (goal.reward?.trophy) {
                TROPHY_TYPE_BRONZE_MEDAL -> Picasso.get().load(R.drawable.ic_badge_bronze_medal_96).into(viewHolder.rewardsBadge)
                TROPHY_TYPE_SILVER_MEDAL -> Picasso.get().load(R.drawable.ic_badge_silver_medal_96).into(viewHolder.rewardsBadge)
                TROPHY_TYPE_GOLD_MEDAL -> Picasso.get().load(R.drawable.ic_gold_silver_medal_96).into(viewHolder.rewardsBadge)
                TROPHY_TYPE_ZOMBIE_HAND -> Picasso.get().load(R.drawable.ic_badge_zombie_hand).into(viewHolder.rewardsBadge)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            HEADER_ITEM
        } else {
            NORMAL_ITEM
        }
    }

    //Footer will be the trophy
    override fun getItemCount(): Int {
        return accomplishedGoals.size.plus(HEADER_ITEM)
    }

    inner class NormalViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val rewardsTitle: TextView = view.rewardsTitle
        val rewardsDescription: TextView = view.rewardsDescription
        val rewardsBadge: ImageView = view.rewardsBadge
        val goalPointsEarned: TextView = view.goalPointsEarned
    }

    inner class HeaderViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val profileContainer: FrameLayout = view.profileFragment
    }
}
