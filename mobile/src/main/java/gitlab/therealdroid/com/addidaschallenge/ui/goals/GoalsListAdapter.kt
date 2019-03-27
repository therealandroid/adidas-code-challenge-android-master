package gitlab.therealdroid.com.addidaschallenge.ui.goals

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import gitlab.therealdroid.com.addidaschallenge.R
import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal
import gitlab.therealdroid.com.addidaschallenge.domain.models.ProgressState
import kotlinx.android.synthetic.main.item_goals_type_list_big.view.*
import kotlinx.android.synthetic.main.item_goals_type_list_big_footer.view.*

class GoalsListAdapter(private var goals: List<Goal>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var NORMAL_ITEM = 0
    private var FOOTER_ITEM = 1
    private var footerEnabled = false

    companion object {
        val TROPHY_TYPE_BRONZE_MEDAL = "bronze_medal"
        val TROPHY_TYPE_SILVER_MEDAL = "silver_medal"
        val TROPHY_TYPE_GOLD_MEDAL = "gold_medal"
        val TROPHY_TYPE_ZOMBIE_HAND = "zombie_hand"
    }

    fun setFooterEnabled(isEnabled: Boolean) {
        this.footerEnabled = isEnabled
        notifyDataSetChanged()
    }

    fun addGoals(goals: List<Goal>) {
        this.goals = goals
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == NORMAL_ITEM) {
            ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_goals_type_list_big, parent, false))
        } else {
            FooterViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_goals_type_list_big_footer,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == FOOTER_ITEM) {
            val footerViewHolder = holder as FooterViewHolder

            with(footerViewHolder.endTitle) {
                footerViewHolder.endTitle.text = context.getString(R.string.progress_complete)
                footerViewHolder.endDescription.text = context.getString(R.string.progress_complete_message)
            }
            with(footerViewHolder.endBadge) {
                footerViewHolder.endBadge.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_trophy_96
                    )
                )
            }
        } else {
            val viewHolder = holder as ViewHolder

            val item = goals[position]
            viewHolder.goalsDescription.text = item.description
            val reward = item.reward?.points
            viewHolder.goalsPoints.text = "$reward PTS"
            viewHolder.goalsRewardProgress.text = item.title
            val steps = item.progress
            val stepsGoal = item.goal
            viewHolder.steps.text = "$steps / $stepsGoal"

            with(viewHolder.goalsRewardState) {
                when (item.state) {
                    ProgressState.RUNNING.ordinal -> {
                        viewHolder.goalsPoints.setTextColor(ContextCompat.getColor(context, R.color.primaryColor))
                        viewHolder.goalsRewardState.setTextColor(ContextCompat.getColor(context, R.color.RUNNING))
                        viewHolder.goalsRewardState.text = context.getString(R.string.GOAL_STATE_RUNNING)
                    }
                    ProgressState.FINISHED.ordinal -> {
                        viewHolder.goalsPoints.setTextColor(ContextCompat.getColor(context, R.color.RUNNING))
                        viewHolder.goalsRewardState.setTextColor(ContextCompat.getColor(context, R.color.FINISHED))
                        viewHolder.goalsRewardState.text = context.getString(R.string.GOAL_STATE_ACCOMPLISHED)
                    }
                    ProgressState.STILL.ordinal -> {
                        viewHolder.goalsPoints.setTextColor(ContextCompat.getColor(context, R.color.primaryColor))

                        if (position == 0) {//set the first goal to still state
                            viewHolder.goalsRewardState.setTextColor(ContextCompat.getColor(context, R.color.RUNNING))
                            viewHolder.goalsRewardState.text = context.getString(R.string.GOAL_STATE_STILL)
                        } else {//others are locked
                            viewHolder.goalsRewardState.setTextColor(ContextCompat.getColor(context, R.color.FINISHED))
                            viewHolder.goalsRewardState.text = context.getString(R.string.GOAL_STATE_LOCKED)
                        }
                    }
                }
            }

            if (item.state == ProgressState.FINISHED.ordinal) {
                Picasso.get().load(R.drawable.ic_badge_checked).into(viewHolder.goalsBadge)
            } else {
                when (item.reward?.trophy) {
                    TROPHY_TYPE_BRONZE_MEDAL -> Picasso.get().load(R.drawable.ic_badge_bronze_medal_96).into(viewHolder.goalsBadge)
                    TROPHY_TYPE_SILVER_MEDAL -> Picasso.get().load(R.drawable.ic_badge_silver_medal_96).into(viewHolder.goalsBadge)
                    TROPHY_TYPE_GOLD_MEDAL -> Picasso.get().load(R.drawable.ic_gold_silver_medal_96).into(viewHolder.goalsBadge)
                    TROPHY_TYPE_ZOMBIE_HAND -> Picasso.get().load(R.drawable.ic_badge_zombie_hand).into(viewHolder.goalsBadge)
                }
            }

        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == goals.size && footerEnabled) {
            FOOTER_ITEM
        } else {
            NORMAL_ITEM
        }
    }

    //Footer will be the trophy
    override fun getItemCount(): Int {
        return if (footerEnabled) {
            goals.size + FOOTER_ITEM
        } else {
            goals.size
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val goalsDescription: TextView = view.goalsDescription
        val goalsPoints: TextView = view.goalsPoints
        val goalsRewardProgress: TextView = view.goalsRewardProgress
        val goalsBadge: ImageView = view.goalsBadge
        val steps: TextView = view.goalsSteps
        val goalsRewardState: TextView = view.goalsRewardState
        val bottomVerticalLine: View = view.bottomVerticalLine
    }

    inner class FooterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val endTitle: TextView = view.goalsEndTitle
        val endDescription: TextView = view.goalsEndDescription
        val endBadge: ImageView = view.goalsEndBadge

    }
}
