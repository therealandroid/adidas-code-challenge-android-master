package gitlab.therealdroid.com.addidaschallenge.domain.datasource.database

import gitlab.therealdroid.com.addidaschallenge.domain.datasource.database.entities.GoalsEntity
import gitlab.therealdroid.com.addidaschallenge.domain.mapper.Mapper
import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal
import gitlab.therealdroid.com.addidaschallenge.domain.models.ProgressState
import io.realm.Realm
import io.realm.RealmResults
import io.realm.Sort
import java.util.*

class GoalsDao : IDao {
    private var mapper = Mapper()
    private val realm = Realm.getDefaultInstance()

    override fun saveGoals(goals: List<Goal>) {
        realm.beginTransaction()
        val list = mapper.toRealmList(goals)
        realm.insert(list)
        realm.commitTransaction()
    }

    override fun getEarnedPoints(state: Int): Int {
        val results: RealmResults<GoalsEntity> = realm.where(GoalsEntity::class.java).equalTo("state", state).findAll()
        return results.sumBy {
            it.reward!!.points
        }
    }

    override fun getGoalById(id: String): Goal? {
        val goal = realm.where(GoalsEntity::class.java).equalTo("id", id).findFirst()

        return if (goal != null) {
            mapper.toModel(realm.copyFromRealm(goal))
        } else {
            null
        }
    }

    override fun getAllGoals(): List<Goal> {
        val results = realm.where(GoalsEntity::class.java).findAll()
        return mapper.toModelList(realm.copyFromRealm(results))
    }

    override fun getGoalByState(state: Int): Goal? {
        val goal = realm.where(GoalsEntity::class.java).equalTo("state", state).findFirst()

        return if (goal != null) {
            mapper.toModel(realm.copyFromRealm(goal))
        } else {
            null
        }
    }

    override fun getAllFinishedGoalsDesc(): List<Goal> {
        val results = realm.where(GoalsEntity::class.java)
            .equalTo("state", ProgressState.FINISHED.ordinal)
            .sort("date", Sort.DESCENDING)
            .findAll()

        return mapper.toModelList(realm.copyFromRealm(results))
    }

    override fun updateGoal(id: String, date: Date?, progress: Int, state: Int): Goal {
        val entity = realm.where(GoalsEntity::class.java).equalTo("id", id).findFirst()
            ?: throw NullPointerException("You're trying to modify an object that is not present in the DB: Table=Goal, id=($id)")

        realm.beginTransaction()
        entity.state = state
        entity.progress = progress
        if (date != null) {
            entity.date = date
        }

        val updatedEntity = realm.copyFromRealm(realm.copyToRealmOrUpdate(entity))
        realm.commitTransaction()

        return mapper.toModel(updatedEntity)
    }


}