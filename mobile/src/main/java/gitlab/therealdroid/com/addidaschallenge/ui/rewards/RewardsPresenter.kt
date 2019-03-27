package gitlab.therealdroid.com.addidaschallenge.ui.rewards

import gitlab.therealdroid.com.addidaschallenge.domain.datasource.database.GoalsDao

class RewardsPresenter(private val contract: RewardsContract) {

    private val goalsDao = GoalsDao()

    fun fetchCompletedGoals() {
        contract.showLoadingCompletedGoals()
        val goals = goalsDao.getAllFinishedGoalsDesc()

        if (goals.isEmpty()) {
            contract.fetchAllCompletedGoalsEmpty()
        } else {
            contract.fetchAllCompletedGoalsSuccess(goals)
        }

        contract.hideLoadingCompletedGoals()
    }

}