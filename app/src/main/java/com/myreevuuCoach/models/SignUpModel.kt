package com.myreevuuCoach.models


class SignUpModel(var response: ProfileModel,
                  var levels: ArrayList<OptionsModel>,
                  var sports: ArrayList<OptionsModel>,
                  var experties: ArrayList<OptionsModel>,
                  var certificates: ArrayList<OptionsModel>,
                  var states: ArrayList<OptionsModel>,
                  var code: Int) : ErrorModelJava()

