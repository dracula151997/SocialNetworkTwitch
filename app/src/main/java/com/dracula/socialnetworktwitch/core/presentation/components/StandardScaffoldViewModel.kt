package com.dracula.socialnetworktwitch.core.presentation.components

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StandardScaffoldViewModel @Inject constructor() : ViewModel() {
    private var bottomNavSelectedItem by mutableStateOf(0)

    fun setBottomNavigationSelectedItem(index: Int) {
        bottomNavSelectedItem = index
    }

    fun isSelected(index: Int): Boolean {
        return bottomNavSelectedItem == index
    }
}