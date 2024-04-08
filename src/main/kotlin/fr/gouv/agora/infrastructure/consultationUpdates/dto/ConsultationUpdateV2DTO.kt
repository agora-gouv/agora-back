package fr.gouv.agora.infrastructure.consultationUpdates.dto

import jakarta.persistence.*
import org.hibernate.Hibernate
import java.util.*

@Entity(name = "consultation_updates_v2")
data class ConsultationUpdateV2DTO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID,
    @JoinTable(joinColumns = [JoinColumn(table = "consultations", referencedColumnName = "id")])
    val consultationId: UUID,
    val isVisibleToUnansweredUsersOnly: Int,
    @Column(columnDefinition = "TEXT")
    val updateLabel: String?,
    val updateDate: Date,
    @Column(columnDefinition = "TEXT")
    val shareTextTemplate: String,
    val hasQuestionsInfo: Int,
    @Column(columnDefinition = "TEXT")
    val responsesInfoPicto: String?,
    @Column(columnDefinition = "TEXT")
    val responsesInfoDescription: String?,
    @Column(columnDefinition = "TEXT")
    val responsesInfoActionText: String?,
    @Column(columnDefinition = "TEXT")
    val infoHeaderPicto: String?,
    @Column(columnDefinition = "TEXT")
    val infoHeaderDescription: String?,
    val hasParticipationInfo: Int,
    @Column(columnDefinition = "TEXT")
    val downloadAnalysisUrl: String?,
    @Column(columnDefinition = "TEXT")
    val feedbackQuestionPicto: String?,
    @Column(columnDefinition = "TEXT")
    val feedbackQuestionTitle: String?,
    @Column(columnDefinition = "TEXT")
    val feedbackQuestionDescription: String?,
    @Column(columnDefinition = "TEXT")
    val footerTitle: String?,
    @Column(columnDefinition = "TEXT")
    val footerDescription: String?,
    @Column(columnDefinition = "TEXT")
    val goals: String?,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ConsultationUpdateV2DTO

        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return this::class.simpleName + "(id=$id, consultationId=$consultationId, isVisibleToUnansweredUsersOnly=$isVisibleToUnansweredUsersOnly, updateLabel=$updateLabel, updateDate=$updateDate, hasQuestionsInfo=$hasQuestionsInfo, responsesInfoPicto=$responsesInfoPicto, responsesInfoDescription=$responsesInfoDescription, responsesInfoActionText=$responsesInfoActionText, infoHeaderPicto=$infoHeaderPicto, infoHeaderDescription=$infoHeaderDescription, hasParticipationInfo=$hasParticipationInfo, downloadAnalysisUrl=$downloadAnalysisUrl, feedbackQuestionPicto=$feedbackQuestionPicto, feedbackQuestionTitle=$feedbackQuestionTitle, feedbackQuestionDescription=$feedbackQuestionDescription, footerTitle=$footerTitle, footerDescription=$footerDescription, goals=$goals)"
    }

}