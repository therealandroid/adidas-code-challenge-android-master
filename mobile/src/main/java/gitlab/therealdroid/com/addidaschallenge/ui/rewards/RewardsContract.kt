package gitlab.therealdroid.com.addidaschallenge.ui.rewards

import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal


interface RewardsContract {
    fun showLoadingCompletedGoals()
    fun hideLoadingCompletedGoals()
    fun fetchAllCompletedGoalsSuccess(goals: List<Goal>)
    fun fetchAllCompletedGoalsEmpty()

}