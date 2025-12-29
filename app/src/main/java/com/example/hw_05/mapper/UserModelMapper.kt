package com.example.hw_05.mapper

import com.example.hw_05.db.entity.UserEntity
import com.example.hw_05.model.UserDataModel

class UserModelMapper {

    fun map(entity: UserEntity): UserDataModel {
        return UserDataModel(
            id = entity.id,
            name = entity.name,
            nickname = entity.nickname,
            email = entity.email
        )
    }
}
