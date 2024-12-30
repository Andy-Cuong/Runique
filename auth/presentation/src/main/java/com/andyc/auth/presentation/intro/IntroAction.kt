package com.andyc.auth.presentation.intro

sealed interface IntroAction {
    data object OnSignInClick: IntroAction
    data object OnSignUpClick: IntroAction
}