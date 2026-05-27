package com.example.ui

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AdminModerationLog
import com.example.data.ChatMessage
import com.example.data.CommunityGroup
import com.example.data.FeedPost
import com.example.ui.theme.KarnatakaGold
import com.example.ui.theme.KarnatakaRed
import com.example.ui.theme.KarnatakaGoldLight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainLayout(viewModel: CommunityViewModel, modifier: Modifier = Modifier) {
    // --- State collections ---
    val groups by viewModel.allGroups.collectAsState()
    val selectedId by viewModel.selectedGroupId.collectAsState()
    val activePosts by viewModel.activePosts.collectAsState()
    val activeChats by viewModel.activeChats.collectAsState()
    val flaggedPosts by viewModel.flaggedPosts.collectAsState()
    val flaggedChats by viewModel.flaggedChats.collectAsState()
    val adminLogs by viewModel.adminLogs.collectAsState()
    val isAdminMode by viewModel.isAdminMode.collectAsState()
    val isAnalyzing by viewModel.isAnalyzing.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val currentLanguage by viewModel.currentLanguage.collectAsState()
    val strings = getAppStrings(currentLanguage)

    var activeTab by remember { mutableStateOf(0) } // 0 = Chats, 1 = Photo Feed, 2 = Admin Cabin
    var showCreatePostDialog by remember { mutableStateOf(false) }

    val activeGroup = groups.find { it.id == selectedId }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(KarnatakaRed, Color(0xFF9E1B27))
                        )
                    )
                    .safeDrawingPadding()
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = "SVRK Logo",
                                tint = KarnatakaGold,
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 6.dp)
                            )
                            Text(
                                text = strings.appTitle,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 2.sp
                                ),
                                color = KarnatakaGold
                            )
                        }
                        Text(
                            text = strings.appSubtitle,
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            ),
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }

                    // Toolbar Actions: Language Toggle + Admin Toggle
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Language Segmented Switch Toggle Component
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(Color.Black.copy(alpha = 0.25f), RoundedCornerShape(16.dp))
                                .padding(3.dp)
                        ) {
                            listOf("kn" to "ಕನ್ನಡ", "en" to "EN").forEach { (code, label) ->
                                val isSelected = currentLanguage == code
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(if (isSelected) KarnatakaGold else Color.Transparent)
                                        .clickable { viewModel.setLanguage(code) }
                                        .testTag("lang_toggle_$code")
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = if (isSelected) Color(0xFF1E1C1A) else Color.White
                                    )
                                }
                            }
                        }

                        // Admin Switch Trigger Button Row
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = if (isAdminMode) KarnatakaGold else Color.White.copy(alpha = 0.15f),
                            modifier = Modifier
                                .clickable {
                                    viewModel.setAdminMode(!isAdminMode)
                                }
                                .testTag("admin_toggle_btn")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isAdminMode) Icons.Default.AdminPanelSettings else Icons.Default.PersonOutline,
                                    contentDescription = "Admin Indicator",
                                    tint = if (isAdminMode) Color(0xFF1E1C1A) else Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = if (isAdminMode) strings.adminModeOn else strings.memberMode,
                                    fontWeight = FontWeight.Bold,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (isAdminMode) Color(0xFF1E1C1A) else Color.White
                                )
                            }
                        }
                    }
                }

                // Slogan banner bar
                Box(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Black.copy(alpha = 0.25f))
                        .padding(vertical = 4.dp, horizontal = 12.dp)
                ) {
                    Text(
                        text = "${strings.slogan} • ${strings.bannerUser}: $currentUser",
                        color = KarnatakaGoldLight,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        maxLines = 1
                    )
                }
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Group Selection Row Selector
            if (groups.isNotEmpty()) {
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(vertical = 10.dp)) {
                        Text(
                            text = strings.selectInterestGroup,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 6.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(groups) { group ->
                                val isSelected = group.id == selectedId
                                GroupFilterPill(
                                    group = group,
                                    isSelected = isSelected,
                                    currentLanguage = currentLanguage,
                                    onClick = { viewModel.selectGroup(group.id) }
                                )
                            }
                        }
                    }
                }
            }

            // Tab bar switcher
            TabRow(
                selectedTabIndex = activeTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = KarnatakaRed,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[activeTab]),
                        color = KarnatakaRed
                    )
                }
            ) {
                Tab(
                    selected = activeTab == 0,
                    onClick = { activeTab = 0 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.tabChat, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    },
                    modifier = Modifier.testTag("tab_chat")
                )
                Tab(
                    selected = activeTab == 1,
                    onClick = { activeTab = 1 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(strings.tabFeed, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }
                    },
                    modifier = Modifier.testTag("tab_feed")
                )
                Tab(
                    selected = activeTab == 2,
                    onClick = { activeTab = 2 },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AdminPanelSettings,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                strings.tabModeration, 
                                fontWeight = FontWeight.SemiBold, 
                                fontSize = 13.sp,
                                color = if (isAdminMode) KarnatakaRed else LocalContentColor.current
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_admin")
                )
            }

            // AI Analysing floating status alert indicator
            AnimatedVisibility(
                visible = isAnalyzing,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(KarnatakaGold)
                        .padding(vertical = 4.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularProgressIndicator(
                            color = Color(0xFF1E1C1A),
                            modifier = Modifier.size(14.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = strings.geminiShieldAnalyzing,
                            color = Color(0xFF1E1C1A),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Render Tab Content
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (activeTab) {
                    0 -> ChatTabContent(
                        chats = activeChats,
                        groupName = if (currentLanguage == "kn") (activeGroup?.kannadaName ?: "ಕನ್ನಡ ವೇದಿಕೆ") else (activeGroup?.name ?: "Vedike Lounge"),
                        currentUser = currentUser,
                        viewModel = viewModel,
                        strings = strings
                    )
                    1 -> PhotoFeedTabContent(
                        posts = activePosts,
                        groupName = if (currentLanguage == "kn") (activeGroup?.kannadaName ?: "ಕನ್ನಡ ವೇದಿಕೆ") else (activeGroup?.name ?: "Photos"),
                        viewModel = viewModel,
                        onOpenCreate = { showCreatePostDialog = true },
                        strings = strings
                    )
                    2 -> ModeratorTabContent(
                        isAdmin = isAdminMode,
                        posts = flaggedPosts,
                        chats = flaggedChats,
                        logs = adminLogs,
                        viewModel = viewModel,
                        strings = strings
                    )
                }
            }
        }
    }

    // Modal dialogue to create a new cultural post
    if (showCreatePostDialog) {
        CreatePostDialog(
            groupName = if (currentLanguage == "kn") (activeGroup?.kannadaName ?: "ಕನ್ನಡ ವೇದಿಕೆ") else (activeGroup?.name ?: "Photos"),
            strings = strings,
            onDismiss = { showCreatePostDialog = false },
            onSubmit = { content, imgKey ->
                viewModel.publishPost(content, imgKey)
                showCreatePostDialog = false
            }
        )
    }
}

