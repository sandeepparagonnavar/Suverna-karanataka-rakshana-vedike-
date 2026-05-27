package com.example.ui

interface AppStrings {
    val appTitle: String
    val appSubtitle: String
    val memberMode: String
    val adminModeOn: String
    val slogan: String
    val bannerUser: String
    val selectInterestGroup: String
    val tabChat: String
    val tabFeed: String
    val tabModeration: String
    val geminiShieldAnalyzing: String
    val activeRoomChat: String
    val activeRoomRealtime: String
    val noMessagesTitle: String
    val noMessagesSubtitle: String
    val mockViolationsPrompt: String
    val simulateSpamBtn: String
    val chatInputPlaceholder: String
    val moderationReviewPending: String
    val autoFlaggedTitle: String
    val emptyPhotosTitle: String
    val emptyPhotosSubtitle: String
    val shareFirstPostBtn: String
    val photoFeedTitleSuffix: String
    val addNewPostBtn: String
    val likesCountSuffix: String
    val hampiTitle: String
    val kuvempuTitle: String
    val yakshaganaTitle: String
    val natureTitle: String
    val noPhotoOption: String
    
    // Additional localized fields for moderation and UI
    val labelAuthor: String
    val labelGroup: String
    val labelRoom: String
    val flaggedPostBadge: String
    val flaggedChatBadge: String
    val illustrationAttachedLabel: String
    val flagTriggerLabel: String
    val flagReasonPrefix: String
    val flagReasonFallback: String
    
    // Mapped aliases to ensure seamless layout references
    val presidentControlPanel: String
    val adminControlCabin: String
    val adminRestrictedNotice: String
    val postFlagsLabel: String
    val chatFlagsLabel: String
    val totalLogsLabel: String
    val tabFlaggedPosts: String
    val tabFlaggedChats: String
    val tabModLogs: String
    val noPostsFlaggedPlaceholder: String
    val noChatsFlaggedPlaceholder: String
    val noLogsPlaceholder: String
    
    // Create Post Dialog mappings
    val imgHampiTitle: String
    val imgKuvempuTitle: String
    val imgYakshaganaTitle: String
    val imgNatureTitle: String
    val imgNoneTitle: String
    val dialogTitle: String
    val dialogPlaceholder: String
    val dialogAttachLabel: String
    val dialogPublishBtn: String
    val dialogDismissBtn: String

    // Admin / Moderation Tab
    val adminCabinTitleKannada: String
    val adminCabinTitleEnglish: String
    val adminCabinRestrictedMsg: String
    val unlockAdminCabinBtn: String
    val postFlagsStat: String
    val chatFlagsStat: String
    val totalLogsStat: String
    val subTabFlaggedPosts: String
    val subTabFlaggedChats: String
    val subTabModerationLogs: String
    val emptyFlaggedPosts: String
    val emptyFlaggedChats: String
    val emptyAdminLogs: String
    val flaggedPostLabel: String
    val flaggedChatLabel: String
    val flagTriggerReason: String
    val approveClearBtn: String
    val deletePostBtn: String
    val geminiAuditBtn: String
    val clearFlagBtn: String
    val deleteMessageBtn: String
    val eventLabel: String
    val operatorLabel: String
    
    // Create Post Dialog
    val createPostDialogTitle: String
    val createPostDialogPlaceholder: String
    val selectCulturePhotoLabel: String
    val shareNowBtn: String
    val dismissBtn: String
}

