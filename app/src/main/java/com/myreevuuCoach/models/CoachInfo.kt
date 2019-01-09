package com.myreevuuCoach.models


data class CoachInfo(
        var coach_level: Int,
        var coach_experience: Int,
        var about: String,
        var experties: ArrayList<OptionsModel>,
        var certificates: ArrayList<OptionsModel>
)
