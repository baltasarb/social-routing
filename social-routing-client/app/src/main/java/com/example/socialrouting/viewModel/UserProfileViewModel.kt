package com.example.socialrouting.viewModel

import android.arch.lifecycle.ViewModel
import com.example.socialrouting.model.User

class UserProfileViewModel : ViewModel() {

    lateinit var userId: String
    lateinit var user: User

    fun init(userId: String) {
        this.userId = userId
    }

    fun getUser() = user


}