package fr.gouv.agora.usecase.suspiciousUser.repository

interface SignupCountRepository {
    fun getTodaySignupCount(ipAddressHash: String, userAgent: String): Int?
    fun initTodaySignupCount(ipAddressHash: String, userAgent: String, todaySignupCount: Int)
}