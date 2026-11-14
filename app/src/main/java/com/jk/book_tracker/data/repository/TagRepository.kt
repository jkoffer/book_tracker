package com.jk.book_tracker.data.repository

import com.jk.book_tracker.data.local.database.dao.TagDao
import com.jk.book_tracker.data.local.database.entity.TagEntity
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TagRepository @Inject constructor(
    private val dao: TagDao
) {
    val tags = dao.getTags()

    suspend fun getAllTags(): List<TagEntity> {
        return dao.getTags().first()
    }

    suspend fun addTag(tag: TagEntity) {
        dao.addTag(tag)
    }

    suspend fun addTags(tags: List<TagEntity>) {
        dao.addTags(tags)
    }

    suspend fun deleteTag(tagId: String) {
        dao.deleteTagByTagId(tagId)
    }
}