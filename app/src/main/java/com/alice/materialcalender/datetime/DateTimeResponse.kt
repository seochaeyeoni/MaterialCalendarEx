package com.alice.materialcalender.datetime

data class DateTimeResponse (val date: String, val detail: List<Detail>)
data class Detail (val time: String, val unavailable_member: String?, var isSelected: Boolean = false)
