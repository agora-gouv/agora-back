package fr.gouv.agora.usecase.consultation.exception

class ConsultationNotFoundException(consultationId: String) : Exception("Consultation with id '$consultationId' was not found")
