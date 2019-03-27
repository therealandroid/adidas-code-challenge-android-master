package gitlab.therealdroid.com.addidaschallenge.domain.datasource.database.entities

import io.realm.RealmObject
import io.realm.annotations.LinkingObjects
import io.realm.annotations.PrimaryKey
import java.util.*


open class GoalsEntity(
    @PrimaryKey var id: String = "",
    var title: String = "",
    var description: String = "",
    var type: String = "",
    var goal: Int = 0,
    var progress: Int = 0,
    var date: Date? = null,
    var reward: RewardEntity? = RewardEntity(),
    var state: Int = 0
): RealmObject()