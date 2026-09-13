package com.example.data.remote

import android.util.Log
import com.example.data.model.MixingServices
import com.example.data.model.Tracks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

/**
 * Service handling Firebase Firestore operations for audio tracks metadata and mixing service offerings.
 */
class FirebaseDjService(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    companion object {
        private const val TAG = "FirebaseDjService"
        const val COLLECTION_TRACKS = "tracks"
        const val COLLECTION_MIXING_SERVICES = "mixing_services"
    }

    /**
     * Stores or updates an audio track metadata document in Firestore.
     */
    suspend fun saveTrack(track: Tracks): Result<String> {
        return runCatching {
            val docRef = if (track.id.isNotEmpty()) {
                firestore.collection(COLLECTION_TRACKS).document(track.id)
            } else {
                firestore.collection(COLLECTION_TRACKS).document()
            }
            docRef.set(track.toMap()).await()
            docRef.id
        }.onFailure { e ->
            Log.e(TAG, "Error saving track to Firestore: ${e.message}", e)
        }
    }

    /**
     * Realtime Flow observing all audio tracks sorted by latest creation time.
     */
    fun observeTracks(): Flow<List<Tracks>> = callbackFlow {
        val listenerRegistration = firestore.collection(COLLECTION_TRACKS)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed for tracks: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val tracksList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(Tracks::class.java)
                    }
                    trySend(tracksList)
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }

    /**
     * Stores or updates a mixing service offering document in Firestore.
     */
    suspend fun saveMixingService(service: MixingServices): Result<String> {
        return runCatching {
            val docRef = if (service.id.isNotEmpty()) {
                firestore.collection(COLLECTION_MIXING_SERVICES).document(service.id)
            } else {
                firestore.collection(COLLECTION_MIXING_SERVICES).document()
            }
            docRef.set(service.toMap()).await()
            docRef.id
        }.onFailure { e ->
            Log.e(TAG, "Error saving mixing service to Firestore: ${e.message}", e)
        }
    }

    /**
     * Realtime Flow observing all active mixing service offerings.
     */
    fun observeMixingServices(): Flow<List<MixingServices>> = callbackFlow {
        val listenerRegistration = firestore.collection(COLLECTION_MIXING_SERVICES)
            .orderBy("priceInr", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.w(TAG, "Listen failed for mixing services: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val servicesList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(MixingServices::class.java)
                    }
                    trySend(servicesList)
                }
            }

        awaitClose {
            listenerRegistration.remove()
        }
    }
}
