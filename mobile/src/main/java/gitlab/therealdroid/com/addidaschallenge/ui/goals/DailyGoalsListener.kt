package gitlab.therealdroid.com.addidaschallenge.ui.goals

import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal


interface DailyGoalsListener {
    fun showListGoalsLoading()
    fun hideListGoalsLoading()
    fun fetchListGoalsUseCaseSuccess(goals: List<Goal>)
    fun fetchListGoalsUseCaseFailed(cause: Int, message: String?, description: String?)
}