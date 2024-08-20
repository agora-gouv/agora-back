package fr.gouv.agora.usecase.notification

class ConsultationIdInconnuException(consultationId: String) : Exception("There is no consultation with id $consultationId")
