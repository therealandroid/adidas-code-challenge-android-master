package gitlab.therealdroid.com.addidaschallenge.ui.goals

import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal


interface StepsListener {
    fun stepsCount(steps: Int)
    fun onNewGoalAccomplished(goal: Goal)
    fun onGoalsAccomplished()
    fun onGoalsUpdated(goals: List<Goal>)
}
