package com.soujunior.petjournal.ui.appArea.pets.petNameAndGenderScreen

/**
 * Classe responsável por controlar os eventos (entrada de dados, botões, etc) na tela NameAndGender
 * */
sealed class NameGenderFormEvent{
    data class PetName(val petName: String) : NameGenderFormEvent()
    data class PetGender (val petGender: Char) : NameGenderFormEvent()

    object ReturnButton : NameGenderFormEvent()

    object NextButton : NameGenderFormEvent()

}
