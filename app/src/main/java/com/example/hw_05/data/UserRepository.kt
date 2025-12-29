package com.example.hw_05.data

import com.example.hw_05.Hash
import com.example.hw_05.Keys
import com.example.hw_05.db.dao.UserDao
import com.example.hw_05.db.entity.UserEntity
import com.example.hw_05.mapper.UserModelMapper
import com.example.hw_05.model.UserDataModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

sealed interface RegisterResult {
    data class Success(val userId: Long) : RegisterResult
    data object UserExists : RegisterResult
    data object EmptyFields : RegisterResult

}

sealed interface LoginResult {
    data class Success(val user: UserDataModel) : LoginResult
    data object UserNotFound : LoginResult
    data object BadCredentials : LoginResult
    data class NeedRecovery(val userId: Long) : LoginResult
    data object DeletedForever : LoginResult
    data object EmptyFields : LoginResult
}
sealed interface RecoveryResult {
    data object UserNotFound : RecoveryResult
    data object NotDeleted : RecoveryResult
    data object Restored : RecoveryResult
    data object DeletedForever : RecoveryResult
}


class UserRepository(
    private val userDao: UserDao,
    private val mapper: UserModelMapper,
    private val ioDispatcher: CoroutineDispatcher
) {

    suspend fun register(
        name: String,
        nickname: String,
        email: String,
        password: String
    ): RegisterResult = withContext(ioDispatcher) {

        val normalizedEmail = email.lowercase()
        val n = name.trim()
        val nick = nickname.trim()
        val e = normalizedEmail.trim()

        if (n.isBlank() || nick.isBlank() || e.isBlank() || password.isBlank()) {
            return@withContext RegisterResult.EmptyFields
        }

        val existing = userDao.findByEmail(normalizedEmail)
        if (existing != null) return@withContext RegisterResult.UserExists

        val id = userDao.insert(
            UserEntity(
                name = n,
                nickname = nick,
                email = e,
                passwordHash = Hash.sha256(password)
            )
        )

        RegisterResult.Success(id)
    }

    suspend fun login(email: String, password: String): LoginResult =
        withContext(ioDispatcher) {

            val normalizedEmail = email.lowercase()
            val e = normalizedEmail.trim()
            if (e.isBlank() || password.isBlank()) {
                return@withContext LoginResult.EmptyFields
            }

            val user = userDao.findByEmail(e) ?: return@withContext LoginResult.UserNotFound

            val ok = user.passwordHash == Hash.sha256(password)
            if (!ok) return@withContext LoginResult.BadCredentials

            val deletedAt = user.deletedAt ?: return@withContext LoginResult.Success(mapper.map(user))

            val expired = System.currentTimeMillis() - deletedAt > Keys.DAYS_7_MS
            return@withContext if (expired) {
                userDao.hardDelete(user.id)
                LoginResult.DeletedForever
            } else {
                LoginResult.NeedRecovery(user.id)
            }
        }


    suspend fun findById(userId: Long): UserDataModel? =
        withContext(ioDispatcher) {
            val user = userDao.findById(userId) ?: return@withContext null
            mapper.map(user)
        }

    suspend fun softDelete(userId: Long) =
        withContext(ioDispatcher) { userDao.softDelete(userId, System.currentTimeMillis()) }

    suspend fun restore(userId: Long) =
        withContext(ioDispatcher) { userDao.restore(userId) }

    suspend fun hardDelete(userId: Long) =
        withContext(ioDispatcher) { userDao.hardDelete(userId) }

    suspend fun cleanupDeleted(): Int =
        withContext(ioDispatcher) {
            val border = System.currentTimeMillis() - Keys.DAYS_7_MS
            userDao.cleanupDeleted(border)
        }



}
