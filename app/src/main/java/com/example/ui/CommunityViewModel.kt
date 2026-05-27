package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CommunityViewModel(
    application: Application,
    private val repository: AppRepository
) : AndroidViewModel(application) {

    // --- Active Selection State ---
    private val _selectedGroupId = MutableStateFlow(1)
    val selectedGroupId: StateFlow<Int> = _selectedGroupId.asStateFlow()

    // --- Admin Settings Cache ---
    private val _isAdminMode = MutableStateFlow(false)
    val isAdminMode: StateFlow<Boolean> = _isAdminMode.asStateFlow()

    private val _currentLanguage = MutableStateFlow("kn")
    val currentLanguage: StateFlow<String> = _currentLanguage.asStateFlow()

    fun setLanguage(lang: String) {
        _currentLanguage.value = lang
    }

    private val _currentUser = MutableStateFlow("Suresh Mandya")
    val currentUser: StateFlow<String> = _currentUser.asStateFlow()

    // --- AI Audit Loading State ---
    private val _isAnalyzing = MutableStateFlow(false)
    val isAnalyzing: StateFlow<Boolean> = _isAnalyzing.asStateFlow()

    // --- Message State Alerts ---
    private val _postScanResult = MutableSharedFlow<String>()
    val postScanResult: SharedFlow<String> = _postScanResult.asSharedFlow()

    // --- Observables from Room ---
    val allGroups: StateFlow<List<CommunityGroup>> = repository.allGroups
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activePosts: StateFlow<List<FeedPost>> = _selectedGroupId
        .flatMapLatest { repository.getActivePostsForGroup(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val activeChats: StateFlow<List<ChatMessage>> = _selectedGroupId
        .flatMapLatest { repository.getActiveChatsForGroup(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flaggedPosts: StateFlow<List<FeedPost>> = repository.flaggedPosts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val flaggedChats: StateFlow<List<ChatMessage>> = repository.flaggedChats
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val adminLogs: StateFlow<List<AdminModerationLog>> = repository.adminLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            // Seed defaults
            repository.ensureSeedData()
        }
    }

    fun selectGroup(groupId: Int) {
        _selectedGroupId.value = groupId
    }

    fun setAdminMode(enabled: Boolean) {
        _isAdminMode.value = enabled
        _currentUser.value = if (enabled) "Admin Gowda" else "Suresh Mandya"
    }

    // --- Core Operations ---

    /**
     * Sends a chat message. Content is audited with Gemini AI, triggering automated flagging.
     */
    fun sendChatMessage(text: String) {
        if (text.isBlank()) return
        val currentGroup = _selectedGroupId.value
        val author = _currentUser.value

        viewModelScope.launch {
            _isAnalyzing.value = true
            val (isFlagged, reason) = GeminiModerator.auditContent(text)
            _isAnalyzing.value = false

            val chat = ChatMessage(
                groupId = currentGroup,
                authorName = author,
                messageText = text,
                isFlagged = isFlagged,
                flagReason = if (isFlagged) reason else null
            )
            repository.insertChat(chat)

            if (isFlagged) {
                // Automatically log auditing action
                repository.insertAdminLog(
                    AdminModerationLog(
                        actionType = "AUTO_SCAN",
                        targetId = chat.id,
                        targetType = "CHAT",
                        details = "AI flagged chat message by $author: \"$text\". Audit reason: $reason",
                        adminName = "Gemini AI Shield"
                    )
                )
            }
        }
    }

    /**
     * Publishes a culture post. Image is provided via key templates. Fully audited with Gemini.
     */
    fun publishPost(content: String, templateImageKey: String?) {
        if (content.isBlank()) return
        val currentGroup = _selectedGroupId.value
        val author = _currentUser.value
        val role = if (_isAdminMode.value) "President" else "Member"

        viewModelScope.launch {
            _isAnalyzing.value = true
            val (isFlagged, reason) = GeminiModerator.auditContent(content)
            _isAnalyzing.value = false

            val post = FeedPost(
                groupId = currentGroup,
                authorName = author,
                authorRole = role,
                content = content,
                imageUrl = templateImageKey,
                isFlagged = isFlagged,
                flagReason = if (isFlagged) reason else null
            )
            repository.insertPost(post)

            if (isFlagged) {
                repository.insertAdminLog(
                    AdminModerationLog(
                        actionType = "AUTO_SCAN",
                        targetId = post.id,
                        targetType = "POST",
                        details = "AI flagged post by $author: \"${content.take(40)}...\". Focus: $reason",
                        adminName = "Gemini AI Shield"
                    )
                )
                _postScanResult.emit("Substituted with flags. Check Admin panel review.")
            } else {
                _postScanResult.emit("Shared successfully!")
            }
        }
    }

    fun supportLikePost(postId: Int) {
        viewModelScope.launch {
            repository.getPostById(postId)?.let { existing ->
                repository.updatePost(existing.copy(likes = existing.likes + 1))
            }
        }
    }

    // --- Admin Control Panel Triggers ---

    fun approvePostFlag(postId: Int) {
        viewModelScope.launch {
            repository.getPostById(postId)?.let { existing ->
                repository.updatePost(existing.copy(isFlagged = false, flagReason = null))
                repository.insertAdminLog(
                    AdminModerationLog(
                        actionType = "RESTORE_POST",
                        targetId = postId,
                        targetType = "POST",
                        details = "Approved and cleared flags on post by ${existing.authorName}.",
                        adminName = _currentUser.value
                    )
                )
            }
        }
    }

    fun deletePostAdmin(postId: Int) {
        viewModelScope.launch {
            repository.getPostById(postId)?.let { existing ->
                repository.softDeletePost(postId)
                repository.insertAdminLog(
                    AdminModerationLog(
                        actionType = "SOFT_DELETE_POST",
                        targetId = postId,
                        targetType = "POST",
                        details = "Permanently deleted content from feed written by ${existing.authorName}.",
                        adminName = _currentUser.value
                    )
                )
            }
        }
    }

    fun approveChatFlag(chatId: Int) {
        viewModelScope.launch {
            repository.getChatById(chatId)?.let { existing ->
                repository.updateChat(existing.copy(isFlagged = false, flagReason = null))
                repository.insertAdminLog(
                    AdminModerationLog(
                        actionType = "RESTORE_CHAT",
                        targetId = chatId,
                        targetType = "CHAT",
                        details = "Approved and allowed message written by ${existing.authorName}: \"${existing.messageText.take(20)}...\".",
                        adminName = _currentUser.value
                    )
                )
            }
        }
    }

    fun deleteChatAdmin(chatId: Int) {
        viewModelScope.launch {
            repository.getChatById(chatId)?.let { existing ->
                repository.softDeleteChat(chatId)
                repository.insertAdminLog(
                    AdminModerationLog(
                        actionType = "SOFT_DELETE_CHAT",
                        targetId = chatId,
                        targetType = "CHAT",
                        details = "Deleted chat entry written by ${existing.authorName}: \"${existing.messageText.take(20)}...\".",
                        adminName = _currentUser.value
                    )
                )
            }
        }
    }

    /**
     * Conducts manual audit scan validation using Gemini AI logic at the click of a button!
     */
    fun performAIScanOnSelectedPost(postId: Int) {
        viewModelScope.launch {
            repository.getPostById(postId)?.let { existing ->
                _isAnalyzing.value = true
                val (isFlagged, reason) = GeminiModerator.auditContent(existing.content)
                _isAnalyzing.value = false

                repository.updatePost(existing.copy(isFlagged = isFlagged, flagReason = if (isFlagged) reason else null))
                repository.insertAdminLog(
                    AdminModerationLog(
                        actionType = "FLAG_POST",
                        targetId = postId,
                        targetType = "POST",
                        details = "Triggered manual Admin Gemini AI Scan. Flagged = $isFlagged, Reason: $reason",
                        adminName = _currentUser.value
                    )
                )
            }
        }
    }

    /**
     * Automatically inserts mock violating posts/chats to verify the Admin panel moderator controls.
     */
    fun simulateMemberViolation() {
        viewModelScope.launch {
            val count = (System.currentTimeMillis() % 1000).toInt()
            
            // Insert mock malicious feed post
            val violationPost = FeedPost(
                groupId = _selectedGroupId.value,
                authorName = "Troublemaker$count",
                authorRole = "Member",
                content = "Warning: Spam post promoting extreme casino lotteries! Buy coins to double your cash in Bengaluru overnight! spam cash casino lotto.",
                imageUrl = "spam_offer",
                isFlagged = true,
                flagReason = "Heuristics Flagged: Financial spam and gambling casino promotion detected.",
                timestamp = System.currentTimeMillis()
            )
            repository.insertPost(violationPost)

            // Insert mock abusive chat message
            val violationChat = ChatMessage(
                groupId = _selectedGroupId.value,
                authorName = "AngryGuy$count",
                messageText = "You guys are absolute idiot creators! This community is useless heritage. Down with the rules!",
                isFlagged = true,
                flagReason = "Heuristics Flagged: Inappropriate insult targeting peers and cultural alignment.",
                timestamp = System.currentTimeMillis()
            )
            repository.insertChat(violationChat)

            // Insert audit log
            repository.insertAdminLog(
                AdminModerationLog(
                    actionType = "FLAG_POST",
                    targetId = violationPost.id,
                    targetType = "POST",
                    details = "Auto-filtered Member violation content: financial casino spam by Troublemaker$count.",
                    adminName = "System Overseer"
                )
            )
        }
    }
}

class CommunityViewModelFactory(
    private val application: Application,
    private val repository: AppRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CommunityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CommunityViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
