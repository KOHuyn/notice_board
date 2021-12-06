package com.example.appcar.ui.notice.pager.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appcar.data.model.CarType
import com.example.appcar.data.model.NoticeType
import com.example.appcar.ui.notice.pager.NoticeBoardPagerFragment

/**
 * Created by KO Huyn on 03/12/2021.
 */
class NoticeBoardPagerAdapter(f: Fragment, noticeTypeArray: List<NoticeType>) :
    FragmentStateAdapter(f) {

    private val arrFragment = noticeTypeArray.map { type ->
        NoticeBoardPagerFragment.newInstance(type.type)
    }

    override fun getItemCount(): Int = arrFragment.size

    override fun createFragment(position: Int): Fragment {
        return arrFragment[position]
    }
}