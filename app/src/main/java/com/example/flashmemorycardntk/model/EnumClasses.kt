package com.example.flashmemorycardntk.model

enum class EnumGroupRoutes(val route: String){
    GROUP_SCREEN_2("group2"),
    GROUP_SCREEN_2_MA("group2MultipleAdd"),
    GROUP_SCREEN_3("group3"),
}
//--------------------------------------------------------------------------
enum class EnumQuestionAnswer(val str: String) {
    QUESTION("Question"),
    ANSWER("Answer")
}
//-----------------------------------------------------------------------------
enum class EnumSortSequence(val strEnum: String) {
    ORIGINAL(strEnum = "original sequence"),
    REVERSED(strEnum = "reversed sequence"),
    EASY(strEnum = "easy first"),
    HARD(strEnum = "hard first")
}
//-----------------------------------------------------------------------------
enum class EnumSnackbarActionScreens {
    GROUP1,
    GROUP2
}
//-----------------------------------------------------------------------------
