package com.example.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AppRepository(private val communityDao: CommunityDao) {

    val allGroups: Flow<List<CommunityGroup>> = communityDao.getAllGroups()
    val adminPosts: Flow<List<FeedPost>> = communityDao.getAllPostsAdmin()
    val adminChats: Flow<List<ChatMessage>> = communityDao.getAllChatsAdmin()
    val flaggedPosts: Flow<List<FeedPost>> = communityDao.getFlaggedPosts()
    val flaggedChats: Flow<List<ChatMessage>> = communityDao.getFlaggedChats()
    val adminLogs: Flow<List<AdminModerationLog>> = communityDao.getAllAdminLogs()

    fun getActivePostsForGroup(groupId: Int): Flow<List<FeedPost>> =
        communityDao.getActivePostsForGroup(groupId)

    fun getActiveChatsForGroup(groupId: Int): Flow<List<ChatMessage>> =
        communityDao.getActiveChatsForGroup(groupId)

    suspend fun getPostById(id: Int): FeedPost? = communityDao.getPostById(id)
    suspend fun getChatById(id: Int): ChatMessage? = communityDao.getChatById(id)

    suspend fun insertPost(post: FeedPost) = withContext(Dispatchers.IO) {
        communityDao.insertPost(post)
    }

    suspend fun updatePost(post: FeedPost) = withContext(Dispatchers.IO) {
        communityDao.updatePost(post)
    }

    suspend fun insertChat(chat: ChatMessage) = withContext(Dispatchers.IO) {
        communityDao.insertChat(chat)
    }

    suspend fun updateChat(chat: ChatMessage) = withContext(Dispatchers.IO) {
        communityDao.updateChat(chat)
    }

    suspend fun softDeletePost(postId: Int) = withContext(Dispatchers.IO) {
        communityDao.softDeletePost(postId)
    }

    suspend fun softDeleteChat(chatId: Int) = withContext(Dispatchers.IO) {
        communityDao.softDeleteChat(chatId)
    }

    suspend fun insertAdminLog(log: AdminModerationLog) = withContext(Dispatchers.IO) {
        communityDao.insertAdminLog(log)
    }

    suspend fun ensureSeedData() = withContext(Dispatchers.IO) {
        val count = communityDao.getGroupCount()
        if (count == 0) {
            // Seed groups
            val groups = listOf(
                CommunityGroup(
                    id = 1,
                    name = "Kannada Sahitya & Kavite",
                    kannadaName = "ಕನ್ನಡ ಸಾಹಿತ್ಯ ಮತ್ತು ಕವಿತೆ",
                    description = "Celebrate the rich writings of Jnanpith awardees (Kuvempu, Bendre, Karanth) and share community poems, books, and insights.",
                    descriptionKn = "ಕನ್ನಡ ಸಾಹಿತ್ಯದ ಜ್ಞಾನಪೀಠ ಪ್ರಶಸ್ತಿ ವಿಜೇತರ ಅಮರ ಕೃತಿಗಳ ಚರ್ಚೆ, ಸ್ವರಚಿತ ಕವನಗಳು, ಸಾಹಿತ್ಯ ವಿಚಾರಗಳನ್ನು ಹಂಚಿಕೊಳ್ಳಿ.",
                    iconName = "book"
                ),
                CommunityGroup(
                    id = 2,
                    name = "Charitre & Samrakshane",
                    kannadaName = "ಇತಿಹಾಸ ಮತ್ತು ಸ್ಮಾರಕ ಸಂರಕ್ಷಣೆ",
                    description = "Commemorate Karnataka's glorious chapters: Kadamba, Chalukya, Rashtrakuta, and the golden Vijayanagara empire. Focus on monument site preservation.",
                    descriptionKn = "ಕದಂಬ, ಚಾಲುಕ್ಯ, ರಾಷ್ಟ್ರಕೂಟ ಹಾಗೂ ವಿಜಯನಗರದ ಭವ್ಯ ಇತಿಹಾಸ ಚರ್ಚಿಸುವುದು ಮತ್ತು ನಮ್ಮ ಪ್ರವಾಸಿ ಉದ್ಯಾನ, ಸ್ಮಾರಕಗಳ ರಕ್ಷಣೆ.",
                    iconName = "castle"
                ),
                CommunityGroup(
                    id = 3,
                    name = "Janapada & Kalegalu",
                    kannadaName = "ಜಾನಪದ ಮತ್ತು ಪ್ರದರ್ಶನ ಕಲೆಗಳು",
                    description = "Promote and preserve deep-rooted folk traditions like Yakshagana, Dollu Kunitha, Veeraagase, Togalu Gombeyaata, and local drama.",
                    descriptionKn = "ಯಕ್ಷಗಾನ, ಡೊಳ್ಳು ಕುಣಿತ, ವೀರಗಾಸೆ ಮುಂತಾದ ಕರುನಾಡಿನ ಅನನ್ಯ ಸಾಂಪ್ರದಾಯಿಕ ಜನಪದ ಕಲೆಗಳ ಚರ್ಚೆ ಮತ್ತು ನವಪೀಳಿಗೆಗೆ ಪ್ರೇರಣೆ.",
                    iconName = "music"
                ),
                CommunityGroup(
                    id = 4,
                    name = "Parisara Samrakshane",
                    kannadaName = "ಪರಿಸರ ಮತ್ತು ಪಶ್ಚಿಮ ಘಟ್ಟಗಳ ರಕ್ಷಣೆ",
                    description = "Unite to advocate for Western Ghats conservation, keep rivers like Cauvery and Sharavathi clean, and organize green planting drives.",
                    descriptionKn = "ಕಾವೇರಿ, ಕೃಷ್ಣಾ, ಶರಾವತಿ ನದಿಗಳ ರಕ್ಷಣೆ, ಪಶ್ಚಿಮ ಘಟ್ಟಗಳ ಜೀವ ವೈವಿಧ್ಯ ಕಾಪಾಡಲು ಮತ್ತು ಕಾಡು ಬೆಳೆಸಲು ಚರ್ಚಿಸಿ.",
                    iconName = "nature"
                )
            )
            for (g in groups) {
                communityDao.insertGroup(g)
            }

            // Seed posts
            val initialPosts = listOf(
                FeedPost(
                    id = 101,
                    groupId = 2,
                    authorName = "Sandesh Gowda",
                    authorRole = "President",
                    content = "Visited the historical Stone Chariot (ಕಲ್ಲು ರಥ) at Hampi today. This structural marvel of the Vijayanagara empire is a glowing testament to Kannada royal architecture. Let's pledge to protect and respect our sacred world heritage sites from littering! #NammaHampi #SaveHeritage",
                    imageUrl = "hampi_chariot", // Represents template image key
                    likes = 45,
                    timestamp = System.currentTimeMillis() - 7200000 // 2 hrs ago
                ),
                FeedPost(
                    id = 102,
                    groupId = 1,
                    authorName = "Deepa Patil",
                    authorRole = "Moderator",
                    content = "Remembering the immortal lines by Rashtrakavi Kuvempu: \n\"ಎಲ್ಲಾದರೂ ಇರು, ಎಂತಾದರೂ ಇರು, \nಎಂದೆಂದಿಗೂ ನೀ ಕನ್ನಡವಾಗಿರು.\"\n(Wherever you are, however you are, remain forever a Kannadiga!). Feeling extremely proud of our literary heritage. Let us instill Kannada reading habits in our local libraries.",
                    imageUrl = "kuvempu_banner",
                    likes = 32,
                    timestamp = System.currentTimeMillis() - 14400000 // 4 hrs ago
                ),
                FeedPost(
                    id = 103,
                    groupId = 3,
                    authorName = "Karthik Hegde",
                    authorRole = "Member",
                    content = "Mesmerized by a Yakshagana (ಯಕ್ಷಗಾನ) performance in Sirsi last night. The intricate makeup, energetic dance-drama gestures, and high-pitched songs in Tenkutitti style are incredibly moving. Proud of our pristine folk arts!",
                    imageUrl = "yakshagana_perf",
                    likes = 27,
                    timestamp = System.currentTimeMillis() - 28800000 // 8 hrs ago
                )
            )
            for (p in initialPosts) {
                communityDao.insertPost(p)
            }

            // Seed chats
            val initialChats = listOf(
                ChatMessage(
                    id = 201,
                    groupId = 1,
                    authorName = "Amit Kumar",
                    messageText = "Namaskara! Can anyone suggest good historical Kannada novels? I just finished 'Parva' by S.L. Bhyrappa and was completely awed.",
                    timestamp = System.currentTimeMillis() - 3600000
                ),
                ChatMessage(
                    id = 202,
                    groupId = 1,
                    authorName = "Vijay Nayak",
                    messageText = "You should definitely read 'Mayura' and 'Chidambara Rahasya' next! They are beautiful.",
                    timestamp = System.currentTimeMillis() - 3000000
                ),
                ChatMessage(
                    id = 203,
                    groupId = 2,
                    authorName = "Priyanka K",
                    messageText = "We are planning a voluntary cleanup campaign at Badami Caves next Sunday under Suverna Karnataka Rakshana Vedike. Please join if you're in Bagalkot!",
                    timestamp = System.currentTimeMillis() - 1200000
                )
            )
            for (c in initialChats) {
                communityDao.insertChat(c)
            }

            // Seed initial log
            communityDao.insertAdminLog(
                AdminModerationLog(
                    actionType = "AUTO_SCAN",
                    targetId = 0,
                    targetType = "SYSTEM",
                    details = "Initialized database seed items safely. Configured default Kannada cultural protection interests.",
                    adminName = "System Overseer",
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
