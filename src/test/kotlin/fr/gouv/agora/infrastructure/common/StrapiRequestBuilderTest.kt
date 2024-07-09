package fr.gouv.agora.infrastructure.common

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class StrapiRequestBuilderTest {

    @Test
    fun `when there is multiple values to filter, then add filters to URI`() {
        // When
        val uri = StrapiRequestBuilder("consultations")
            .filterBy("nom", listOf("article", "titre", "loi"))
            .build()

        // Then
        assertThat(uri)
            .isEqualTo("consultations?pagination[pageSize]=100&populate=deep&filters[nom][\$in]=article&filters[nom][\$in]=titre&filters[nom][\$in]=loi")
    }

    @Test
    fun `when there is no values to filter, then throw exception`() {
        // When & Then
        assertThatThrownBy {
            StrapiRequestBuilder("consultations")
                .filterBy("nom", emptyList())
                .build()
        }.isInstanceOf(Exception::class.java)
    }

    @Test
    fun `returns all objects between 2 dates`() {
        // When
        val dateBetween = LocalDateTime.of(2024, 12, 25, 12, 10)
        val uri = StrapiRequestBuilder("consultations")
            .withDateBefore(dateBetween, "date_de_debut")
            .withDateAfter(dateBetween, "date_de_fin")
            .build()

        // Then
        assertThat(uri)
            .isEqualTo("consultations?pagination[pageSize]=100&populate=deep&filters[date_de_debut][\$lt]=2024-12-25T12:10:00&filters[date_de_fin][\$gt]=2024-12-25T12:10:00")
    }

    @Test
    fun `returns all objects sorted`() {
        // When
        val uri = StrapiRequestBuilder("consultations")
            .sortBy("date_de_debut", "desc")
            .build()

        // Then
        assertThat(uri).isEqualTo("consultations?pagination[pageSize]=100&populate=deep&sort[0]=date_de_debut:desc")
    }

    @Test
    fun `returns objects limited by pageSize`() {
        // When
        val uri = StrapiRequestBuilder("consultations")
            .withPageSize(8)
            .build()

        // Then
        assertThat(uri).isEqualTo("consultations?pagination[pageSize]=8&populate=deep")
    }

    @Test
    fun `returns objects with depth`() {
        // When
        val uri = StrapiRequestBuilder("consultations")
            .populate("0")
            .build()

        // Then
        assertThat(uri).isEqualTo("consultations?pagination[pageSize]=100&populate=0")
    }
}
