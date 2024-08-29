package fr.gouv.agora.infrastructure.common

import fr.gouv.agora.usecase.notification.ConsultationIdInconnuException
import fr.gouv.agora.usecase.notification.FcmTokenVideException
import fr.gouv.agora.usecase.notification.QagIdInconnuException
import fr.gouv.agora.usecase.notification.UserIdInconnuException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class DefaultControllerAdvice {
    @ExceptionHandler(ConsultationIdInconnuException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun consultationIdInconnuHandler(e: ConsultationIdInconnuException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un id de consultation existant.")
    }

    @ExceptionHandler(QagIdInconnuException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun qagIdInconnuHandler(e: QagIdInconnuException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un id de qag existant.")
    }

    @ExceptionHandler(FcmTokenVideException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun fcmTokenVideHandler(e: FcmTokenVideException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un FcmToken.")
    }

    @ExceptionHandler(UserIdInconnuException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun userIdInconnuHandler(e: UserIdInconnuException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un id d'utilisateur existant.")
    }

    @ExceptionHandler(ConsultationIdInconnuException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun userIdInconnuHandler(e: ConsultationIdInconnuException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un id de consultation existant.")
    }
}

class ErrorResponse(val title: String)
