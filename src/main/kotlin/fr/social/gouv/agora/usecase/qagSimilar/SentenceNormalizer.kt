package fr.social.gouv.agora.usecase.qagSimilar

//@Component
//class SentenceNormalizer {
//
//    companion object {
//        private const val ANNOTATION_PIPELINE_PROPERTIES_FILE = "StanfordCoreNLP-french.properties"
//    }
//
//    fun preProcess(text: String): String {
//        val annotationPipeline = initAnnotationPipeline()
//
//        val coreDocument = annotationPipeline.processToCoreDocument(text)
//        return coreDocument.tokens().mapNotNull { token ->
//            token.lemma()
//        }.joinToString(" ")
//    }
//
//    private fun initAnnotationPipeline(): StanfordCoreNLP {
//        val props = Properties()
//        this::class.java.classLoader.getResourceAsStream(ANNOTATION_PIPELINE_PROPERTIES_FILE).use {
//            props.load(it)
//        }
//
//        return StanfordCoreNLP(props)
//    }
//}
