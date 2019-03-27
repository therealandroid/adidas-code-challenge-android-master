package gitlab.therealdroid.com.addidaschallenge.domain.datasource.api

import gitlab.therealdroid.com.addidaschallenge.domain.models.GoalsHolder
import kotlinx.coroutines.Deferred

interface IApiWrapper {
    fun listGoals(): Deferred<GoalsHolder>
}