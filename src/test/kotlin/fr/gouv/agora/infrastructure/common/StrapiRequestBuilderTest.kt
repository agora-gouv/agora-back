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
            .filterIn("nom", listOf("article", "titre", "loi"))
            .build()

        // Then
        assertThat(uri)
            .isEqualTo("consultations?pagination[pageSize]=100&populate=*&filters[nom][\$in]=article&filters[nom][\$in]=titre&filters[nom][\$in]=loi")
    }

    @Test
    fun `when there is a complex field to filter, then add filters to URI`() {
        // When
        val uri = StrapiRequestBuilder("fiche-inventaires")
            .filterIn(listOf("thematique", "label"), listOf("thematique"))
            .build()

        // Then
        assertThat(uri)
            .contains("filters[thematique][label][\$in]=thematique")
    }

    @Test
    fun `when there is a complex field with thematique and etape, then add filters to URI`() {
        // When
        val uri = StrapiRequestBuilder("fiche-inventaires")
            .filterIn(listOf("etape"), listOf("etape"))
            .build()

        // Then
        assertThat(uri)
            .contains("filters[etape][\$in]=etape")
    }

    @Test
    fun `when there is a complex field with thematique and etape and modalite_participation, then add filters to URI`() {
        // When
        val uri = StrapiRequestBuilder("fiche-inventaires")
            .filterIn(listOf("modalite_participation"), listOf("modalite_participation"))
            .build()

        // Then
        assertThat(uri)
            .contains("filters[modalite_participation][\$in]=modalite_participation")
    }

    @Test
    fun `when there is a complex field with annee de lancement, then add filters to URI`() {
        // When
        val uri = StrapiRequestBuilder("fiche-inventaires")
            .filterIn(listOf("annee_de_lancement"), listOf("annee_de_lancement"))
            .build()

        // Then
        assertThat(uri)
            .contains("filters[annee_de_lancement][\$in]=annee_de_lancement")
    }

    @Test
    fun `when there is a complex field with titre, then add filters to URI`() {
        // When
        val uri = StrapiRequestBuilder("fiche-inventaires")
            .contains("titre", "titre")
            .build()

        // Then
        assertThat(uri)
            .contains("filters[titre][\$containsi]=titre")
    }

    @Test
    fun `when there is no values to filter, then throw exception`() {
        // When & Then
        assertThatThrownBy {
            StrapiRequestBuilder("consultations")
                .filterIn("nom", emptyList())
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
            .isEqualTo("consultations?pagination[pageSize]=100&populate=*&filters[date_de_debut][\$lt]=2024-12-25T12:10:00&filters[date_de_fin][\$gt]=2024-12-25T12:10:00")
    }

    @Test
    fun `returns all objects sorted`() {
        // When
        val uri = StrapiRequestBuilder("consultations")
            .sortBy("date_de_debut", "desc")
            .build()

        // Then
        assertThat(uri).isEqualTo("consultations?pagination[pageSize]=100&populate=*&sort[0]=date_de_debut:desc")
    }

    @Test
    fun `returns objects limited by pageSize`() {
        // When
        val uri = StrapiRequestBuilder("consultations")
            .withPageSize(8)
            .build()

        // Then
        assertThat(uri).isEqualTo("consultations?pagination[pageSize]=8&populate=*")
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

    @Test
    fun `getByIds - when given a list of documentIds - should filter by documentId`() {
        // Given
        val ids = listOf("abc123def456", "xyz789ghi012")

        // When
        val uri = StrapiRequestBuilder("consultations")
            .getByIds(ids)
            .build()

        // Then
        assertThat(uri)
            .isEqualTo("consultations?pagination[pageSize]=100&populate=*&filters[documentId][\$in]=abc123def456&filters[documentId][\$in]=xyz789ghi012")
    }

    @Test
    fun `withUnpublished - when called - should use status=draft`() {
        // When
        val uri = StrapiRequestBuilder("consultations")
            .withUnpublished()
            .build()

        // Then
        assertThat(uri).isEqualTo("consultations?pagination[pageSize]=100&status=draft&populate=*")
    }
}
