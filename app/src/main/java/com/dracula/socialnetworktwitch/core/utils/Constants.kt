package com.dracula.socialnetworktwitch.core.utils

object Constants {
    const val SEARCH_DELAY: Long = 500L
    const val SPLASH_SCREEN_DURATION = 3000L
    const val MAX_POST_DESCRIPTION_LINES = 1
    const val MIN_USERNAME_LENGTH = 5
    const val MIN_PASSWORD_LENGTH = 5
    const val DEFAULT_SHARED_PREFERENCE_NAME = "app_sharedpref"
    const val DEFAULT_PAGE_SIZE = 20
    const val DEFAULT_PAGE = 0
    const val MAX_COMMENT_LENGTH = 2000

    object SharedPrefKeys {
        const val KEY_TOKEN = "token"
        const val KEY_USER_ID = "user_id"
    }

    object NavArguments {
        const val NAV_USER_ID = "user_id"
        const val NAV_POST_ID = "post_id"
        const val NAV_PARENT_ID = "parent_id"


    }

    object AnnotatedStringTags {
        const val ANNOTATION_TAG_USERNAME = "username"
        const val ANNOTATION_TAG_PARENT_ID = "parentId"
    }
}