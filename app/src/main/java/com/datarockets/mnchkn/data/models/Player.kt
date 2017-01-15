package com.datarockets.mnchkn.data.models

data class Player(var id: Long = -1,
                  var name: String? = null,
                  var levelScore: Int = 1,
                  var strengthScore: Int = 1,
                  var totalScore: Int = 2,
                  var color: String? = null)
