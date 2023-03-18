package com.example.hrrestaurant.ui.activity.loginActivity

import android.location.Location
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.hrrestaurant.data.dataSources.local.Order
import com.example.hrrestaurant.data.dataSources.remote.User
import com.example.hrrestaurant.data.repositories.Repository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginActivityViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _loggedInStatus = MutableLiveData<Boolean>()
    val loggedInStatus: LiveData<Boolean>
        get() = _loggedInStatus
    private var user: HashMap<String, java.io.Serializable> = hashMapOf()
    private val fireStoreDb: FirebaseFirestore by lazy { Firebase.firestore }

    fun userLoggedIn(userId: String, userEmail: String, userPhoneNumber: String?) {
        user["userId"] = userId
        user["userEmail"] = userEmail
        userPhoneNumber.let {
            user["userPrimaryPhoneNumber"] = userPhoneNumber!!
        }
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                fireStoreDb.collection("Users").add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d("Firebase", "user id ${documentReference.id}")
                    }.addOnFailureListener { exception ->
                        Log.d("Firebase", "Failed to register user")
                    }
            }
        }
    }

    fun addUserName(userName: String) {
        user["userName"] = userName
    }

    fun addLocation(location: String) {
        user["userLocation"] = location
    }

    fun addPrimaryPhoneNumber(phoneNumber: Int) {
        user["userPrimaryPhoneNumber"] = phoneNumber
    }

    fun addSecondaryPhoneNumber(secondaryPhoneNumber: Int) {
        user["userSecondaryPhoneNumber"] = secondaryPhoneNumber
    }

    fun changeLoginStatus(status: Boolean) {
        _loggedInStatus.postValue(status)
    }
}