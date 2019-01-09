package com.myreevuuCoach.models


abstract class BaseModel {
    var error: Error? = null

    inner class Error {
        var code: Int = 0
        var message: String? = null
    }
}