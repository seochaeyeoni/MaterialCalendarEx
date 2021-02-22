package com.alice.materialcalender.range

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alice.materialcalender.R
import com.alice.materialcalender.databinding.FragRangeBinding
import com.alice.materialcalender.src.BaseFrag
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RangeFrag : BaseFrag() {

    private var _binding: FragRangeBinding? = null
    private val binding get() = _binding!!

    private var dateList = ArrayList<LocalDate>()

    val color = 0
    private val transparent = 1
    private val disabled = 2

    private val year = CalendarDay.today().year
    private val month = CalendarDay.today().month
    private val day = CalendarDay.today().day

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragRangeBinding.inflate(inflater, container, false)
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cal = Calendar.getInstance()

        cal.set(year, month + 1, 1)
        val maxDay: Int = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        binding.calendarView.state().edit()
            .setMinimumDate(CalendarDay.from(year, month, 1))
            .setMaximumDate(CalendarDay.from(year, month + 2, maxDay))
            .commit()

        setDisabledDates()
        rangeCalendar()
    }

    private fun rangeCalendar() {
        binding.calendarView.setOnRangeSelectedListener { widget, dates ->
            for (i in dates.indices) {
                dateList.add(dates[i].date)
            }
            if (dateList.size > 28) {
                showCustomToast("기간은 최대 4주까지 선택할 수 있습니다.")
                val removeList = ArrayList<LocalDate>()
                Log.d("why", dateList.toString())
                for (i in 28 until dateList.size) {
                    Log.d("why", dateList[28].toString())
                    removeList.add(dateList[28])
                    dateList.removeAt(28)
                }
                setEvent(removeList, transparent)
                removeList.clear()
            }
            showCustomToast("${dateList[0]}~${dateList[dateList.size-1]}")
            setEvent(dateList, color)
        }

        binding.calendarView.setOnDateChangedListener { _, date, selected ->
            if (dateList.size > 0) {
                setEvent(dateList, transparent)
                dateList.clear()
            }
            dateList.add(date.date)
            if (selected) {
                setEvent(dateList, color)
            } else {
                setEvent(dateList, transparent)
            }
            dateList.clear()

        }
        binding.calendarView.invalidateDecorators()
    }

    private fun setDisabledDates() {
        val disabledList = ArrayList<LocalDate>()
        for (i in 1 until day) {
            disabledList.add(LocalDate.of(year, month, i))
        }
        setEvent(disabledList, disabled)
    }

    private fun setEvent(dateList: List<LocalDate>, color: Int) {
        val datesLeft: MutableList<CalendarDay> = ArrayList()
        val datesCenter: MutableList<CalendarDay> = ArrayList()
        val datesRight: MutableList<CalendarDay> = ArrayList()
        val datesIndependent: MutableList<CalendarDay> = ArrayList()
        datesLeft.clear()
        datesCenter.clear()
        datesRight.clear()
        datesIndependent.clear()
        for (localDate in dateList) {
            var right = false
            var left = false
            for (day1 in dateList) {
                if (localDate.isEqual(day1.plusDays(1))) {
                    left = true
                }
                if (day1.isEqual(localDate.plusDays(1))) {
                    right = true
                }
            }
            if (left && right) {
                datesCenter.add(CalendarDay.from(localDate))
            } else if (left) {
                datesLeft.add(CalendarDay.from(localDate))
            } else if (right) {
                datesRight.add(CalendarDay.from(localDate))
            } else {
                datesIndependent.add(CalendarDay.from(localDate))
            }
        }
        when (color) {
            0 -> {
                setDecor(datesCenter, R.drawable.r_center, 0)
                setDecor(datesLeft, R.drawable.r_left, 0)
                setDecor(datesRight, R.drawable.r_right, 0)
                setDecor(datesIndependent, R.drawable.r_independent, 0)
            }
            1 -> {
                setDecor(datesCenter, R.drawable.t_center, 1)
                setDecor(datesLeft, R.drawable.t_left, 1)
                setDecor(datesRight, R.drawable.t_right, 1)
                setDecor(datesIndependent, R.drawable.t_independent, 1)
            }
            2 -> {
                setDecor(datesCenter, R.drawable.t_center, 2)
                setDecor(datesLeft, R.drawable.t_left, 2)
                setDecor(datesRight, R.drawable.t_right, 2)
                setDecor(datesIndependent, R.drawable.t_independent, 2)
            }
        }
    }

    private fun setDecor(calendarDayList: List<CalendarDay>, drawable: Int, color: Int) {
        binding.calendarView.addDecorators(
            EventDecorator(
                requireContext(), drawable, calendarDayList, color
            )
        )
    }

    //setEvent 에서 LocalDate 로 바로 받기 때문에 필요x
    fun getLocalDate(date: String?): LocalDate? {
        val DATE_FORMAT = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH)
        return try {
            val input = sdf.parse(date)
            val cal = Calendar.getInstance()
            cal.time = input
            LocalDate.of(
                cal[Calendar.YEAR],
                cal[Calendar.MONTH] + 1,
                cal[Calendar.DAY_OF_MONTH]
            )
        } catch (e: NullPointerException) {
            null
        } catch (e: ParseException) {
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}