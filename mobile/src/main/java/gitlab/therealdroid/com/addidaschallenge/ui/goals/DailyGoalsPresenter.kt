package gitlab.therealdroid.com.addidaschallenge.ui.goals

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.fitness.Fitness
import com.google.android.gms.fitness.data.DataType
import com.google.android.gms.fitness.data.Field
import com.google.android.gms.fitness.request.SensorRequest
import gitlab.therealdroid.com.addidaschallenge.BuildConfig
import gitlab.therealdroid.com.addidaschallenge.configuration.ProjectConfiguration
import gitlab.therealdroid.com.addidaschallenge.domain.datasource.local.PreferencesManager
import gitlab.therealdroid.com.addidaschallenge.domain.datasource.local.PreferencesManager.Prefs.PREF_ATTR_STEP_COUNT
import gitlab.therealdroid.com.addidaschallenge.configuration.device.hasInternetConnection
import gitlab.therealdroid.com.addidaschallenge.domain.Repository
import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal
import gitlab.therealdroid.com.addidaschallenge.domain.models.ProgressState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.TimeUnit

class DailyGoalsPresenter(
    private var context: Context,
    private var stepListener: StepsListener?,
    private var dailyGoalsListener: DailyGoalsListener?
) {

    private val repository = Repository()

    //We use preferences to write the progress
    //instead writing it to the database due to performance
    private var preferencesManager = PreferencesManager(context)

    //TODO FUTURE check daily goals and replace it to keep daily goals updated
    //Is a good idea to keep the history of the user goals
    //For this we can use the Google Fit API to store it into DataSources
    fun listDailyGoals() {
        GlobalScope.launch(Dispatchers.Main) {
            dailyGoalsListener?.showListGoalsLoading()

            try {
                var goals =
                    repository.getLocalDailyGoals()

                //If device doesn't have internet  and the data was fetched previously
                if (context.hasInternetConnection() && goals.isEmpty()) {
                    goals = repository
                        .dailyGoals()
                        .await()
                        .item

                    repository.saveDailyGoals(goals)
                }

                dailyGoalsListener?.fetchListGoalsUseCaseSuccess(goals)
            } catch (e: Throwable) {
                dailyGoalsListener?.fetchListGoalsUseCaseFailed(0, e.message, e.localizedMessage)
            }

            dailyGoalsListener?.hideListGoalsLoading()
        }
    }

    //Start it every time user open the app
    //and then we establish connection to receive real time steps
    fun startSensor() {
        val sensorRequest = SensorRequest.Builder()
            .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
            .setSamplingRate(1, TimeUnit.SECONDS)
            .build()

        val client = Fitness.getSensorsClient(context, GoogleSignIn.getLastSignedInAccount(context)!!)

        client.add(sensorRequest) { it ->
            //Every time we receive update we append it to the progress
            val totalProgress = incrementProgress(it.getValue(Field.FIELD_STEPS).asInt())
            stepListener?.stepsCount(totalProgress)
            updateQueue(totalProgress)
        }
    }

    /**
     *
     * This method manage the queue and set the progress of the goals
     *
     * Queue items have 3 states
     *
     * ProgressState.STILL when the goal is not achieved yet
     * ProgressState.RUNNING when the goal is currently in progress (The user is running)
     * ProgressState.FINISHED when the goal is completed  (The user achieved the Goal)
     *
     *
     */
    private fun updateQueue(totalProgress: Int) {
        val goals = repository.getAllGoals() // get the list upated so we can verify next item in the queue

        if (goals.isEmpty()) {
            Log.e(ProjectConfiguration.TAG, "You should call listDailyGoals` first")
            return
        }

        //Get the next goal
        //NOTE: If all goals are completed, goal will be null
        //and then we can let the user know that.
        val goal = goals.find {
            it.state == ProgressState.RUNNING.ordinal || it.state == ProgressState.STILL.ordinal
        }

        //If null there is no more goals to reach,
        //Just send a message to the user saying congratulations!!
        if (goal != null) {

            //Check if the current goal is reached
            if (totalProgress > goal.goal) {
                //Update the goal progress and state
                updateGoal(goal = goal, steps = goal.goal, state = ProgressState.FINISHED.ordinal)
                // we clean the progress so the user can continue with the next Goal
                clearProgress()
                //Notify the goal was reached
                stepListener?.onNewGoalAccomplished(goal)
            } else {
                //update the goal
                updateGoal(goal, Calendar.getInstance().time, totalProgress, ProgressState.RUNNING.ordinal)
            }

            //Debug purpose
            if (BuildConfig.DEBUG) {
                print("ID -> ${goal.id} Goal -> $goal Progress -> $totalProgress")
            }
        } else {
            //all goals are finished
            stepListener?.onGoalsAccomplished()
        }

        stepListener?.onGoalsUpdated(goals)
    }

    private fun updateGoal(goal: Goal, date: Date? = null, steps: Int, state: Int): Goal {
        return repository.updateGoal(goal.id, date, steps, state)
    }

    //Stop the sensor from raw updates
    fun stopSensor() {
        Fitness.getSensorsClient(context, GoogleSignIn.getLastSignedInAccount(context)!!).remove {}
    }

    /**
     *
     * We store the steps into {@link gitlab.therealdroid.com.addidaschallenge.domain.datasource.local.PreferencesManager}
     *
     * Increment the steps and return the total progress updated
     */
    private fun incrementProgress(steps: Int): Int {
        val total = getTotalStepsGiven()
        preferencesManager.save(PREF_ATTR_STEP_COUNT, total.plus(steps))//sum progress + new steps
        return total
    }

    private fun getTotalStepsGiven(): Int {
        return preferencesManager.get(PREF_ATTR_STEP_COUNT)
    }

    private fun clearProgress() {
        preferencesManager.save(PREF_ATTR_STEP_COUNT, 0)
    }

    fun hasDialogDisplayedBefore(): Boolean? {
        return preferencesManager.get(PreferencesManager.Prefs.PREF_ATTR_TIP_DIALOG) == 1
    }

    fun disableDialog() {
        preferencesManager.save(PreferencesManager.Prefs.PREF_ATTR_TIP_DIALOG, 1)
    }

}