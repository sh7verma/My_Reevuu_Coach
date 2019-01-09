package com.myreevuuCoach.models


data class BaseSuccessModel(
        var response: Response
) : ErrorModelJava() {

    data class Response(
            var message: String,
            var code: Int
    )
}