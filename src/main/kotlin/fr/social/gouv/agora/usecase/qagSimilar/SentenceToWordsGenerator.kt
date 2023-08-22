package fr.social.gouv.agora.usecase.qagSimilar

import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory
import org.springframework.stereotype.Component

@Component
class SentenceToWordsGenerator(private val sentenceNormalizer: SentenceNormalizer) {

    fun sentenceToWords(sentence: String): List<String> {
        return DefaultTokenizerFactory().create(sentenceNormalizer.preProcess(sentence)).tokens
    }

}