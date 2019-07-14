package ps.g49.socialroutingclient.adapters.listeners

interface OnPointClickListener {
    fun onPointClick(position: Int, isButtonClick: Boolean = false, isSaved: Boolean = false)
}