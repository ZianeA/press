package press.preferences

import android.content.Context
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.contour.ContourLayout
import me.saket.inboxrecyclerview.InboxRecyclerView
import me.saket.inboxrecyclerview.expander.InboxItemExpander
import me.saket.press.R
import me.saket.press.shared.localization.strings
import me.saket.press.shared.preferences.PreferenceCategory.AboutApp
import me.saket.press.shared.preferences.PreferenceCategory.LookAndFeel
import me.saket.press.shared.preferences.PreferenceCategory.Sync
import me.saket.press.shared.preferences.PreferenceCategoryItemModel
import me.saket.press.shared.preferences.PreferenceCategoryScreenKey
import me.saket.press.shared.ui.ScreenKey
import press.navigation.navigator
import press.navigation.transitions.ExpandableScreenHost
import press.theme.themeAware
import press.widgets.DividerItemDecoration
import press.widgets.PressToolbar
import press.widgets.SlideDownItemAnimator

class PreferencesView(context: Context) : ContourLayout(context), ExpandableScreenHost {
  private val toolbar = PressToolbar(context).apply {
    title = context.strings().prefs.screen_title
  }

  private val categoryList = InboxRecyclerView(context).apply {
    layoutManager = LinearLayoutManager(context)
    itemAnimator = SlideDownItemAnimator()
    addItemDecoration(DividerItemDecoration())
  }

  private val categoryAdapter = PreferenceCategoryListAdapter(
    categories = preferenceCategories(),
    onClick = { item ->
      navigator().lfg(PreferenceCategoryScreenKey(item.category))
    }
  )

  init {
    id = R.id.preferences_view
    themeAware {
      setBackgroundColor(it.window.backgroundColor)
    }

    toolbar.layoutBy(
      x = matchParentX(),
      y = topTo { parent.top() }
    )
    categoryList.layoutBy(
      x = matchParentX(),
      y = topTo { toolbar.bottom() }.bottomTo { parent.bottom() }
    )

    categoryList.adapter = categoryAdapter
  }

  private fun preferenceCategories(): List<PreferenceCategoryItemModel> {
    val strings = context.strings().prefs
    return listOf(
      PreferenceCategoryItemModel(
        title = strings.category_title_look_and_feel,
        subtitle = strings.category_subtitle_look_and_feel,
        category = LookAndFeel
      ),
      PreferenceCategoryItemModel(
        title = strings.category_title_sync,
        subtitle = strings.category_subtitle_sync,
        category = Sync
      ),
      PreferenceCategoryItemModel(
        title = strings.category_title_about_app,
        subtitle = Html.fromHtml(strings.category_subtitle_about_app, FROM_HTML_MODE_LEGACY),
        category = AboutApp
      )
    )
  }

  override fun createScreenExpander(): InboxItemExpander<ScreenKey> {
    return categoryAdapter.createScreenExpander()
  }
}
