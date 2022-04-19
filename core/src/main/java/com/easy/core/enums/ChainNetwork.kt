package com.easy.core.enums

val SUPPORT_NETWORKS = listOf(
    ChainNetwork.MAIN, ChainNetwork.ROPSTEN, ChainNetwork.RINKEBY
)

enum class ChainNetwork(
    val label: String
) {
    MAIN("MainNet"), ROPSTEN("Ropsten"), RINKEBY("Rinkeby")
}