package lavsam.gb.testingl2.presenter

import lavsam.gb.testingl2.view.ViewContract

internal interface PresenterContract {
    fun onAttach(view: ViewContract?)
    fun onDetach()
}