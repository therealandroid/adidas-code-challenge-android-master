package gitlab.therealdroid.com.addidaschallenge.ui.goals

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import gitlab.therealdroid.com.addidaschallenge.BuildConfig
import gitlab.therealdroid.com.addidaschallenge.R
import gitlab.therealdroid.com.addidaschallenge.configuration.device.showSnack
import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal
import gitlab.therealdroid.com.addidaschallenge.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_goals_list.*
import android.R.string.cancel
import android.app.AlertDialog
import android.content.DialogInterface
import android.preference.PreferenceManager
import gitlab.therealdroid.com.addidaschallenge.domain.datasource.local.PreferencesManager


class DailyGoalsFragment : Fragment(), DailyGoalsListener, StepsListener {
    interface GoalsReachedListener {
        fun onGoalsReached()
    }

    var goalListener: GoalsReachedListener? = null

    var dailyGoalsPresenter: DailyGoalsPresenter? = null
    var adapter: GoalsListAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_goals_list, container, false)
        dailyGoalsPresenter = DailyGoalsPresenter(context!!, this, this)
        return view
    }

    override fun onResume() {
        super.onResume()
        dailyGoalsPresenter?.listDailyGoals()
    }

    override fun showListGoalsLoading() {
        goalsLoading.visibility = View.VISIBLE
    }

    override fun hideListGoalsLoading() {
        goalsLoading.visibility = View.INVISIBLE
    }

    override fun fetchListGoalsUseCaseSuccess(goals: List<Goal>) {
        adapter = GoalsListAdapter(goals)
        goalsList?.layoutManager = LinearLayoutManager(context)
        goalsList?.adapter = adapter
        dailyGoalsPresenter?.startSensor()

        if(!dailyGoalsPresenter?.hasDialogDisplayedBefore()!!){
            showTipDialog()
        }
    }

    override fun fetchListGoalsUseCaseFailed(cause: Int, message: String?, description: String?) {
        if (BuildConfig.DEBUG)
            activity?.showSnack("$cause -> $message -> $description")
    }

    override fun stepsCount(steps: Int) {
        if (BuildConfig.DEBUG)
            activity?.showSnack("Total steps$steps")
    }

    override fun onNewGoalAccomplished(goal: Goal) {
        goalListener?.onGoalsReached()

        if (BuildConfig.DEBUG)
            activity?.showSnack("NEW GOAL ACCOMPLISHED" + goal.progress)
    }

    override fun onGoalsAccomplished() {
        adapter?.setFooterEnabled(true)

        if (BuildConfig.DEBUG)
            activity?.showSnack("CONGRATULATIONS, YOU'VE FINISHED ALL GOALS")
    }

    override fun onGoalsUpdated(goals: List<Goal>) {
        adapter?.addGoals(goals)
    }

    fun setGoalReachedListener(activity: MainActivity) {
        goalListener = activity
    }

    fun showTipDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Tip")
        builder.setMessage("Start walking to start counting your daily goals")
        builder.setPositiveButton("Got it") { dialog, _ ->
            dailyGoalsPresenter?.disableDialog()
            dialog.cancel()
        }
        val dialog = builder.create()
        dialog.show()
    }
}
