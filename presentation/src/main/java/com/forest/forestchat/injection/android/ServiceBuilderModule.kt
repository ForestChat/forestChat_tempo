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
package com.forest.forestchat.injection.android

import com.forest.forestchat.feature.backup.RestoreBackupService
import com.forest.forestchat.injection.scope.ActivityScope
import com.forest.forestchat.service.HeadlessSmsSendService
import com.forest.forestchat.receiver.SendSmsReceiver
import com.forest.forestchat.service.AutoDeleteService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindAutoDeleteService(): AutoDeleteService

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindHeadlessSmsSendService(): HeadlessSmsSendService

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindRestoreBackupService(): RestoreBackupService

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun bindSendSmsReceiver(): SendSmsReceiver

}
