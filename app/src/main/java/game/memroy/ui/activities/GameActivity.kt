package game.memroy.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import game.memroy.MainActivity
import game.memroy.R
import game.memroy.data.model.Card
import game.memroy.data.model.Game
import game.memroy.data.model.Level
import game.memroy.viewModels.CardsViewModel
import kotlinx.android.synthetic.main.activity_game.*


class GameActivity : BaseActivity(), View.OnClickListener {
    override fun onClick(view: View?) {
        if (view is ImageView) {
            if (firstClickedIv == null) {
                makeImageViewVisible(view)
            } else if (secondClickedIv == null) {
                makeImageViewVisible(view)
                checkIsSameImage()
            }
        }
    }

    private fun startNewSet() {
        if (cards[firstClickedIndex!!].url == cards[secondClickedIndex!!].url) {
            startTimer(game.level.totalTimeInSecs)
            firstClickedIv?.let { it.visibility = View.INVISIBLE }
            secondClickedIv?.let { it.visibility = View.INVISIBLE }
        } else {
            firstClickedIv?.let {
                setImageViewDrawable(it,  R.drawable.rounded_bg)
                it.isClickable = true
            }
            secondClickedIv?.let {
                setImageViewDrawable(it,  R.drawable.rounded_bg)
                it.isClickable = true
            }
        }
        firstClickedIv = null
        secondClickedIv = null
        firstClickedIndex = null
        secondClickedIndex = null
        isGameFinished()
    }

    private fun isGameFinished() {
        var isGameFinished = true
        for (i in 0 until imageViews.size) {
            if (imageViews[i].visibility == View.VISIBLE) {
                isGameFinished = false
            }
        }
        if (isGameFinished) {
            showAlterDailog()
        }
    }

    private fun showAlterDailog() {
        Toast.makeText(this, "New game starting", Toast.LENGTH_SHORT).show()
        ll_main.postDelayed({
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }, 1000)
    }

    private fun checkIsSameImage() {
        if (cards[firstClickedIndex!!].url == cards[secondClickedIndex!!].url) {
            game.setScore(tv_timer.text.toString())
            tv_score.text = game.score.toString()
        }
        firstClickedIv?.let {
            it.postDelayed({ startNewSet() }, 1000)
        }
    }

    private fun setImageViewDrawable(iv: ImageView, drawableId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv.setImageDrawable(getDrawable(drawableId))
        } else {
            iv.setImageDrawable(this.resources.getDrawable(drawableId))
        }
    }

    fun getImage(imageName: String): Int {
        return resources.getIdentifier(imageName, "drawable", packageName)
    }

    private fun makeImageViewVisible(clickedIv: ImageView) {
        clickedIv.isClickable = false
        for (i in 0 until imageViews.size) {
            if (imageViews[i].id == clickedIv.id) {
                val options = RequestOptions()
                options.apply {
                    fitCenter()
                    override(clickedIv.width, clickedIv.height)
                }
                Glide.with(this)
                    .load(getImage(cards[i].url))
                    .apply(options)
                    .into(clickedIv)
                if (firstClickedIndex == null) {
                    firstClickedIv = clickedIv
                    firstClickedIndex = i
                } else {
                    secondClickedIndex = i
                    secondClickedIv = clickedIv
                }
            }
        }
    }

    private var timer: CountDownTimer? = null
    private lateinit var cards: List<Card>
    private var firstClickedIv: ImageView? = null
    private var firstClickedIndex: Int? = null
    private var secondClickedIndex: Int? = null
    private var secondClickedIv: ImageView? = null
    private lateinit var game: Game
    private var linearLayouts: MutableList<LinearLayout> = mutableListOf()
    private var imageViews: MutableList<ImageView> = mutableListOf()
    private lateinit var cardsViewModel: CardsViewModel
    override val layout: Int
        get() = R.layout.activity_game


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
    }

    private fun initData() {
        val level = Level(1)
        game = Game(level)
        val cardsCount = game.getCardsCount()
        cardsViewModel = ViewModelProviders.of(this).get(CardsViewModel::class.java)
        cards = cardsViewModel.getCards(cardsCount)
        addLinearLayouts(level.row)
        addImageLayouts(linearLayouts, level.column)
        ll_main.postDelayed({ startTimer(level.totalTimeInSecs) }, 1000)
    }


    private fun startTimer(timeInSecs: Int) {
        var time = timeInSecs
        if (timer != null) {
            timer?.let { it.cancel() }
        }
        timer = object : CountDownTimer((timeInSecs * 1000).toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                tv_timer.text = getTimeForDisplay(time)
                time--
            }

            override fun onFinish() {
                tv_timer.text = "Try again"
            }

        }.start()

    }

    fun getTimeForDisplay(number: Int): String {
        return if (number <= 9) "0$number" else number.toString()
    }

    private fun addImageLayouts(linearLayouts: List<LinearLayout>, row: Int) {
        for (i in 0 until linearLayouts.size) {
            for (j in 0 until row) {
                val imageView = getImageView()
                imageView.setOnClickListener(this)
                imageViews.add(imageView)
                linearLayouts[i].addView(imageView)
            }
        }
    }

    private fun getImageView(): ImageView {
        val iv = ImageView(this)
        iv.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f
            )
        iv.id = View.generateViewId()
        iv.scaleType = ImageView.ScaleType.FIT_CENTER

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv.setImageDrawable(getDrawable( R.drawable.rounded_bg))
        } else {
            iv.setImageDrawable(this.resources.getDrawable(R.drawable.rounded_bg))
        }
        return iv
    }

    private fun addLinearLayouts(row: Int) {
        for (i in 0 until row) {
            val linearLayout = getLinearLayout()
            linearLayouts.add(linearLayout)
            ll_main.addView(linearLayout)
        }
    }

    private fun getLinearLayout(): LinearLayout {
        val linearLayout = LinearLayout(this)
        linearLayout.layoutParams =
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
            )
        linearLayout.orientation = LinearLayout.HORIZONTAL
        linearLayout.id = View.generateViewId()
        return linearLayout
    }

}