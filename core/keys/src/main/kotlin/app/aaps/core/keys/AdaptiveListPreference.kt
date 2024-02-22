package app.aaps.core.keys

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.StringRes
import androidx.preference.ListPreference
import androidx.preference.PreferenceManager
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class AdaptiveListPreference(
    ctx: Context,
    attrs: AttributeSet? = null,
    stringKey: StringKey?,
    @StringRes title: Int?,
    @StringRes summary: Int? = null,
    entries: Array<CharSequence>? = null,
    entryValues: Array<CharSequence>? = null
) : ListPreference(ctx, attrs) {

    @Inject lateinit var preferences: Preferences

    // Inflater constructor
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, stringKey = null, title = null)

    init {
        (context.applicationContext as HasAndroidInjector).androidInjector().inject(this)

        stringKey?.let { key = context.getString(it.key) }
        title?.let { this.title = context.getString(it) }
        summary?.let { this.summary = context.getString(it) }
        entries?.let { setEntries(it)}
        entryValues?.let { setEntryValues(it)}

        val preferenceKey = stringKey ?: preferences.get(key) as StringKey
        if (preferences.simpleMode && preferenceKey.defaultedBySM) isVisible = false
        if (preferences.apsMode && !preferenceKey.showInApsMode) {
            isVisible = false; isEnabled = false
        }
        if (preferences.nsclientMode && !preferenceKey.showInNsClientMode) {
            isVisible = false; isEnabled = false
        }
        if (preferences.pumpControlMode && !preferenceKey.showInPumpControlMode) {
            isVisible = false; isEnabled = false
        }
        if (preferenceKey.dependency != 0) {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            if (!sp.getBoolean(context.getString(preferenceKey.dependency), false))
                isVisible = false
        }
        if (preferenceKey.negativeDependency != 0) {
            val sp = PreferenceManager.getDefaultSharedPreferences(context)
            if (sp.getBoolean(context.getString(preferenceKey.dependency), false))
                isVisible = false
        }
        setDefaultValue(preferenceKey.defaultValue)
    }

    override fun onAttached() {
        super.onAttached()
        // PreferenceScreen is final so we cannot extend and modify behavior
        val preferenceKey = preferences.get(key) as StringKey
        if (preferenceKey.hideParentScreenIfHidden) {
            parent?.isVisible = isVisible
            parent?.isEnabled = isEnabled
        }
    }
}