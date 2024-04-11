package fr.gouv.agora.usecase.suspiciousUser.repository

interface SignupCountRepository {
    fun getTodaySignupCount(ipAddressHash: String): Int?
    fun initTodaySignupCount(ipAddressHash: String, todaySignupCount: Int)
}