// ==========================================
// Filter Pills Component
// ==========================================
@Composable
fun GroupFilterPill(
    group: CommunityGroup,
    isSelected: Boolean,
    currentLanguage: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) KarnatakaRed else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) BorderStroke(1.dp, KarnatakaGold) else null,
        modifier = Modifier
            .clickable(onClick = onClick)
            .testTag("group_pill_${group.id}"),
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (group.iconName) {
                    "book" -> Icons.Default.MenuBook
                    "castle" -> Icons.Default.Fort
                    "music" -> Icons.Default.Audiotrack
                    "nature" -> Icons.Default.Forest
                    else -> Icons.Default.Groups
                },
                contentDescription = null,
                tint = if (isSelected) KarnatakaGold else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = if (currentLanguage == "kn") group.kannadaName else group.name,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    lineHeight = 13.sp
                )
                Text(
                    text = if (currentLanguage == "kn") group.name else group.kannadaName,
                    color = if (isSelected) KarnatakaGoldLight else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 10.sp
                )
            }
        }
    }
}

// ==========================================
// CHAT TAB VIEW
// ==========================================
@Composable
fun ChatTabContent(
    chats: List<ChatMessage>,
    groupName: String,
    currentUser: String,
    viewModel: CommunityViewModel,
    strings: AppStrings
) {
    var textInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()) {
        // Chat room banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f))
                .padding(vertical = 8.dp, horizontal = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.MapsUgc,
                    contentDescription = null,
                    tint = KarnatakaRed,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${strings.activeRoomChat}: $groupName (${strings.activeRoomRealtime})",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // Chat stream
        if (chats.isEmpty()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.ChatBubbleOutline,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = strings.noMessagesTitle,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = strings.noMessagesSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(chats) { chat ->
                    ChatBubble(chat = chat, currentUser = currentUser, strings = strings)
                }
            }
        }

        // Action controls (Spam test shortcut + Chat textfield)
        Surface(
            tonalElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                // Testing simulated tool to catch trolls
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = strings.mockViolationsPrompt,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                    Button(
                        onClick = { viewModel.simulateMemberViolation() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer
                        ),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier
                            .height(24.dp)
                            .testTag("simulate_spam_btn")
                    ) {
                        Text(strings.simulateSpamBtn, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = { Text(strings.chatInputPlaceholder) },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("chat_input"),
                        shape = RoundedCornerShape(24.dp),
                        colors = TextFieldDefaults.colors(
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    if (textInput.isNotBlank()) {
                                        viewModel.sendChatMessage(textInput)
                                        textInput = ""
                                    }
                                },
                                modifier = Modifier.testTag("send_chat_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Send,
                                    contentDescription = "Send text",
                                    tint = KarnatakaRed
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(chat: ChatMessage, currentUser: String, strings: AppStrings) {
    val isMyMessage = chat.authorName == currentUser
    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
    val formattedTime = format.format(Date(chat.timestamp))

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isMyMessage) Alignment.End else Alignment.Start
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = chat.authorName,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = if (isMyMessage) KarnatakaRed else MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = formattedTime,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        
        Surface(
            shape = RoundedCornerShape(
                topStart = 16.dp, 
                topEnd = 16.dp, 
                bottomStart = if (isMyMessage) 16.dp else 0.dp, 
                bottomEnd = if (isMyMessage) 0.dp else 16.dp
            ),
            color = if (chat.isFlagged) {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)
            } else if (isMyMessage) {
                KarnatakaRed.copy(alpha = 0.08f)
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            },
            border = if (chat.isFlagged) BorderStroke(1.dp, MaterialTheme.colorScheme.error) else null
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                if (chat.isFlagged) {
                    Text(
                        text = chat.messageText,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodyMedium.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Warning, contentDescription = null, modifier = Modifier.size(12.dp), tint = MaterialTheme.colorScheme.error)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                "${strings.moderationReviewPending}: ${chat.flagReason ?: "Flagged content scan"}",
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                } else {
                    Text(
                        text = chat.messageText,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

// ==========================================
// CO_FOUND / FORUM PHOTO FEED VIEW
// ==========================================
@Composable
fun PhotoFeedTabContent(
    posts: List<FeedPost>,
    groupName: String,
    viewModel: CommunityViewModel,
    onOpenCreate: () -> Unit,
    strings: AppStrings
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (posts.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.NoPhotography,
                        contentDescription = null,
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = strings.emptyPhotosTitle,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = strings.emptyPhotosSubtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onOpenCreate,
                        colors = ButtonDefaults.buttonColors(containerColor = KarnatakaRed)
                    ) {
                        Text(strings.shareFirstPostBtn, color = Color.White)
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 80.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    Text(
                        text = "$groupName - ${strings.photoFeedTitleSuffix}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = KarnatakaRed,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
                items(posts) { post ->
                    FeedPostCard(post = post, onLike = { viewModel.supportLikePost(post.id) }, strings = strings)
                }
            }
        }

        // Floating Action Button
        LargeFloatingActionButton(
            onClick = onOpenCreate,
            containerColor = KarnatakaGold,
            contentColor = Color(0xFF1E1C1A),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .testTag("create_post_fab")
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.AddAPhoto, contentDescription = "Add Post")
                Spacer(modifier = Modifier.width(8.dp))
                Text(strings.addNewPostBtn, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun FeedPostCard(post: FeedPost, onLike: () -> Unit, strings: AppStrings) {
    val format = SimpleDateFormat("d MMM, hh:mm a", Locale.getDefault())
    val dateText = format.format(Date(post.timestamp))

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("feed_card_${post.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (post.isFlagged) {
                MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = if (post.isFlagged) BorderStroke(1.2.dp, MaterialTheme.colorScheme.error) else BorderStroke(0.6.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Initial Letter Avatar
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(KarnatakaRed, KarnatakaGold)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = post.authorName.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = post.authorName,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        // Badges reflecting structural identity representation
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = when (post.authorRole) {
                                "President" -> KarnatakaGold
                                "Moderator" -> KarnatakaRed
                                else -> MaterialTheme.colorScheme.surfaceVariant
                            },
                            contentColor = when (post.authorRole) {
                                "President" -> Color(0xFF1E1C1A)
                                "Moderator" -> Color.White
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        ) {
                            Text(
                                text = post.authorRole,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Text(
                        text = dateText,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
            }

            // Post content
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
                color = MaterialTheme.colorScheme.onSurface
            )

            // Render cultural illustration media safely
            if (post.imageUrl != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(Color(0xFF1E1C1A))
                ) {
                    TemplateImage(post.imageUrl)
                }
            }

            // Flags indicator for review
            if (post.isFlagged) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.error.copy(alpha = 0.15f))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                strings.autoFlaggedTitle,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                post.flagReason ?: "Triggered community moderation safety standards.",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.8f)
                            )
                        }
                    }
                }
            }

            // Bottom Actions / Likes Bar
            Divider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onLike) {
                    Icon(
                        imageVector = Icons.Outlined.ThumbUp,
                        contentDescription = "Like",
                        tint = KarnatakaRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Text(
                    text = "${post.likes} ${strings.likesCountSuffix}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// ==========================================
// CUSTOM VECTOR LANDMARKS RENDERING
// ==========================================
@Composable
fun TemplateImage(key: String) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        when (key) {
            "hampi_chariot" -> {
                // Golden desert hampi stone chariot
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFEE58), Color(0xFFFF9800))
                    )
                )
                // Draw majestic Hampi stone wheel
                drawCircle(
                    color = Color(0xFF6D4C41),
                    radius = 35f,
                    center = Offset(width * 0.3f, height * 0.65f)
                )
                drawCircle(
                    color = Color(0xFF3E2723),
                    radius = 12f,
                    center = Offset(width * 0.3f, height * 0.65f)
                )
                drawCircle(
                    color = Color(0xFF6D4C41),
                    radius = 35f,
                    center = Offset(width * 0.7f, height * 0.65f)
                )
                drawCircle(
                    color = Color(0xFF3E2723),
                    radius = 12f,
                    center = Offset(width * 0.7f, height * 0.65f)
                )

                // Draw solid temple blocks
                val path = Path().apply {
                    moveTo(width * 0.15f, height * 0.5f)
                    lineTo(width * 0.85f, height * 0.5f)
                    lineTo(width * 0.8f, height * 0.25f)
                    lineTo(width * 0.20f, height * 0.25f)
                    close()
                }
                drawPath(path = path, color = Color(0xFF8D6E63))
                
                // Draw outline gold banner
                drawRect(
                    color = KarnatakaGold,
                    size = Size(width, 36f),
                    topLeft = Offset(0f, 0f)
                )
            }
            "kuvempu_banner" -> {
                // Sunset literary theme
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(Color(0xFFE91E63), Color(0xFF880E4F)),
                        center = Offset(width / 2f, height / 2f)
                    )
                )
                // Ancient writings quill representations
                drawCircle(
                    color = KarnatakaGold.copy(alpha = 0.5f),
                    radius = 80f,
                    center = Offset(width * 0.8f, height * 0.3f)
                )
                // Line sketches
                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(40f, height * 0.2f),
                    end = Offset(width - 40f, height * 0.2f),
                    strokeWidth = 4f
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(40f, height * 0.5f),
                    end = Offset(width - 40f, height * 0.5f),
                    strokeWidth = 4f
                )
            }
            "yakshagana_perf" -> {
                // Dramatic folk dance
                drawRect(
                    brush = Brush.horizontalGradient(
                        colors = listOf(Color(0xFFD84315), Color(0xFF3E2723))
                    )
                )
                // Yakshagana gold circle halo (Kireeta outline)
                drawCircle(
                    color = KarnatakaGold,
                    radius = 70f,
                    center = Offset(width / 2, height / 2 - 10f),
                    style = Stroke(width = 8f)
                )
                drawCircle(
                    color = KarnatakaRed,
                    radius = 45f,
                    center = Offset(width / 2, height / 2 - 10f)
                )
            }
            "nature", "nature_scen" -> {
                // Forest Western Ghats
                drawRect(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF26A69A), Color(0xFF1B5E20))
                    )
                )
                // Western Ghat Mountain ridges
                val trail = Path().apply {
                    moveTo(0f, height)
                    lineTo(width * 0.35f, height * 0.45f)
                    lineTo(width * 0.65f, height * 0.65f)
                    lineTo(width * 0.9f, height * 0.35f)
                    lineTo(width, height)
                    close()
                }
                drawPath(path = trail, color = Color(0xFF004D40))
                
                // Sun
                drawCircle(Color(0xFFFFD54F), radius = 25f, center = Offset(width * 0.8f, height * 0.25f))
            }
            else -> {
                // Fallback elegant logo symbol drawing
                drawRect(
                    brush = Brush.linearGradient(
                        colors = listOf(KarnatakaRed, KarnatakaGold)
                    )
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.15f),
                    radius = 120f,
                    center = Offset(width / 2, height / 2)
                )
            }
        }
    }
}

