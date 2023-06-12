package com.soujunior.petjournal.ui.accountManager.changePasswordScreen

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.soujunior.domain.entities.auth.PasswordModel
import com.soujunior.domain.repository.ValidationRepository
import com.soujunior.domain.usecase.auth.ChangePasswordUseCase
import com.soujunior.domain.usecase.auth.util.ValidationResult
import com.soujunior.petjournal.ui.ValidationEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModelImpl(
    private val changePassword: ChangePasswordUseCase,
    private val validation: ValidationRepository
) : ChangePasswordViewModel() {
    override var state by mutableStateOf(ChangePasswordFormState())
    override val validationEventChannel = Channel<ValidationEvent>()
    override val validationEvents = validationEventChannel.receiveAsFlow()
    override val success = MutableLiveData<String>()
    override val error = MutableLiveData<String>()


    override fun success(result: String) {
        this.success.value = result
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }
    }

    override fun failed(exception: Throwable?) {
        if (exception is Error) {
            viewModelScope.launch { validationEventChannel.send(ValidationEvent.Failed) }
            this.error.value = exception.message
        } else {
            viewModelScope.launch { validationEventChannel.send(ValidationEvent.Failed) }
            this.error.value = "lançar um erro aqui"
        }
    }

    override fun enableButton(): Boolean {
        val passwordResult = validation.validatePassword(password = state.password)
        val repeatedPasswordResult = validation.validateRepeatedPassword(
            repeatedPassword = state.repeatedPassword,
            password = state.password
        )

        return state.password.isNotBlank() &&
               state.repeatedPassword.isNotBlank() &&
               passwordResult.errorMessage == null &&
               repeatedPasswordResult.errorMessage == null
    }

    private fun hasError(result: ValidationResult): Boolean {
        return listOf(result).any { !it.success }
    }

    private fun change(
        password: String? = null,
        repeatedPassword: String? = null,
        disconnect: Boolean? = null
    ) {
        when {
            password != null -> {
                state = state.copy(password = password)
                val passwordResult = validation.validatePassword(
                    password = state.password
                )
                state =
                    if (hasError(passwordResult)) state.copy(passwordError = passwordResult.errorMessage)
                    else state.copy(passwordError = null)
                change(repeatedPassword = state.repeatedPassword)
            }

            repeatedPassword != null -> {
                state = state.copy(repeatedPassword = repeatedPassword)
                val repeatedPasswordResult = validation.validateRepeatedPassword(
                    repeatedPassword = state.repeatedPassword,
                    password = state.password
                )
                state =
                    if (hasError(repeatedPasswordResult)) state.copy(repeatedPasswordError = repeatedPasswordResult.errorMessage)
                    else state.copy(repeatedPasswordError = null)
            }

            disconnect != null -> {
                state = state.copy(disconnectOtherDevices = disconnect)
            }
        }
    }

    override fun onEvent(event: ChangePasswordFormEvent) {
        when (event) {
            is ChangePasswordFormEvent.PasswordChanged -> {
                change(password = event.password)
                Log.e("testar", "${state.password}")
            }
            is ChangePasswordFormEvent.ConfirmPasswordChanged -> {
                change(repeatedPassword = event.confirmPassword)
                Log.e("testar", "${state.repeatedPassword}")
            }
            is ChangePasswordFormEvent.DisconnectOtherDevices -> {
                change(disconnect = event.disconnect)
                Log.e("testar", "${state.disconnectOtherDevices}")
            }
            is ChangePasswordFormEvent.Submit -> submitNewPassword()
        }
    }

    override fun disconnectOtherDevices() {
        if (state.disconnectOtherDevices){
            //TODO: criar metodo para desconectar outros dispositivos
        }
    }
    override fun submitNewPassword() {
        disconnectOtherDevices()
        viewModelScope.launch {
            val result = changePassword.execute(
                PasswordModel(
                    password = state.password
                )
            )
            result.handleResult(::success, ::failed)
        }
    }
}