package com.datarockets.mnchkn.data.models

data class Player(var id: Long = -1,
                  var name: String? = null,
                  var levelScore: Int = 1,
                  var strengthScore: Int = 1,
                  var totalScore: Int = levelScore + strengthScore,
                  var color: String? = null,
                  var position: Int = 0,
                  var playing: Boolean = false)