object KannadaStrings : AppStrings {
    override val appTitle = "ಕನ್ನಡ ವೇದಿಕೆ"
    override val appSubtitle = "ಸುವರ್ಣ ಕರ್ನಾಟಕ ರಕ್ಷಣಾ ವೇದಿಕೆ"
    override val memberMode = "ಸದಸ್ಯ ಮೋಡ್"
    override val adminModeOn = "ಅಡ್ಮಿನ್: ಆನ್"
    override val slogan = "ಕನ್ನಡವೇ ಸತ್ಯ, ಕನ್ನಡವೇ ನಿತ್ಯ • ಎಲ್ಲರಿಗೂ ನಮಸ್ಕಾರ"
    override val bannerUser = "ಬಳಕೆದಾರ"
    override val selectInterestGroup = "ಆಸಕ್ತಿ ಆಧಾರಿತ ಗುಂಪುಗಳು:"
    override val tabChat = "ಚಾಟ್ ಲೌಂಜ್"
    override val tabFeed = "ಫೋಟೋ ಫೀಡ್"
    override val tabModeration = "ಮಧ್ಯಸ್ಥಿಕೆ (Admin)"
    override val geminiShieldAnalyzing = "ಸ್ವಯಂಚಾಲಿತ ಜೆಮಿನಿ ಶೀಲ್ಡ್ ಸಂದೇಶವನ್ನು ಪರಿಶೀಲಿಸುತ್ತಿದೆ..."
    override val activeRoomChat = "ಕ್ರಿಯಾಶೀಲ ಕೊಠಡಿ"
    override val activeRoomRealtime = "ನೈಜ-ಸಮಯದ ಚರ್ಚಾ ಫಲಕ"
    override val noMessagesTitle = "ಯಾವುದೇ ಸಂದೇಶಗಳಿಲ್ಲ. ಮೊದಲ ಚರ್ಚೆ ಆರಂಭಿಸಿ!"
    override val noMessagesSubtitle = "ನಮ್ಮನ್ನು ಕನ್ನಡದಲ್ಲಿ ಪರಿಚಯಿಸಿಕೊಳ್ಳಿ!"
    override val mockViolationsPrompt = "ಅಡ್ಮಿನ್ ಫಿಲ್ಟರ್‌ಗಳನ್ನು ಪರೀಕ್ಷಿಸಲು ನಿಯಮ ಉಲ್ಲಂಘನೆಯ ಅಣಕು ಅಲರ್ಟ್‌ಗಳು ಬೇಕೇ?"
    override val simulateSpamBtn = "ಸ್ಪ್ಯಾಮ್ ಸಿಮ್ಯುಲೇಟ್ ಮಾಡಿ"
    override val chatInputPlaceholder = "ಸಂದೇಶ ಕಳುಹಿಸಿ ಸುರಕ್ಷಿತವಾಗಿ..."
    override val moderationReviewPending = "ಮಾಡರೇಶನ್ ವಿಮರ್ಶೆ ಬಾಕಿ ಇದೆ"
    override val autoFlaggedTitle = "ಸುರಕ್ಷತೆಗಾಗಿ ಸ್ವಯಂಚಾಲಿತವಾಗಿ ಫ್ಲ್ಯಾಗ್ ಮಾಡಲಾಗಿದೆ"
    override val emptyPhotosTitle = "ಯಾವುದೇ ಫೋಟೋಗಳಿಲ್ಲ. ಮೊದಲ ಪೋಸ್ಟ್ ಪ್ರಕಟಿಸಿ!"
    override val emptyPhotosSubtitle = "ಕರ್ನಾಟಕದ ಸೌಂದರ್ಯವನ್ನು ಆಚರಿಸಲು ಪ್ಲಸ್ (+) ಬಟನ್ ಒತ್ತಿ!"
    override val shareFirstPostBtn = "ಮೊದಲ ಪೋಸ್ಟ್ ಹಂಚಿಕೊಳ್ಳಿ"
    override val photoFeedTitleSuffix = "ಜ್ಞಾನ ಮತ್ತು ಚಿತ್ರಗಳು"
    override val addNewPostBtn = "ಹೊಸ ಪೋಸ್ಟ್"
    override val likesCountSuffix = "ಜನರು ಇಷ್ಟಪಟ್ಟಿದ್ದಾರೆ"
    override val hampiTitle = "ಹಂಪಿ ಕಲ್ಲು ರಥ (ಐತಿಹಾಸಿಕ)"
    override val kuvempuTitle = "ಕುವೆಂಪು ಸಾಹಿತ್ಯ ಬ್ಯಾನರ್"
    override val yakshaganaTitle = "ಯಕ್ಷಗಾನ ಸಾಂಸ್ಕೃತಿಕ ಪ್ರದರ್ಶನ"
    override val natureTitle = "ಪಶ್ಚಿಮ ಘಟ್ಟಗಳ ಪ್ರಕೃತಿ ಕಾವಲು"
    override val noPhotoOption = "ಚಿತ್ರ ಲಗತ್ತು ಇಲ್ಲದೆ (ಕೇವಲ ಪಠ್ಯ)"
    
