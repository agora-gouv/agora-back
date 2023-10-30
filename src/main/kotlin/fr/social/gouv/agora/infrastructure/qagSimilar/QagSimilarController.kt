package fr.social.gouv.agora.infrastructure.qagSimilar

//@RestController
//@Suppress("unused")
//class QagSimilarController(
//    private val featureFlagsUseCase: FeatureFlagsUseCase,
//    private val findSimilarQagUseCase: FindSimilarQagUseCase,
//    private val mapper: QagModeratingJsonMapper,
//) {
//
//    @GetMapping("/qags/has_similar")
//    fun hasSimilarQag(
//        @RequestHeader("Authorization") authorizationHeader: String,
//        @RequestParam("title") title: String,
//    ): ResponseEntity<*> {
//        if (!featureFlagsUseCase.isFeatureEnabled(AgoraFeature.SimilarQag)) {
//            return ResponseEntity.badRequest().body(Unit)
//        }
//
//        val qagResult = findSimilarQagUseCase.findSimilarQags(
//            writingQag = title,
//            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
//        )
//        return if (qagResult.isEmpty())
//            ResponseEntity.ok().body(QagHasSimilarJson(hasSimilar = false))
//        else
//            ResponseEntity.ok().body(QagHasSimilarJson(hasSimilar = true))
//    }
//
//    @GetMapping("/qags/similar")
//    fun getSimilarQagList(
//        @RequestHeader("Authorization") authorizationHeader: String,
//        @RequestParam("title")
//        title: String,
//    ): ResponseEntity<*> {
//        if (!featureFlagsUseCase.isFeatureEnabled(AgoraFeature.SimilarQag)) {
//            return ResponseEntity.badRequest().body(Unit)
//        }
//
//        val qagResult = findSimilarQagUseCase.findSimilarQags(
//            writingQag = title,
//            userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader),
//        )
//        return if (qagResult.isEmpty())
//            ResponseEntity.ok().body(Unit)
//        else
//            ResponseEntity.ok().body(mapper.toJson(qagResult))
//    }
//
//}