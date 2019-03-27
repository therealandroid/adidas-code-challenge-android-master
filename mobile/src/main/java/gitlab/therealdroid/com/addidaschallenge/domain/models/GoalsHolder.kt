package gitlab.therealdroid.com.addidaschallenge.domain.models

import com.google.gson.annotations.SerializedName

data class GoalsHolder(@SerializedName("items") var item: List<Goal>)