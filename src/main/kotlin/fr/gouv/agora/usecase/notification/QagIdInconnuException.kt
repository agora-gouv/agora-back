package fr.gouv.agora.usecase.notification

class QagIdInconnuException(qagId: String) : Exception("There is no qag with id $qagId")
