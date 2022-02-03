package lavsam.gb.testingl2.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_details.*
import lavsam.gb.testingl2.R
import lavsam.gb.testingl2.presenter.DetailsPresenter
import lavsam.gb.testingl2.presenter.PresenterDetailsContract
import java.util.*

class DetailsFragment: Fragment(), ViewDetailsContract  {

    private val presenter: PresenterDetailsContract = DetailsPresenter(this)

    companion object {
        private const val TOTAL_COUNT_EXTRA = "TOTAL_COUNT_EXTRA"
        @JvmStatic
        fun newInstance(counter: Int) = DetailsFragment().apply {
            arguments = bundleOf(TOTAL_COUNT_EXTRA to counter)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
    }

    private fun setUI() {
        arguments?.let {
            val counter = it.getInt(TOTAL_COUNT_EXTRA, 0)
            presenter.setCounter(counter)
            setCountText(counter)
        }
        decrementButton.setOnClickListener { presenter.onDecrement() }
        incrementButton.setOnClickListener { presenter.onIncrement() }
    }

    override fun setCount(count: Int) {
        setCountText(count)
    }

    private fun setCountText(count: Int) {
        totalCountTextView.text =
            String.format(Locale.getDefault(), getString(R.string.results_count), count)
    }
}