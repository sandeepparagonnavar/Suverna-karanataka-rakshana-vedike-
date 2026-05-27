package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CommunityDao {
    // --- Groups ---
    @Query("SELECT * FROM community_groups ORDER BY id ASC")
    fun getAllGroups(): Flow<List<CommunityGroup>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: CommunityGroup)

    @Query("SELECT COUNT(*) FROM community_groups")
    suspend fun getGroupCount(): Int

    // --- Feed Posts ---
    @Query("SELECT * FROM feed_posts WHERE groupId = :groupId AND isDeleted = 0 ORDER BY timestamp DESC")
    fun getActivePostsForGroup(groupId: Int): Flow<List<FeedPost>>

    @Query("SELECT * FROM feed_posts ORDER BY timestamp DESC")
    fun getAllPostsAdmin(): Flow<List<FeedPost>>

    @Query("SELECT * FROM feed_posts WHERE isFlagged = 1 ORDER BY timestamp DESC")
    fun getFlaggedPosts(): Flow<List<FeedPost>>

    @Query("SELECT * FROM feed_posts WHERE id = :postId")
    suspend fun getPostById(postId: Int): FeedPost?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: FeedPost)

    @Update
    suspend fun updatePost(post: FeedPost)

    @Query("UPDATE feed_posts SET isDeleted = 1 WHERE id = :postId")
    suspend fun softDeletePost(postId: Int)

    // --- Chat Messages ---
    @Query("SELECT * FROM chat_messages WHERE groupId = :groupId AND isDeleted = 0 ORDER BY timestamp ASC")
    fun getActiveChatsForGroup(groupId: Int): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages ORDER BY timestamp DESC")
    fun getAllChatsAdmin(): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages WHERE isFlagged = 1 ORDER BY timestamp DESC")
    fun getFlaggedChats(): Flow<List<ChatMessage>>

    @Query("SELECT * FROM chat_messages WHERE id = :chatId")
    suspend fun getChatById(chatId: Int): ChatMessage?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChat(chat: ChatMessage)

    @Update
    suspend fun updateChat(chat: ChatMessage)

    @Query("UPDATE chat_messages SET isDeleted = 1 WHERE id = :chatId")
    suspend fun softDeleteChat(chatId: Int)

    // --- Admin Logs ---
    @Query("SELECT * FROM admin_moderation_logs ORDER BY timestamp DESC")
    fun getAllAdminLogs(): Flow<List<AdminModerationLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdminLog(log: AdminModerationLog)
}
