package ru.skillbranch.devintensive.utils

import ru.skillbranch.devintensive.R

enum class ChatType(val layoutId: Int) {
    SINGLE(R.layout.item_chat_single),
    GROUP(R.layout.item_chat_group),
    ARCHIVE(R.layout.item_chat_archive)
}