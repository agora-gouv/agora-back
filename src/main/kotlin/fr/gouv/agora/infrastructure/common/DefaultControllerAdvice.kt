package fr.gouv.agora.infrastructure.common

import fr.gouv.agora.domain.exceptions.InvalidNumberOfDepartmentsException
import fr.gouv.agora.domain.exceptions.InvalidTerritoryException
import fr.gouv.agora.usecase.consultation.exception.ConsultationNotFoundException
import fr.gouv.agora.usecase.consultation.exception.ConsultationUpdateNotFoundException
import fr.gouv.agora.usecase.notification.FcmTokenVideException
import fr.gouv.agora.usecase.notification.QagIdInconnuException
import fr.gouv.agora.usecase.notification.UserIdInconnuException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class DefaultControllerAdvice {
    @ExceptionHandler(ConsultationNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handler(e: ConsultationNotFoundException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un id de consultation existant.")
    }

    @ExceptionHandler(QagIdInconnuException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handler(e: QagIdInconnuException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un id de qag existant.")
    }

    @ExceptionHandler(FcmTokenVideException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handler(e: FcmTokenVideException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un FcmToken.")
    }

    @ExceptionHandler(UserIdInconnuException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handler(e: UserIdInconnuException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un id d'utilisateur existant.")
    }

    @ExceptionHandler(ConsultationUpdateNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handler(e: ConsultationUpdateNotFoundException): ErrorResponse {
        return ErrorResponse("Veuillez renseigner un id de contenu de consultation existant.")
    }

    @ExceptionHandler(InvalidTerritoryException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handler(e: InvalidTerritoryException): ErrorResponse {
        return ErrorResponse(e.message!!)
    }

    @ExceptionHandler(InvalidNumberOfDepartmentsException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handler(e: InvalidNumberOfDepartmentsException): ErrorResponse {
        return ErrorResponse(e.message!!)
    }
}

class ErrorResponse(val title: String)
