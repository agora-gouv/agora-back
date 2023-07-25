package fr.social.gouv.agora.usecase.qag

import edu.stanford.nlp.pipeline.StanfordCoreNLP
import org.springframework.stereotype.Component
import java.util.*

@Component
class SentenceNormalizer {

    companion object {
        private val COMMON_WORDS_TO_REMOVE = listOf("mais", "donc", "pour", "dans", "quel", "quelle", "quels", "quelles", "elle", "nous", "vous", "elles", "cette", "quoi", "quand", "comment", "pourquoi", "lorsque", "sans", "avec", "sous", "entre", "dans", "parmi", "aussi", "donc", "puis", "afin", "tous", "toutes", "toujours", "encore", "bien", "comme", "comprendre", "être", "avoir", "faire", "aller", "plus", "moins", "beaucoup", "très", "trop")

        private val pipeline: StanfordCoreNLP

        init {
            val props = Properties()
            val propertiesStream = this::class.java.classLoader.getResourceAsStream("StanfordCoreNLP-french.properties")
            propertiesStream.use {
                props.load(it)
            }
            pipeline = StanfordCoreNLP(props)
        }
    }

    fun preProcess(text: String): String {
        val sentence = pipeline.processToCoreDocument(text)
        val lemmatizedWords = ArrayList<String>()
        for (coreLabel in sentence.tokens()) {
            val lemma = coreLabel.lemma()
            if(!COMMON_WORDS_TO_REMOVE.contains(lemma) && lemma.length>3)
                lemmatizedWords.add(lemma)
        }
        return lemmatizedWords.joinToString(" ")
    }
}
