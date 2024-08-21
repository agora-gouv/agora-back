package fr.gouv.agora.usecase.notification

class UserIdInconnuException(userId: String) : Exception("User with id: $userId does not exist.")
