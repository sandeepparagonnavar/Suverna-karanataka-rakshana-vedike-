package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "community_groups")
data class CommunityGroup(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val kannadaName: String,
    val description: String,
    val descriptionKn: String,
    val iconName: String, // e.g., "book", "palette", "history", "nature"
    val postCount: Int = 0
)

@Entity(tableName = "feed_posts")
data class FeedPost(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val groupId: Int,
    val authorName: String,
    val authorRole: String = "Member", // "Member", "Moderator", "President"
    val content: String,
    val imageUrl: String? = null, // Holds a drawable template id, or a camera photo
    val likes: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val isFlagged: Boolean = false,
    val flagReason: String? = null,
    val isDeleted: Boolean = false
)

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val groupId: Int,
    val authorName: String,
    val messageText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFlagged: Boolean = false,
    val flagReason: String? = null,
    val isDeleted: Boolean = false
)

@Entity(tableName = "admin_moderation_logs")
data class AdminModerationLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val actionType: String, // "FLAG_POST", "RESTORE_POST", "SOFT_DELETE_POST", "BAN_USER", "FLAG_CHAT", "AUTO_SCAN"
    val targetId: Int,
    val targetType: String, // "POST", "CHAT"
    val details: String,
    val adminName: String,
    val timestamp: Long = System.currentTimeMillis()
)
