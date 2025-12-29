package com.example.hw_05.mapper


import com.example.hw_05.db.entity.PetEntity
import com.example.hw_05.model.PetDataModel

class PetModelMapper {

    fun map(entity: PetEntity): PetDataModel {
        return PetDataModel(
            id = entity.id,
            name = entity.name,
            weightKg = entity.weightKg,
            appearedAt = entity.appearedAt
        )
    }

    fun map(input: PetDataModel): PetEntity = PetEntity(
        id = input.id,
        name = input.name,
        weightKg = input.weightKg,
        appearedAt = input.appearedAt
    )

}