    // Additional localized fields
    override val labelAuthor = "ಲೇಖಕರು"
    override val labelGroup = "ಗುಂಪು ಐಡಿ"
    override val labelRoom = "ಕೊಠಡಿ ಐಡಿ"
    override val flaggedPostBadge = "ಫ್ಲ್ಯಾಗ್ ಮಾಡಲಾದ ಪೋಸ್ಟ್"
    override val flaggedChatBadge = "ಫ್ಲ್ಯಾಗ್ ಮಾಡಲಾದ ಚಾಟ್"
    override val illustrationAttachedLabel = "ಚಿತ್ರ ಲಗತ್ತು"
    override val flagTriggerLabel = "ಫ್ಲ್ಯಾಗ್ ಉಲ್ಲಂಘನೆ ಕಾರಣ"
    override val flagReasonPrefix = "ಶೀಲ್ಡ್ ಟ್ಯಾಗ್"
    override val flagReasonFallback = "ಮಾಡರೇಶನ್ ವಿಮರ್ಶೆ ಬಾಕಿ ಇದೆ."
    
    override val presidentControlPanel = "ಅಧ್ಯಕ್ಷರ ನಿಯಂತ್ರಣ ಮಂಡಳಿ"
    override val adminControlCabin = "ಅಡ್ಮಿನ್ ಮಾಡರೇಶನ್ ನಿಯಂತ್ರಣ ಕೊಠಡಿ"
    override val adminRestrictedNotice = "ಈ ಪ್ರೀಮಿಯಂ ನಿರ್ವಹಣಾ ಕೊಠಡಿಯು ಅಡ್ಮಿನ್‌ಗಳಿಗೆ ಮಾತ್ರ ಸೀಮಿತವಾಗಿದೆ. ದಯವಿಟ್ಟು ಮೇಲ್ಭಾಗದಲ್ಲಿರುವ 'ಅಡ್ಮಿನ್ ಮೋಡ್' ಆನ್ ಮಾಡಿ."
    override val postFlagsLabel = "ಪೋಸ್ಟ್ ಫ್ಲ್ಯಾಗ್ಗಳು"
    override val chatFlagsLabel = "ಚಾಟ್ ಫ್ಲ್ಯಾಗ್ಗಳು"
    override val totalLogsLabel = "ಒಟ್ಟು ಲಾಗ್‌ಗಳು"
    override val tabFlaggedPosts = "ಫ್ಲ್ಯಾಗ್ ಮಾಡಲಾದ ಪೋಸ್ಟ್‌ಗಳು"
    override val tabFlaggedChats = "ಫ್ಲ್ಯಾಗ್ ಮಾಡಲಾದ ಚಾಟ್‌ಗಳು"
    override val tabModLogs = "ಸಿಸ್ಟಮ್ ಲಾಗ್"
    override val noPostsFlaggedPlaceholder = "ವಿಮರ್ಶೆಗಾಗಿ ಯಾವುದೇ ಪೋಸ್ಟ್‌ಗಳಿಲ್ಲ. ಫೀಡ್ ಸಂಪೂರ್ಣವಾಗಿ ಸುರಕ್ಷಿತವಾಗಿದೆ!"
    override val noChatsFlaggedPlaceholder = "ಎಲ್ಲಾ ಚಾಟ್ ರೂಮ್‌ಗಳು ಉಲ್ಲಂಘನೆಗಳಿಂದ ಮುಕ್ತವಾಗಿವೆ."
    override val noLogsPlaceholder = "ಇನ್ನೂ ಯಾವುದೇ ಆಡಳಿತಾತ್ಮಕ ಘಟನೆಗಳು ದಾಖಲಾಗಿಲ್ಲ."
    