// ==========================================
// ADMIN MODERATION CONTROL PANEL TAB
// ==========================================
@Composable
fun ModeratorTabContent(
    isAdmin: Boolean,
    posts: List<FeedPost>,
    chats: List<ChatMessage>,
    logs: List<AdminModerationLog>,
    viewModel: CommunityViewModel,
    strings: AppStrings
) {
    if (!isAdmin) {
        // Locked status fallback display
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Protected Panel",
                    modifier = Modifier.size(64.dp),
                    tint = KarnatakaRed.copy(alpha = 0.3f)
                )
                Spacer(modifier = Modifier.height(14.dp))
                Text(
                    text = strings.presidentControlPanel, // Kannada title
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = strings.adminControlCabin,
                    style = MaterialTheme.typography.titleMedium,
                    color = KarnatakaRed,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = strings.adminRestrictedNotice,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.widthIn(max = 450.dp)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = { viewModel.setAdminMode(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = KarnatakaRed),
                    modifier = Modifier.testTag("unlock_panel_btn")
                ) {
                    Text(strings.unlockAdminCabinBtn, color = Color.White)
                }
            }
        }
    } else {
        var subTab by remember { mutableStateOf(0) } // 0 = Flagged Posts, 1 = Flagged Chats, 2 = System Log Logs

        Column(modifier = Modifier.fillMaxSize()) {
            // Stats summary headers
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 4.dp,
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Posts count
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(strings.postFlagsLabel, style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${posts.size}",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    // Chats count
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.6f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(strings.chatFlagsLabel, style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${chats.size}",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                    // Total incidents logged
                    Card(modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(strings.totalLogsLabel, style = MaterialTheme.typography.labelSmall)
                            Text(
                                "${logs.size}",
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                }
            }

            // Sub Tab select row for moderation items
            TabRow(
                selectedTabIndex = subTab,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = KarnatakaRed,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[subTab]),
                        color = KarnatakaRed
                    )
                }
            ) {
                Tab(
                    selected = subTab == 0,
                    onClick = { subTab = 0 },
                    text = { Text("${strings.tabFlaggedPosts} (${posts.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("admin_sub_posts")
                )
                Tab(
                    selected = subTab == 1,
                    onClick = { subTab = 1 },
                    text = { Text("${strings.tabFlaggedChats} (${chats.size})", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("admin_sub_chats")
                )
                Tab(
                    selected = subTab == 2,
                    onClick = { subTab = 2 },
                    text = { Text(strings.tabModLogs, fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("admin_sub_logs")
                )
            }

            // Sub tab contents rendering
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                when (subTab) {
                    0 -> {
                        if (posts.isEmpty()) {
                            AdminEmptyPlaceholder(strings.noPostsFlaggedPlaceholder)
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(posts) { post ->
                                    FlaggedPostItemCard(post = post, viewModel = viewModel, strings = strings)
                                }
                            }
                        }
                    }
                    1 -> {
                        if (chats.isEmpty()) {
                            AdminEmptyPlaceholder(strings.noChatsFlaggedPlaceholder)
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(chats) { chat ->
                                    FlaggedChatItemCard(chat = chat, viewModel = viewModel, strings = strings)
                                }
                            }
                        }
                    }
                    2 -> {
                        if (logs.isEmpty()) {
                            AdminEmptyPlaceholder(strings.noLogsPlaceholder)
                        } else {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(logs) { log ->
                                    AdminLogRecordRow(log = log)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminEmptyPlaceholder(text: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.VerifiedUser, contentDescription = null, modifier = Modifier.size(48.dp), tint = Color(0xFF4CAF50))
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ==========================================
// FLAGGED CONTENT ACTION ITEM CARDS
// ==========================================
@Composable
fun FlaggedPostItemCard(post: FeedPost, viewModel: CommunityViewModel, strings: AppStrings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Meta info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "${strings.labelAuthor}: ${post.authorName} (${post.authorRole})",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${strings.labelGroup}: ${post.groupId}",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Red)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(strings.flaggedPostBadge, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            // Original post text block
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            if (post.imageUrl != null) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "[${strings.illustrationAttachedLabel}: ${post.imageUrl}]",
                    color = KarnatakaRed,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            // AI scan result note
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Row {
                    Icon(Icons.Default.QueryStats, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onErrorContainer)
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = strings.flagTriggerLabel,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            post.flagReason ?: "Flagged on automated heuristics scanning.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onErrorContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            // Override actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Clear / Approve
                Button(
                    onClick = { viewModel.approvePostFlag(post.id) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_approve_post_${post.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(strings.approveClearBtn, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                // Delete content permanently from app
                Button(
                    onClick = { viewModel.deletePostAdmin(post.id) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_delete_post_${post.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(strings.deletePostBtn, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                // Re-Audit using Gemini AI endpoint
                FilledTonalButton(
                    onClick = { viewModel.performAIScanOnSelectedPost(post.id) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_audit_post_${post.id}"),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(Icons.Default.AutoFixHigh, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(strings.geminiAuditBtn, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun FlaggedChatItemCard(chat: ChatMessage, viewModel: CommunityViewModel, strings: AppStrings) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.error),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = "${strings.labelAuthor}: ${chat.authorName}",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${strings.labelRoom}: ${chat.groupId}",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFE65100))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(strings.flaggedChatBadge, color = Color.White, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = chat.messageText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))
            // Flag Reason
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.7f))
                    .padding(8.dp)
                    .clip(RoundedCornerShape(4.dp))
            ) {
                Text(
                     text = "${strings.flagReasonPrefix}: ${chat.flagReason ?: strings.flagReasonFallback}",
                     style = MaterialTheme.typography.labelSmall,
                     color = MaterialTheme.colorScheme.onErrorContainer
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            // Override actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Clear flags
                Button(
                    onClick = { viewModel.approveChatFlag(chat.id) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_approve_chat_${chat.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(strings.clearFlagBtn, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                // Delete permanent
                Button(
                    onClick = { viewModel.deleteChatAdmin(chat.id) },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_delete_chat_${chat.id}"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(strings.deleteMessageBtn, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun AdminLogRecordRow(log: AdminModerationLog) {
    val format = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val printedTime = format.format(Date(log.timestamp))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon code based on severity
            Icon(
                imageVector = when (log.actionType) {
                    "AUTO_SCAN" -> Icons.Default.GppGood
                    "FLAG_POST", "FLAG_CHAT" -> Icons.Default.GppBad
                    "SOFT_DELETE_POST", "SOFT_DELETE_CHAT" -> Icons.Default.DeleteOutline
                    "RESTORE_POST", "RESTORE_CHAT" -> Icons.Default.Undo
                    else -> Icons.Default.EventNote
                },
                contentDescription = null,
                tint = when (log.actionType) {
                    "AUTO_SCAN" -> Color(0xFF4CAF50)
                    "FLAG_POST", "FLAG_CHAT" -> Color(0xFFF44336)
                    "SOFT_DELETE_POST", "SOFT_DELETE_CHAT" -> Color(0xFFE91E63)
                    else -> KarnatakaGold
                },
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 4.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "EVENT: ${log.actionType}",
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = printedTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                    )
                }
                Text(
                    text = log.details,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Operator: ${log.adminName}",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

// ==========================================
// POPUP DIALOG FOR CREATING POSTS
// ==========================================
@Composable
fun CreatePostDialog(
    groupName: String,
    strings: AppStrings,
    onDismiss: () -> Unit,
    onSubmit: (content: String, templateImgKey: String?) -> Unit
) {
    var contentText by remember { mutableStateOf("") }
    
    // Choose which cultural illustration card representation to attach
    val imgTemplates = listOf(
        Pair("hampi_chariot", strings.imgHampiTitle),
        Pair("kuvempu_banner", strings.imgKuvempuTitle),
        Pair("yakshagana_perf", strings.imgYakshaganaTitle),
        Pair("nature", strings.imgNatureTitle),
        Pair(null, strings.imgNoneTitle)
    )
    var selectedImgKey by remember { mutableStateOf<String?>("hampi_chariot") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "${strings.dialogTitle} \n(Share to Feed: $groupName)",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = KarnatakaRed
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = contentText,
                    onValueChange = { contentText = it },
                    placeholder = { Text(strings.dialogPlaceholder) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(84.dp)
                        .testTag("post_content_input"),
                    shape = RoundedCornerShape(8.dp)
                )

                Text(
                    strings.dialogAttachLabel,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )

                // Select image layout
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    imgTemplates.forEach { (key, title) ->
                        val isPicked = selectedImgKey == key
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (isPicked) KarnatakaRed.copy(alpha = 0.12f) else Color.Transparent,
                            border = if (isPicked) BorderStroke(1.dp, KarnatakaRed) else BorderStroke(0.5.dp, MaterialTheme.colorScheme.outlineVariant),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedImgKey = key }
                                .testTag("attach_img_${key ?: "none"}")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isPicked,
                                    onClick = { selectedImgKey = key },
                                    colors = RadioButtonDefaults.colors(selectedColor = KarnatakaRed)
                                )
                                Text(
                                    text = title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isPicked) FontWeight.Bold else FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onSubmit(contentText, selectedImgKey) },
                colors = ButtonDefaults.buttonColors(containerColor = KarnatakaRed),
                modifier = Modifier.testTag("submit_post_btn")
            ) {
                Text(strings.dialogPublishBtn, color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, modifier = Modifier.testTag("cancel_post_btn")) {
                Text(strings.dialogDismissBtn)
            }
        }
    )
}
