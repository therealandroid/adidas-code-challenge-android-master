package gitlab.therealdroid.com.addidaschallenge.domain.models

import java.util.*

enum class ProgressState {
    STILL, FINISHED, RUNNING
}

data class Goal(
    var id: String = "",
    var title: String = "",
    var description: String = "",
    var type: String = "",
    var goal: Int = 0,
    var reward: Reward? = null,
    var progress: Int = 0,
    var date: Date? = null,
    var state: Int = ProgressState.STILL.ordinal

)