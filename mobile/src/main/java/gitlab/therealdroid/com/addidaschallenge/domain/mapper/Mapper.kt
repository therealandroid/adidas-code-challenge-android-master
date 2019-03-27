package gitlab.therealdroid.com.addidaschallenge.domain.mapper

import com.google.gson.ExclusionStrategy
import com.google.gson.FieldAttributes
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import gitlab.therealdroid.com.addidaschallenge.domain.datasource.database.entities.GoalsEntity
import gitlab.therealdroid.com.addidaschallenge.domain.models.Goal
import io.realm.RealmObject

class Mapper {

    //We should exclude Realm fields during the map process to avoid StackOverflow exception
    val gson: Gson = GsonBuilder()
        .setExclusionStrategies(object : ExclusionStrategy {
            override fun shouldSkipField(f: FieldAttributes): Boolean {
                return f.declaringClass == RealmObject::class.java
            }

            override fun shouldSkipClass(clazz: Class<*>): Boolean {
                return false
            }
        }).create()

    fun toModel(goalEntity: GoalsEntity): Goal {
        val jsonString = gson.toJson(goalEntity)
        return gson.fromJson(jsonString, Goal::class.java)
    }

    fun toRealm(goal: Goal): GoalsEntity {
        val jsonString = gson.toJson(goal)
        return gson.fromJson(jsonString, GoalsEntity::class.java)
    }

    fun toRealmList(goals: List<Goal>): List<GoalsEntity> {
        val type = object : TypeToken<List<GoalsEntity>>() {}.type
        val jsonString = gson.toJson(goals)
        return gson.fromJson(jsonString, type)
    }

    fun toModelList(goals: List<GoalsEntity>): List<Goal> {
        val type = object : TypeToken<List<Goal>>() {}.type
        val jsonString = gson.toJson(goals)
        return gson.fromJson(jsonString, type)
    }
}