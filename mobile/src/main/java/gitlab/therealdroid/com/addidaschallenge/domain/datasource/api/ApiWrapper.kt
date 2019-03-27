package gitlab.therealdroid.com.addidaschallenge.domain.datasource.api

import gitlab.therealdroid.com.addidaschallenge.domain.models.GoalsHolder
import kotlinx.coroutines.Deferred

class ApiWrapper: IApiWrapper {

    override fun listGoals(): Deferred<GoalsHolder> {
        return ApiManager.goalsApi.listGoals()
    }

}