package com.easy.assets.presentation.send

import com.easy.core.common.UiText

sealed class SendingFormEvent {
    data class AmountChanged(val amount: String): SendingFormEvent()
    data class AddressChanged(val address: String): SendingFormEvent()
    data class ActionChanged(val action: Action): SendingFormEvent()

    object Submit: SendingFormEvent()
    object Broadcast: SendingFormEvent()
}