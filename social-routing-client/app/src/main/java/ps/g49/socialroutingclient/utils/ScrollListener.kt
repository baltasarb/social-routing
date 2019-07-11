package ps.g49.socialroutingclient.utils

import androidx.recyclerview.widget.RecyclerView

class ScrollListener(val func: () -> Unit): RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (!recyclerView.canScrollVertically(1)) {
            func()
        }
    }
}