    override val imgHampiTitle = "ಹಂಪಿ ಕಲ್ಲು ರಥ (ಐತಿಹಾಸಿಕ)"
    override val imgKuvempuTitle = "ಕುವೆಂಪು ಸಾಹಿತ್ಯ ಬ್ಯಾನರ್"
    override val imgYakshaganaTitle = "ಯಕ್ಷಗಾನ ಸಾಂಸ್ಕೃತಿಕ ಪ್ರದರ್ಶನ"
    override val imgNatureTitle = "ಪಶ್ಚಿಮ ಘಟ್ಟಗಳ ಪ್ರಕೃತಿ ಕಾವಲು"
    override val imgNoneTitle = "ಚಿತ್ರ ಲಗತ್ತು ಇಲ್ಲದೆ (ಕೇವಲ ಪಠ್ಯ)"
    override val dialogTitle = "ಕರುನಾಡ ಫೀಡ್‌ಗೆ ಹೊಸ ಪೋಸ್ಟ್"
    override val dialogPlaceholder = "ಕನ್ನಡ ಕಲೆ, ಇತಿಹಾಸ ಅಥವಾ ಸಾಂಸ್ಕೃತಿಕ ವಿಚಾರಗಳನ್ನು ಇಲ್ಲಿ ಹಂಚಿಕೊಳ್ಳಿ..."
    override val dialogAttachLabel = "ಕರ್ನಾಟಕದ ಸಾಂಸ್ಕೃತಿಕ ಫೋಟೋ ಪ್ರತಿನಿಧಿ ಲಗತ್ತಿಸಿ:"
    override val dialogPublishBtn = "ಪ್ರಕಟಿಸಿ"
    override val dialogDismissBtn = "ರದ್ದುಮಾಡಿ"

    // Admin / Moderation Tab
    override val adminCabinTitleKannada = "ಅಧ್ಯಕ್ಷರ ನಿಯಂತ್ರಣ ಮಂಡಳಿ"
    override val adminCabinTitleEnglish = "ಅಡ್ಮಿನ್ ಮಾಡರೇಶನ್ ನಿಯಂತ್ರಣ ಕೊಠಡಿ"
    override val adminCabinRestrictedMsg = "ಈ ಪ್ರೀಮಿಯಂ ನಿರ್ವಹಣಾ ಕೊಠಡಿಯು ಅಡ್ಮಿನ್‌ಗಳಿಗೆ ಮಾತ್ರ ಸೀಮಿತವಾಗಿದೆ. ಲಾಗ್‌ಗಳನ್ನು ಪರಿಶೀಲಿಸಲು ಅಥವಾ ಫ್ಲ್ಯಾಗ್‌ಗಳನ್ನು ತೆರವುಗೊಳಿಸಲು, ದಯವಿಟ್ಟು ಮೇಲ್ಭಾಗದಲ್ಲಿರುವ 'ಅಡ್ಮಿನ್ ಮೋಡ್' ಆನ್ ಮಾಡಿ."
    override val unlockAdminCabinBtn = "ಕೊಠಡಿ ಅನ್‌ಲಾಕ್ ಮಾಡಿ"
    override val postFlagsStat = "ಪೋಸ್ಟ್ ಫ್ಲ್ಯಾಗ್ಗಳು"
    override val chatFlagsStat = "ಚಾಟ್ ಫ್ಲ್ಯಾಗ್ಗಳು"
    override val totalLogsStat = "ಒಟ್ಟು ಲಾಗ್‌ಗಳು"
    override val subTabFlaggedPosts = "ಫ್ಲ್ಯಾಗ್ ಮಾಡಲಾದ ಪೋಸ್ಟ್‌ಗಳು"
    override val subTabFlaggedChats = "ಫ್ಲ್ಯಾಗ್ ಮಾಡಲಾದ ಚಾಟ್‌ಗಳು"
    override val subTabModerationLogs = "ಸಿಸ್ಟಮ್ ಲಾಗ್"
    override val emptyFlaggedPosts = "ವಿಮರ್ಶೆಗಾಗಿ ಯಾವುದೇ ಪೋಸ್ಟ್‌ಗಳಿಲ್ಲ. ಫೀಡ್ ಸಂಪೂರ್ಣವಾಗಿ ಸುರಕ್ಷಿತವಾಗಿದೆ!"
    override val emptyFlaggedChats = "ಎಲ್ಲಾ ಚಾಟ್ ರೂಮ್‌ಗಳು ಉಲ್ಲಂಘನೆಗಳಿಂದ ಮುಕ್ತವಾಗಿವೆ."
    override val emptyAdminLogs = "ಇನ್ನೂ ಯಾವುದೇ ಆಡಳಿತಾತ್ಮಕ ಘಟನೆಗಳು ದಾಖಲಾಗಿಲ್ಲ."
    override val flaggedPostLabel = "ಫ್ಲ್ಯಾಗ್ ಮಾಡಲಾದ ಪೋಸ್ಟ್"
    override val flaggedChatLabel = "ಫ್ಲ್ಯಾಗ್ ಮಾಡಲಾದ ಚಾಟ್"
    override val flagTriggerReason = "ಫ್ಲ್ಯಾಗ್ ಪ್ರಚೋದಕ / ಕಾರಣ:"
    override val approveClearBtn = "ಅನುಮೋದಿಸು / ತೆರವುಗೊಳಿಸು"
    override val deletePostBtn = "ಪೋಸ್ಟ್ ಅಳಿಸಿ"
    override val geminiAuditBtn = "ಜೆಮಿನಿ ಆಡಿಟ್"
    override val clearFlagBtn = "ಫ್ಲ್ಯಾಗ್ ತೆರವುಗೊಳಿಸು"
    override val deleteMessageBtn = "ಸಂದೇಶ ಅಳಿಸಿ"
    override val eventLabel = "ಘಟನೆ"
    override val operatorLabel = "ನಿರ್ವಾಹಕರು"
    
