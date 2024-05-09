package com.example.flashmemorycardntk.data

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.flashmemorycardntk.model.EnumSortSequence
import com.example.flashmemorycardntk.model.SCDifficulty


class DatabaseInitializer {
    companion object {
        fun createCallback(): RoomDatabase.Callback {
            return object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    db.beginTransaction()
                    try {
                        val groupIdGitCards = insertGroup(db, "Git Questions")
                        val listQuestionAnswer = questionsAndAnswers()

                        insertCards(db, listQuestionAnswer, groupIdGitCards)

                        db.setTransactionSuccessful()
                    } finally {
                        db.endTransaction()
                    }
                }
            }
        }

        private fun insertGroup(db: SupportSQLiteDatabase, name: String): Long {
            val stmt =
                db.compileStatement("INSERT INTO group_entity (name, isMarkedForDeletion, settingInfinite, settingSortSequence, settingShuffle, settingBalance) VALUES (?, 0, 0, '${EnumSortSequence.ORIGINAL.strEnum}', 0, 3)")
            stmt.bindString(1, name)
            return stmt.executeInsert()
        }

        private fun insertCards(
            db: SupportSQLiteDatabase,
            questionsAndAnswers: List<Pair<String, String>>,
            groupId: Long
        ) {
            val stmt =
                db.compileStatement("INSERT INTO card_entity (question, answer, groupId, isMarkedForDeletion, isMarkedForPlay, successRate, errorRate, difficultyLevel) VALUES (?, ?, ?, 0, 0, 0, 0, '${SCDifficulty.Level1.levelMode}')")
            questionsAndAnswers.forEach { (question, answer) ->
                stmt.bindString(1, question)
                stmt.bindString(2, answer)
                stmt.bindLong(3, groupId)
                stmt.executeInsert()
                stmt.clearBindings()
            }
        }
    }
}

fun questionsAndAnswers(): List<Pair<String, String>> {
    return listOf(
        Pair(
            "What is Git?",
            "Git is a distributed version control system used to track changes in files and coordinate work among multiple developers."
        ),
        Pair(
            "How do you initialize a new Git repository?",
            "Use the command `git init`. This creates a new `.git` subdirectory in your current directory, initializing the Git repository."
        ),
        Pair(
            "What is a 'commit'?",
            "A commit is a record of changes that have been saved to the Git repository. Each commit contains a commit message explaining the changes."
        ),
        Pair(
            "How can you view the commit history?",
            "Use the command `git log` to view a list of recent commits made in the repository."
        ),
        Pair(
            "What is a 'branch' in Git?",
            "A branch is a separate version of the repository that diverges from the main line and can be developed independently."
        ),
        Pair(
            "How do you create a new branch?",
            "Use the command `git branch <branch-name>` to create a new branch. Replace `<branch-name>` with the name you want to give your branch."
        ),
        Pair(
            "How do you switch between branches?",
            "Use the command `git checkout <branch-name>` to switch to an existing branch. Replace `<branch-name>` with the name of the branch you want to switch to."
        ),
        Pair(
            "What is a 'merge' in Git?",
            "Merging is the process of combining the changes from one branch into another, typically from a development branch into the main branch."
        ),
        Pair(
            "How do you merge branches?",
            "Use the command `git merge <branch-name>` while on the branch that you want to merge into. This will merge the changes from `<branch-name>` into your current branch."
        ),
        Pair(
            "What is a 'merge conflict'?",
            "A merge conflict occurs when Git is unable to automatically resolve differences in code between two commits. It must be manually resolved by the user."
        ),
        Pair(
            "How do you resolve a merge conflict?",
            "You need to edit the files where Git has indicated a conflict, make the necessary changes to resolve the conflict, and then mark the conflict as resolved with `git add <file-name>`."
        ),
        Pair(
            "What is a 'remote' in Git?",
            "A remote in Git is a common repository that all team members use to exchange their changes. The most frequently used remote is typically named 'origin'."
        ),
        Pair(
            "How do you add a remote repository?",
            "Use the command `git remote add <name> <url>`, replacing `<name>` with your preferred name for the remote, and `<url>` with the URL of the remote repository."
        ),
        Pair(
            "How do you fetch changes from a remote repository?",
            "Use the command `git fetch <remote>`. This downloads changes from the remote repository but does not integrate them into your current branch."
        ),
        Pair(
            "What is a 'pull' in Git?",
            "A pull is when you fetch changes from a remote repository and immediately merge them into your current branch. It's effectively a combination of `git fetch` and `git merge`."
        ),
        Pair(
            "How do you push changes to a remote repository?",
            "Use the command `git push <remote> <branch>`, replacing `<remote>` with the name of the remote and `<branch>` with the branch name."
        ),
        Pair(
            "What is 'stashing'?",
            "Stashing takes your uncommitted changes and saves them away for later, restoring your working directory to match the HEAD commit."
        ),
        Pair(
            "How do you stash changes?",
            "Use the command `git stash` to stash your changes. You can reapply these changes later with `git stash pop`."
        ),
        Pair(
            "What is a 'tag' in Git?",
            "A tag is a marker used to point to a specific commit in the history, typically used to mark release points like v1.0, v2.0, etc."
        ),
        Pair(
            "How do you create a tag?",
            "Use the command `git tag <tag-name> <commit-id>`, replacing `<tag-name>` with the name of the tag and `<commit-id>` with the commit you want to tag."
        ),
        Pair(
            "What is the difference between a 'lightweight' and an 'annotated' tag?",
            "A lightweight tag is simply a pointer to a specific commit, whereas an annotated tag is stored as a full object in the Git database, containing the tagger name, email, date, and a tagging message."
        ),
        Pair(
            "How do you clone a repository?",
            "Use the command `git clone <repository-url>` to make a copy of the remote repository on your local machine."
        ),
        Pair(
            "What is 'rebase' in Git?",
            "Rebase is a way to move or combine a sequence of commits to a new base commit. It's a cleaner alternative to merging that can create a linear project history."
        )
    )
}