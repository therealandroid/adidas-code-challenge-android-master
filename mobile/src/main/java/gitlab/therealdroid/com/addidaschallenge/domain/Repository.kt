package gitlab.therealdroid.com.addidaschallenge.domain

import android.support.annotation.VisibleForTesting
import gitlab.therealdroid.com.addidaschallenge.domain.datasource.api.ApiWrapper
import gitlab.therealdroid.com.addidaschallenge.domain.datasource.api.IApiWrapper
import gitlab.therealdroid.com.addidaschallenge.domain.datasource.database.GoalsDao
import gitlab.therealdroid.com.addidaschallenge.domain.datasource.database.IDao
import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal
import gitlab.therealdroid.com.addidaschallenge.domain.models.GoalsHolder
import gitlab.therealdroid.com.addidaschallenge.domain.models.ProgressState
import kotlinx.coroutines.Deferred
import java.util.*

/**
 *
 * The repository centralize the data sources
 *
 * This constructor with parameters is needed for testing purposes
 *
 */
class Repository(var goalsDao: IDao? = null, var apiWrapper: IApiWrapper? = null) {

    init {
        if (goalsDao == null) {
            goalsDao = GoalsDao() // -> Realm database
        }

        if (apiWrapper == null) {
            apiWrapper = ApiWrapper() // -> Realm database
        }
    }

    //Async Request
    fun dailyGoals(): Deferred<GoalsHolder> {
        return apiWrapper!!.listGoals()
    }

    fun getTotalPointsEarned(): Int {
        return goalsDao!!.getEarnedPoints(ProgressState.FINISHED.ordinal)
    }

    fun getLocalDailyGoals(): List<Goal> {
        return goalsDao!!.getAllGoals()
    }

    fun saveDailyGoals(goals: List<Goal>) {
        goalsDao!!.saveGoals(goals)
    }

    fun getAllGoals(): List<Goal> {
        return goalsDao!!.getAllGoals()
    }

    fun updateGoal(id: String, date: Date? , progress: Int, state: Int): Goal {
        return goalsDao!!.updateGoal(id, date, progress, state)
    }
}