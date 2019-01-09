package com.myreevuuCoach.models

data class ProfileModel(var id: Int,
                        var name: String,
                        var username: String,
                        var email: String,
                        var gender: Int,
                        var email_notification: Int,
                        var phone_number: String,
                        var profile_pic: String,
                        var access_token: String,
                        var user_type: Int,
                        var account_type: Int,
                        var email_verified: Int,
                        var skip_tip: Int,
                        var is_blocked: Int,
                        var is_approved: Int,
                        var profile_status: Int,
                        var created_at: String,
                        var updated_at: String,
                        var new_email: String,
                        var sport_info: SportInfo,
                        var coach_info: CoachInfo)

