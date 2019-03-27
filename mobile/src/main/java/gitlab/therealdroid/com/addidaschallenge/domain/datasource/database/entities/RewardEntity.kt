package gitlab.therealdroid.com.addidaschallenge.domain.datasource.database.entities

import io.realm.RealmObject

open class RewardEntity(
    var trophy: String = "",
    var points: Int = 0
): RealmObject()