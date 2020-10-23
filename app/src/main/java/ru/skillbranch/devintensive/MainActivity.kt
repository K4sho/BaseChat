package ru.skillbranch.devintensive

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.renderscript.Sampler
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardOpen
import ru.skillbranch.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener, ValueAnimator.AnimatorUpdateListener {
    private lateinit var benderImage: ImageView
    private lateinit var mEnlargeAnimation: Animation
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView
    lateinit var benderObj: Bender


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = findViewById(R.id.iv_bender)
        textTxt = findViewById(R.id.tv_text)
        messageEt = findViewById(R.id.et_message)
        sendBtn = findViewById(R.id.iv_send)

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name

        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        Log.d("M_MainActivity", "onCreate")
        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()
        sendBtn.setOnClickListener(this)

        messageEt.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                doIt()
            }
            false
        }

        mEnlargeAnimation = AnimationUtils.loadAnimation(this, R.anim.enlarge)
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)

        outState?.putString("STATUS", benderObj.status.name)
        outState?.putString("QUESTION", benderObj.question.name)
        Log.d("M_MainActivity", "onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")
    }

    private fun doIt() {
        val prevColor = benderObj.status.color
        val (phrase, color) = benderObj.listenAnswers(messageEt.text.toString())
        messageEt.setText("")
        val (r, g, b) = color
        benderImage.startAnimation((mEnlargeAnimation))
        animate(Color.rgb(prevColor.first, prevColor.second, prevColor.third), Color.rgb(r, g, b))
        textTxt.text = phrase
    }

    private fun animate(color1: Int, color2: Int) {
        val bgAnim = ValueAnimator.ofObject(ArgbEvaluator(), color1, color2)
        bgAnim.duration = 600
        bgAnim.addUpdateListener(this)
        bgAnim.start()
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send) {
            doIt()
            if(isKeyboardOpen()) hideKeyboard()
        }
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        benderImage.setColorFilter(animation?.animatedValue as Int, PorterDuff.Mode.MULTIPLY)
        sendBtn.setColorFilter(animation.animatedValue as Int)
    }
}