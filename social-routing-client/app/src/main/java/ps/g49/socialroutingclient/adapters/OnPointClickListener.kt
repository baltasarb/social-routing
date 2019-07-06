package ps.g49.socialroutingclient.adapters

interface OnPointClickListener {
    fun onPointClick(position: Int, isButtonClick: Boolean = false, isSaved: Boolean = false)
}