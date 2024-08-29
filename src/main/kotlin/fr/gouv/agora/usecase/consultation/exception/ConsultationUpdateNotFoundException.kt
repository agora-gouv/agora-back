package fr.gouv.agora.usecase.consultation.exception

class ConsultationUpdateNotFoundException(consultationId: String, consultationUpdateId: String) : Exception("Consultation update with id '$consultationUpdateId' for the consultation '$consultationId' was not found")
