package com.example.data.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.PropertyName

/**
 * Firestore data model representing mixing and music production service offerings.
 * Can be serialized to/from Firestore collections (e.g. "mixing_services").
 */
@IgnoreExtraProperties
data class MixingServices(
    @DocumentId
    val id: String = "",

    @get:PropertyName("serviceName")
    @set:PropertyName("serviceName")
    var serviceName: String = "",

    @get:PropertyName("category")
    @set:PropertyName("category")
    var category: String = "", // e.g. "FLP Project", "Stems Pack", "Custom Remix", "Dhol Loops", "Mastering"

    @get:PropertyName("description")
    @set:PropertyName("description")
    var description: String = "",

    @get:PropertyName("includedDeliverables")
    @set:PropertyName("includedDeliverables")
    var includedDeliverables: List<String> = emptyList(),

    @get:PropertyName("audioFormats")
    @set:PropertyName("audioFormats")
    var audioFormats: List<String> = emptyList(), // e.g. ["24-bit WAV", "FL Studio .flp", "320kbps MP3"]

    @get:PropertyName("priceInr")
    @set:PropertyName("priceInr")
    var priceInr: Double = 0.0,

    @get:PropertyName("originalPriceInr")
    @set:PropertyName("originalPriceInr")
    var originalPriceInr: Double = 0.0,

    @get:PropertyName("currency")
    @set:PropertyName("currency")
    var currency: String = "INR",

    @get:PropertyName("deliveryTimelineDays")
    @set:PropertyName("deliveryTimelineDays")
    var deliveryTimelineDays: Int = 3,

    @get:PropertyName("revisionsAllowed")
    @set:PropertyName("revisionsAllowed")
    var revisionsAllowed: Int = 2,

    @get:PropertyName("fileSizeBytes")
    @set:PropertyName("fileSizeBytes")
    var fileSizeBytes: Long = 0L,

    @get:PropertyName("fileSizeDisplay")
    @set:PropertyName("fileSizeDisplay")
    var fileSizeDisplay: String = "",

    @get:PropertyName("previewAudioUrl")
    @set:PropertyName("previewAudioUrl")
    var previewAudioUrl: String = "",

    @get:PropertyName("isAvailable")
    @set:PropertyName("isAvailable")
    var isAvailable: Boolean = true,

    @get:PropertyName("isBestSeller")
    @set:PropertyName("isBestSeller")
    var isBestSeller: Boolean = false,

    @get:PropertyName("contactWhatsApp")
    @set:PropertyName("contactWhatsApp")
    var contactWhatsApp: String = "",

    @get:PropertyName("instantDriveDelivery")
    @set:PropertyName("instantDriveDelivery")
    var instantDriveDelivery: Boolean = true,

    @get:PropertyName("createdAt")
    @set:PropertyName("createdAt")
    var createdAt: Long = System.currentTimeMillis(),

    @get:PropertyName("updatedAt")
    @set:PropertyName("updatedAt")
    var updatedAt: Long = System.currentTimeMillis()
) {
    /**
     * Converts model to Map representation for Firestore document writes.
     */
    fun toMap(): Map<String, Any?> = mapOf(
        "serviceName" to serviceName,
        "category" to category,
        "description" to description,
        "includedDeliverables" to includedDeliverables,
        "audioFormats" to audioFormats,
        "priceInr" to priceInr,
        "originalPriceInr" to originalPriceInr,
        "currency" to currency,
        "deliveryTimelineDays" to deliveryTimelineDays,
        "revisionsAllowed" to revisionsAllowed,
        "fileSizeBytes" to fileSizeBytes,
        "fileSizeDisplay" to fileSizeDisplay,
        "previewAudioUrl" to previewAudioUrl,
        "isAvailable" to isAvailable,
        "isBestSeller" to isBestSeller,
        "contactWhatsApp" to contactWhatsApp,
        "instantDriveDelivery" to instantDriveDelivery,
        "createdAt" to createdAt,
        "updatedAt" to updatedAt
    )

    companion object {
        /**
         * Creates a MixingServices model from a local MixingPack.
         */
        fun fromMixingPack(pack: MixingPack, djPhone: String = "+919876543210"): MixingServices {
            return MixingServices(
                id = pack.id.toString(),
                serviceName = pack.title,
                category = pack.category,
                description = pack.description,
                includedDeliverables = pack.includedItems.split(",").map { it.trim() }.filter { it.isNotEmpty() },
                audioFormats = listOf(pack.format),
                priceInr = pack.priceInr.toDouble(),
                originalPriceInr = pack.originalPriceInr.toDouble(),
                fileSizeDisplay = pack.fileSize,
                isAvailable = true,
                isBestSeller = pack.isBestSeller,
                contactWhatsApp = djPhone,
                instantDriveDelivery = true
            )
        }
    }
}

/**
 * Typealias for developers or code referring to singular MixingService.
 */
typealias MixingService = MixingServices
