package fr.gouv.agora.infrastructure.utils

object CollectionUtils {

    fun <T, R> List<T>.mapNotNullWhile(
        transformMethod: (T) -> R?,
        loopWhileCondition: (List<R>) -> Boolean,
    ): List<R> {
        val list = mutableListOf<R>()
        for (item in this) {
            if (!loopWhileCondition(list))
                break
            val transformedItem = transformMethod.invoke(item)
            if (transformedItem != null) {
                list.add(transformedItem)
            }
        }
        return list
    }

}