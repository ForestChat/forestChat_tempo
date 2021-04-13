/*
 * Copyright (C) 2017 Moez Bhatti <moez.bhatti@gmail.com>
 *
 * This file is part of QKSMS.
 *
 * QKSMS is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * QKSMS is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with QKSMS.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.forest.forestchat.feature.themepicker

import android.animation.ObjectAnimator
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.forest.forestchat.R
import com.forest.forestchat.common.base.QkController
import com.forest.forestchat.common.util.Colors
import com.forest.forestchat.common.util.extensions.dpToPx
import com.forest.forestchat.common.util.extensions.setBackgroundTint
import com.forest.forestchat.common.util.extensions.setVisible
import com.forest.forestchat.feature.themepicker.injection.ThemePickerModule
import com.forest.forestchat.injection.appComponent
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import kotlinx.android.synthetic.main.theme_picker_controller.*
import kotlinx.android.synthetic.main.theme_picker_hsv.*
import javax.inject.Inject

class ThemePickerController(
    val recipientId: Long = 0L
) : QkController<ThemePickerView, ThemePickerState, ThemePickerPresenter>(), ThemePickerView {

    @Inject override lateinit var presenter: ThemePickerPresenter

    @Inject lateinit var colors: Colors
    @Inject lateinit var themeAdapter: ThemeAdapter
    @Inject lateinit var themePagerAdapter: ThemePagerAdapter

    init {
        appComponent
                .themePickerBuilder()
                .themePickerModule(ThemePickerModule(this))
                .build()
                .inject(this)

        layoutRes = R.layout.theme_picker_controller
    }

    override fun onViewCreated() {
        pager.offscreenPageLimit = 1
        pager.adapter = themePagerAdapter
        tabs.pager = pager

        themeAdapter.data = colors.materialColors

        materialColors.layoutManager = LinearLayoutManager(activity)
        materialColors.adapter = themeAdapter
    }

    override fun onAttach(view: View) {
        super.onAttach(view)
        presenter.bindIntents(this)
        setTitle(R.string.title_theme)
        showBackButton(true)

        themedActivity?.supportActionBar?.let { toolbar ->
            ObjectAnimator.ofFloat(toolbar, "elevation", toolbar.elevation, 0f).start()
        }
    }

    override fun onDetach(view: View) {
        super.onDetach(view)

        themedActivity?.supportActionBar?.let { toolbar ->
            ObjectAnimator.ofFloat(toolbar, "elevation", toolbar.elevation, 8.dpToPx(toolbar.themedContext).toFloat()).start()
        }
    }

    override fun themeSelected(): Observable<Int> = themeAdapter.colorSelected

    override fun hsvThemeSelected(): Observable<Int> = picker.selectedColor

    override fun clearHsvThemeClicks(): Observable<*> = clear.clicks()

    override fun applyHsvThemeClicks(): Observable<*> = apply.clicks()

    override fun render(state: ThemePickerState) {
        tabs.setRecipientId(state.recipientId)

        hex.setText(Integer.toHexString(state.newColor).takeLast(6))

        applyGroup.setVisible(state.applyThemeVisible)
        apply.setBackgroundTint(state.newColor)
        apply.setTextColor(state.newTextColor)
    }

    override fun setCurrentTheme(color: Int) {
        picker.setColor(color)
        themeAdapter.selectedColor = color
    }

}