    // Create Post Dialog
    override val createPostDialogTitle = "ಕರುನಾಡ ಫೀಡ್‌ಗೆ ಹೊಸ ಪೋಸ್ಟ್"
    override val createPostDialogPlaceholder = "ಕನ್ನಡ ಕಲೆ, ಇತಿಹಾಸ ಅಥವಾ ಸಾಂಸ್ಕೃತಿಕ ವಿಚಾರಗಳನ್ನು ಇಲ್ಲಿ ಹಂಚಿಕೊಳ್ಳಿ..."
    override val selectCulturePhotoLabel = "ಕರ್ನಾಟಕದ ಸಾಂಸ್ಕೃತಿಕ ಫೋಟೋ ಪ್ರತಿನಿಧಿ ಲಗತ್ತಿಸಿ:"
    override val shareNowBtn = "ಪ್ರಕಟಿಸಿ"
    override val dismissBtn = "ರದ್ದುಮಾಡಿ"
}

object EnglishStrings : AppStrings {
    override val appTitle = "KANNADA VEDIKE"
    override val appSubtitle = "Suverna Karnataka Rakshana Vedike"
    override val memberMode = "Member Mode"
    override val adminModeOn = "Admin: ON"
    override val slogan = "Kannadave Satya, Kannadave Nitya • Greetings to Everyone"
    override val bannerUser = "User"
    override val selectInterestGroup = "Select Interest Group:"
    override val tabChat = "Chat Lounge"
    override val tabFeed = "Photos Dev"
    override val tabModeration = "Moderation"
    override val geminiShieldAnalyzing = "Automated Gemini Shield analyzing text for safety..."
    override val activeRoomChat = "Active Room"
    override val activeRoomRealtime = "Real-time discussion board"
    override val noMessagesTitle = "No messages here. Start the discussion!"
    override val noMessagesSubtitle = "Let's introduce ourselves in Kannada!"
    override val mockViolationsPrompt = "Need mock violation alerts to test Admin filters?"
    override val simulateSpamBtn = "Simulate Spam"
    override val chatInputPlaceholder = "Send chat safely..."
    override val moderationReviewPending = "Moderation Review Pending"
    override val autoFlaggedTitle = "Auto Flagged for Safety"
    override val emptyPhotosTitle = "No photos here. Publish the first post!"
    override val emptyPhotosSubtitle = "Tap + below to celebrate the beauty of Karnataka!"
    override val shareFirstPostBtn = "Share First Post"
    override val photoFeedTitleSuffix = "Knowledge & Gallery"
    override val addNewPostBtn = "Post Photo"
    override val likesCountSuffix = "people liked this"
    override val hampiTitle = "Hampi Stone Chariot (Historical)"
    override val kuvempuTitle = "Kuvempu Literary Banner"
    override val yakshaganaTitle = "Yakshagana Cultural Mask"
    override val natureTitle = "Western Ghats Nature Ridge"
    override val noPhotoOption = "No Photo Attachment (Only text)"
    
