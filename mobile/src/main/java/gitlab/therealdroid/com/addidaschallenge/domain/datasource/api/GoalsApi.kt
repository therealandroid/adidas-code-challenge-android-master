package gitlab.therealdroid.com.addidaschallenge.domain.datasource.api

import gitlab.therealdroid.com.addidaschallenge.domain.models.GoalsHolder
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface GoalsApi {

    @GET("/_ah/api/myApi/v1/goals")
    fun listGoals(): Deferred<GoalsHolder>

}