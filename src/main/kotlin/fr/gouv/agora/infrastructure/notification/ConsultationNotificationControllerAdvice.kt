package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.usecase.notification.ConsultationIdInconnuException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice(assignableTypes = [ConsultationNotificationController::class])
class ConsultationNotificationControllerAdvice {
    @ExceptionHandler(ConsultationIdInconnuException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun consultationIdInconnuHandler(e: ConsultationIdInconnuException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un id de consultation existant.")
    }
}

class ErrorResponse(val title: String)
