package com.alice.materialcalender.datetime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alice.materialcalender.R
import com.alice.materialcalender.databinding.FragDateTimeBinding
import com.alice.materialcalender.src.BaseFrag
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DateTimeFrag : BaseFrag() {

    private var _binding: FragDateTimeBinding? = null
    private val binding get() = _binding!!

    private var dataList = ArrayList<DateTimeResponse>()
    private var dateList = ArrayList<String>()

    val select = 0
    private val available = 1
    private val disabled = 2

    lateinit var startDate: Array<String>
    lateinit var endDate: Array<String>

    private val year = CalendarDay.today().year
    private val month = CalendarDay.today().month
    private val day = CalendarDay.today().day

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragDateTimeBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        makeDummy()
        startDate = dataList[0].date.split("-").toTypedArray()
        endDate = dataList[dataList.size-1].date.split("-").toTypedArray()
        binding.calendarView.state().edit()
            .setMinimumDate(CalendarDay.from(startDate[0].toInt(), startDate[1].toInt(), startDate[2].toInt()))
            .setMaximumDate(CalendarDay.from(endDate[0].toInt(), endDate[1].toInt(), endDate[2].toInt()))
            .commit()

        setDisabledDates()
        setAvailableDates()
        dateTimeCalendar()
    }

    private fun dateTimeCalendar() {
        binding.calendarView.setOnDateChangedListener { _, date, selected ->
            if (selected) {
                dateList.add(date.date.toString())
                val checked = listOf(CalendarDay.from(date.year, date.month, date.day))
                setDecor(checked, R.drawable.r_independent, select)
            } else {
                dateList.remove(date.date.toString())
                val unchecked = listOf(CalendarDay.from(date.year, date.month, date.day))
                setDecor(unchecked, R.drawable.l_independent, available)
            }
        }
        binding.calendarView.invalidateDecorators()
    }

    private fun setDisabledDates() {
        var localDate = LocalDate.of(startDate[0].toInt(), startDate[1].toInt(), startDate[2].toInt())
        val endLocalDate = LocalDate.of(endDate[0].toInt(), endDate[1].toInt(), endDate[2].toInt())
        var strings = ArrayList<String>()
        val disabledList = ArrayList<CalendarDay>()
        while (true) {
            localDate = localDate.plusDays(1)
            if (localDate == endLocalDate) break
            strings.add(localDate.toString())
        }
        for (i in 0 until dataList.size) {
            if (dataList[i].date in strings) {
                strings.remove(dataList[i].date)
            }
        }
        for (i in 0 until strings.size) {
            val date = strings[i].split("-").toTypedArray()
            disabledList.add(CalendarDay.from(date[0].toInt(), date[1].toInt(), date[2].toInt()))
        }
        setDecor(disabledList, R.drawable.t_independent, disabled)
    }

    private fun setAvailableDates() {
        val availableList = ArrayList<CalendarDay>()
        for (i in 0 until dataList.size) {
            val date = dataList[i].date.split("-").toTypedArray()
            availableList.add(CalendarDay.from(date[0].toInt(), date[1].toInt(), date[2].toInt()))
        }
        setDecor(availableList, R.drawable.l_independent, available)
    }

    private fun setDecor(calendarDayList: List<CalendarDay>, drawable: Int, color: Int) {
        binding.calendarView.addDecorators(
            EventDecorator(
                requireContext(), drawable, calendarDayList, color
            )
        )
    }

    private fun makeDummy() {
        var detailList = arrayListOf(
            Detail("20:00", null),
            Detail("21:00", null),
            Detail("22:00", null),
            Detail("23:00", null))
        dataList.add(DateTimeResponse("2021-02-24", detailList))
        detailList = arrayListOf(
            Detail("14:00", null),
            Detail("15:00", "me"),
            Detail("16:00", "me"),
            Detail("17:00", "me"),
            Detail("18:00", "me"),
            Detail("19:00", null),
            Detail("20:00", null),
            Detail("21:00", null),
            Detail("22:00", null),
            Detail("23:00", null))
        dataList.add(DateTimeResponse("2021-02-25", detailList))
        detailList = arrayListOf(
            Detail("14:00", null),
            Detail("15:00", null),
            Detail("16:00", null),
            Detail("17:00", null),
            Detail("18:00", null),
            Detail("19:00", null),
            Detail("20:00", null),
            Detail("21:00", null),
            Detail("22:00", null),
            Detail("23:00", null))
        dataList.add(DateTimeResponse("2021-02-26", detailList))
        detailList = arrayListOf(
            Detail("21:00", null),
            Detail("22:00", null),
            Detail("23:00", null))
        dataList.add(DateTimeResponse("2021-02-28", detailList))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}