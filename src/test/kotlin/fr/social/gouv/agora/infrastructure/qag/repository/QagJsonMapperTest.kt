package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.profile.repository.DateMapper
import fr.social.gouv.agora.infrastructure.qag.*
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueJsonMapper
import fr.social.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class QagJsonMapperTest {

    @Autowired
    private lateinit var dateMapper: DateMapper

    @Autowired
    private lateinit var thematiqueJsonMapper: ThematiqueJsonMapper

    @Autowired
    private lateinit var mapper: QagJsonMapper

    private val qagToMap = Qag(
        id = "qagId",
        thematique = Thematique(
            id = "thematiqueId",
            label = "label",
            picto = "picto",
        ),
        title = "qag title",
        description = "qag description",
        date = Date(0),
        status = QagStatus.MODERATED_ACCEPTED,
        username = "qag username",
        userId = "userId",
        support = SupportQag(supportCount = 10, isSupportedByUser = true),
        response = null,
        feedback = false,
        feedbackResults = emptyList(),
    )

    private val expectedQagJson = QagJson(
        id = "qagId",
        thematique = ThematiqueNoIdJson(
            label = "label",
            picto = "picto",
        ),
        title = "qag title",
        description = "qag description",
        date = "",
        username = "qag username",
        canShare = true,
        canSupport = true,
        canDelete = true,
        support = SupportQagJson(supportCount = 10, isSupportedByUser = true),
        isAuthor = true,
        response = null,
    )

    private val responseQagToMap = ResponseQag(
        id = "responseId",
        author = "ministre",
        authorPortraitUrl = "authorPortraitUrl",
        authorDescription = "authorDescription",
        responseDate = Date(0),
        videoUrl = "videoUrl",
        videoWidth = 100,
        videoHeight = 100,
        transcription = "transcription",
        qagId = "qagId",
    )

    private val expectedQagJsonResponseQag = ResponseQagJson(
        author = "ministre",
        authorDescription = "authorDescription",
        responseDate = "",
        videoUrl = "videoUrl",
        videoWidth = 100,
        videoHeight = 100,
        transcription = "transcription",
        feedbackStatus = false,
        feedbackResults = null,
    )

    @Test
    fun `toJson - when response doesn't exist - should return QagJson without response`() {
        // When
        val result = mapper.toJson(qag = qagToMap, userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            expectedQagJson.copy(
                date = dateMapper.toFormattedDate(Date(0)),
                thematique = thematiqueJsonMapper.toNoIdJson(
                    Thematique(
                        id = "thematiqueId",
                        label = "label",
                        picto = "picto",
                    )
                )
            )
        )
    }

    @Test
    fun `toJson - when response exist and has no feedback - should return QagJson with feedbackResults = 0`() {
        val qagToTest = qagToMap.copy(response = responseQagToMap)
        val result = mapper.toJson(qag = qagToTest, userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            expectedQagJson.copy(
                date = dateMapper.toFormattedDate(Date(0)),
                thematique = thematiqueJsonMapper.toNoIdJson(
                    Thematique(
                        id = "thematiqueId",
                        label = "label",
                        picto = "picto",
                    )
                ),
                response = expectedQagJsonResponseQag.copy(
                    responseDate = dateMapper.toFormattedDate(Date(0)), feedbackResults = FeedbackResultsJson(
                        positiveRatio = 0,
                        negativeRatio = 0,
                        count = 0,
                    )
                ),
            )
        )
    }

    @Test
    fun `toJson - when response exist and feedbackResults is null - should return QagJson with feedbackResults = null`() {
        val qagToTest = qagToMap.copy(response = responseQagToMap, feedbackResults = null)
        val result = mapper.toJson(qag = qagToTest, userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            expectedQagJson.copy(
                date = dateMapper.toFormattedDate(Date(0)),
                thematique = thematiqueJsonMapper.toNoIdJson(
                    Thematique(
                        id = "thematiqueId",
                        label = "label",
                        picto = "picto",
                    )
                ),
                response = expectedQagJsonResponseQag.copy(
                    responseDate = dateMapper.toFormattedDate(Date(0)),
                ),
            )
        )
    }

    @Test
    fun `toJson - when response exist and feedbackResults is not null - should return QagJson with correct percentage`() {
        val qagToTest = qagToMap.copy(
            response = responseQagToMap,
            feedbackResults = listOf(
                FeedbackQag(qagId = "qagId", userId = "userId", isHelpful = false),
                FeedbackQag(qagId = "qagId", userId = "userId", isHelpful = false),
                FeedbackQag(qagId = "qagId", userId = "userId", isHelpful = true),
                FeedbackQag(qagId = "qagId", userId = "userId", isHelpful = false)
            )
        )
        val result = mapper.toJson(qag = qagToTest, userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            expectedQagJson.copy(
                date = dateMapper.toFormattedDate(Date(0)),
                thematique = thematiqueJsonMapper.toNoIdJson(
                    Thematique(
                        id = "thematiqueId",
                        label = "label",
                        picto = "picto",
                    )
                ),
                response = expectedQagJsonResponseQag.copy(
                    responseDate = dateMapper.toFormattedDate(Date(0)),
                    feedbackResults = FeedbackResultsJson(positiveRatio = 25, negativeRatio = 75, count = 4)
                ),
            )
        )
    }

    @Test
    fun `toJson - when response exist and feedbackResults is not null - should return QagJson with percentage Int`() {
        val qagToTest = qagToMap.copy(
            response = responseQagToMap,
            feedbackResults = listOf(
                FeedbackQag(qagId = "qagId", userId = "userId", isHelpful = false),
                FeedbackQag(qagId = "qagId", userId = "userId", isHelpful = true),
                FeedbackQag(qagId = "qagId", userId = "userId", isHelpful = true),
            )
        )
        val result = mapper.toJson(qag = qagToTest, userId = "userId")

        // Then
        assertThat(result).isEqualTo(
            expectedQagJson.copy(
                date = dateMapper.toFormattedDate(Date(0)),
                thematique = thematiqueJsonMapper.toNoIdJson(
                    Thematique(
                        id = "thematiqueId",
                        label = "label",
                        picto = "picto",
                    )
                ),
                response = expectedQagJsonResponseQag.copy(
                    responseDate = dateMapper.toFormattedDate(Date(0)),
                    feedbackResults = FeedbackResultsJson(positiveRatio = 67, negativeRatio = 33, count = 3)
                ),
            )
        )
    }
}