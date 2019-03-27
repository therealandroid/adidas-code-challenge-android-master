package gitlab.therealdroid.com.addidaschallenge.domain.datasource.database

import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal
import java.util.*

/**
 * This interface help you to easily replace your database implementation
 * IE: Use ROOM instead Realm objects
 *
 */
interface IDao {
    fun saveGoals(goals: List<Goal>)

    fun getEarnedPoints(state: Int): Int

    fun getGoalById(id: String): Goal?

    fun getAllGoals(): List<Goal>

    fun getGoalByState(state: Int): Goal?

    fun getAllFinishedGoalsDesc(): List<Goal>

    fun updateGoal(id: String, date: Date?, progress: Int, state: Int): Goal

}