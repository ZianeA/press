package press.home

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import io.reactivex.subjects.PublishSubject
import me.saket.press.shared.home.HomeUiModel
import press.home.NoteAdapter.NoteVH
import me.saket.press.shared.home.HomeUiModel.Note as Model

// TODO: rename to NoteListAdapter
class NoteAdapter : ListAdapter<Model, NoteVH>(NoteDiffer()) {
  val clicks = PublishSubject.create<HomeUiModel.Note>()

  init {
    setHasStableIds(true)
    stateRestorationPolicy = PREVENT_WHEN_EMPTY
  }

  override fun getItemId(position: Int) =
    getItem(position).adapterId

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
    NoteVH(
      NoteRowView(parent.context).apply {
        setOnClickListener { clicks.onNext(model) }
      }
    )

  override fun onBindViewHolder(holder: NoteVH, position: Int) {
    holder.view.render(getItem(position))
  }

  class NoteVH(val view: NoteRowView) : ViewHolder(view)
}

private class NoteDiffer : DiffUtil.ItemCallback<Model>() {
  override fun areItemsTheSame(oldItem: Model, newItem: Model) = oldItem.id == newItem.id
  override fun areContentsTheSame(oldItem: Model, newItem: Model) = oldItem == newItem
}
