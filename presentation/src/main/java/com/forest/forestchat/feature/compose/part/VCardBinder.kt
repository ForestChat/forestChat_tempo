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
package com.forest.forestchat.feature.compose.part

import android.content.Context
import android.view.Gravity
import android.widget.FrameLayout
import com.forest.forestchat.R
import com.forest.forestchat.common.base.QkViewHolder
import com.forest.forestchat.common.util.Colors
import com.forest.forestchat.common.util.extensions.resolveThemeColor
import com.forest.forestchat.common.util.extensions.setBackgroundTint
import com.forest.forestchat.common.util.extensions.setTint
import com.forest.forestchat.extensions.isVCard
import com.forest.forestchat.extensions.mapNotNull
import com.forest.forestchat.feature.compose.BubbleUtils
import com.forest.forestchat.model.Message
import com.forest.forestchat.model.MmsPart
import ezvcard.Ezvcard
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.mms_vcard_list_item.*
import javax.inject.Inject

class VCardBinder @Inject constructor(colors: Colors, private val context: Context) : PartBinder() {

    override val partLayout = R.layout.mms_vcard_list_item
    override var theme = colors.theme()

    override fun canBindPart(part: MmsPart) = part.isVCard()

    override fun bindPart(
        holder: QkViewHolder,
        part: MmsPart,
        message: Message,
        canGroupWithPrevious: Boolean,
        canGroupWithNext: Boolean
    ) {
        BubbleUtils.getBubble(false, canGroupWithPrevious, canGroupWithNext, message.isMe())
                .let(holder.vCardBackground::setBackgroundResource)

        holder.containerView.setOnClickListener { clicks.onNext(part.id) }

        Observable.just(part.getUri())
                .map(context.contentResolver::openInputStream)
                .mapNotNull { inputStream -> inputStream.use { Ezvcard.parse(it).first() } }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { vcard -> holder.name?.text = vcard.formattedName.value }

        val params = holder.vCardBackground.layoutParams as FrameLayout.LayoutParams
        if (!message.isMe()) {
            holder.vCardBackground.layoutParams = params.apply { gravity = Gravity.START }
            holder.vCardBackground.setBackgroundTint(theme.theme)
            holder.vCardAvatar.setTint(theme.textPrimary)
            holder.name.setTextColor(theme.textPrimary)
            holder.label.setTextColor(theme.textTertiary)
        } else {
            holder.vCardBackground.layoutParams = params.apply { gravity = Gravity.END }
            holder.vCardBackground.setBackgroundTint(holder.containerView.context.resolveThemeColor(R.attr.bubbleColor))
            holder.vCardAvatar.setTint(holder.containerView.context.resolveThemeColor(android.R.attr.textColorSecondary))
            holder.name.setTextColor(holder.containerView.context.resolveThemeColor(android.R.attr.textColorPrimary))
            holder.label.setTextColor(holder.containerView.context.resolveThemeColor(android.R.attr.textColorTertiary))
        }
    }

}