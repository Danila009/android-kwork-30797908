package com.example.messenger.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

tailrec fun Context?.activity(): Activity? = this as? Activity
    ?: (this as? ContextWrapper)?.baseContext?.activity()