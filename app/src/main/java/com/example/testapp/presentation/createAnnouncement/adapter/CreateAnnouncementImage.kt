package com.example.testapp.presentation.createAnnouncement.adapter

import android.net.Uri

data class CreateAnnouncementImage (
    val uri: Uri,
    var uploadState: UploadState
    )
enum class UploadState {
    UPLOADING,
    UPLOADED,
    FAILED
}