    // Additional localized fields
    override val labelAuthor = "Author"
    override val labelGroup = "Group ID"
    override val labelRoom = "Room ID"
    override val flaggedPostBadge = "Flagged Post"
    override val flaggedChatBadge = "Flagged Chat"
    override val illustrationAttachedLabel = "Attached Illustration"
    override val flagTriggerLabel = "Flag Trigger"
    override val flagReasonPrefix = "Shield Tag"
    override val flagReasonFallback = "Filtered under safety standards check."
    
    override val presidentControlPanel = "President's Control Board"
    override val adminControlCabin = "Admin Moderation Control Cabin"
    override val adminRestrictedNotice = "This premium management cockpit is highly restricted. To check dynamic auto-scans, verify logs, or clear flags, kindly toggle the 'Admin Mode' button on the yellow header at the top right."
    override val postFlagsLabel = "Post Flags"
    override val chatFlagsLabel = "Chat Flags"
    override val totalLogsLabel = "Total Logs"
    override val tabFlaggedPosts = "Flagged Posts"
    override val tabFlaggedChats = "Flagged Chats"
    override val tabModLogs = "System Logs"
    override val noPostsFlaggedPlaceholder = "No posts flagged for review. Workspace is safe!"
    override val noChatsFlaggedPlaceholder = "All communication chat lounges clear of any violations."
    override val noLogsPlaceholder = "No administrative incidents catalogued yet."
    
    override val imgHampiTitle = "Hampi Stone Chariot (Historical)"
    override val imgKuvempuTitle = "Kuvempu Literary Banner"
    override val imgYakshaganaTitle = "Yakshagana Cultural Mask"
    override val imgNatureTitle = "Western Ghats Nature Ridge"
    override val imgNoneTitle = "No Photo Attachment (Only text)"
    override val dialogTitle = "Share to Feed"
    override val dialogPlaceholder = "Share Kannada art, history or cultural ideas safely..."
    override val dialogAttachLabel = "Attach Karnataka Cultural Photo Representation:"
    override val dialogPublishBtn = "Share Now"
    override val dialogDismissBtn = "Dismiss"

    // Admin / Moderation Tab
    override val adminCabinTitleKannada = "President’s Control Board"
    override val adminCabinTitleEnglish = "Admin Moderation Control Cabin"
    override val adminCabinRestrictedMsg = "This premium management cockpit is highly restricted. To check dynamic auto-scans, verify logs, or clear flags, kindly toggle the 'Admin Mode' button on the yellow header at the top right."
    override val unlockAdminCabinBtn = "Unlock Admin Cabin"
    override val postFlagsStat = "Post Flags"
    override val chatFlagsStat = "Chat Flags"
    override val totalLogsStat = "Total Logs"
    override val subTabFlaggedPosts = "Flagged Posts"
    override val subTabFlaggedChats = "Flagged Chats"
    override val subTabModerationLogs = "System Logs"
    override val emptyFlaggedPosts = "No posts flagged for review. Workspace is safe!"
    override val emptyFlaggedChats = "All communication chat lounges clear of any violations."
    override val emptyAdminLogs = "No administrative incidents catalogued yet."
    override val flaggedPostLabel = "Flagged Post"
    override val flaggedChatLabel = "Flagged Chat"
    override val flagTriggerReason = "Flag Trigger / Reason:"
    override val approveClearBtn = "Approve / Clear"
    override val deletePostBtn = "Delete Post"
    override val geminiAuditBtn = "Gemini Audit"
    override val clearFlagBtn = "Clear Flag"
    override val deleteMessageBtn = "Delete Message"
    override val eventLabel = "EVENT"
    override val operatorLabel = "Operator"
    
    // Create Post Dialog
    override val createPostDialogTitle = "Share to Feed"
    override val createPostDialogPlaceholder = "Share Kannada art, history or cultural ideas safely..."
    override val selectCulturePhotoLabel = "Attach Karnataka Cultural Photo Representation:"
    override val shareNowBtn = "Share Now"
    override val dismissBtn = "Dismiss"
}

fun getAppStrings(lang: String): AppStrings {
    return if (lang == "en") EnglishStrings else KannadaStrings
}
