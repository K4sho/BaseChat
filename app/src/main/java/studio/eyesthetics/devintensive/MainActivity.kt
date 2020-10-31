package ru.skillbranch.devintensive

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.hideKeyboard
import ru.skillbranch.devintensive.extensions.isKeyboardOpen

import studio.eyesthetics.devintensive.models.Bender

class MainActivity : AppCompatActivity(), View.OnClickListener,ValueAnimator.AnimatorUpdateListener {
    private lateinit var benderImage: ImageView
    private lateinit var mEnlargeAnimation: Animation
    lateinit var textTxt: TextView
    lateinit var messageEt: EditText
    lateinit var sendBtn: ImageView
    lateinit var benderObj: Bender

/**
    * Вызывается при первом создании или перезапуске Activity
    *
    * Здесь задается внешний вид активности (UI) через метод setContentView().
    * инициализируются представления
    * представления связываются с необходимыми данными и ресурсами
    * связываются данными со списками
    *
    * Этот метод также предоставляет Bundle, содержащий ранее сохранённое
    * состояние Activity, если оно было.
    *
    * Всегда сопровождается вызовом onStart()
    */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendBtn = iv_send

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
            doIt() }
        false
        }

        mEnlargeAnimation = AnimationUtils.loadAnimation(this, R.anim.enlarge)
    }

    /**
     * Если Activity возвращается в приоритетный режим после вызовы onStop(),
     * то в этом случае вызывается метод onRestart().
     * Т.е. вызывается после того, как Activity была остановлена и снова была запущена пользователем.
     * Всегда сопровождается вызовом метода onStart().
     *
     * используется для специальных действий, которые должны выполняться только при повторном запуске Activity
     */

    override fun onRestart() {
        super.onRestart()
        Log.d("M_MainActivity", "onRestart")
    }

    /**
     * При вызове onStart() окно еще не видно пользователю, но вскоре будет видно.
     * Вызывается непосредственно перед тем, как активность становится видимой пользователю.
     *
     * Чтение из БД
     * Запуск сложной анимации
     * Запуск потоков, отслеживания показаний датчиков, запросов к GPS, таймеров, сервисов или других процессов,
     * которые нужны исключительно для обновления пользовательского интерфейса
     *
     * Затем следует onResume(), если Activity выходит на передний план
     */

    override fun onStart() {
        super.onStart()
        Log.d("M_MainActivity", "onStart")
    }

    /**
     * Вызывается, когда Activity начнет взаимодействовать с пользователем.
     *
     * запуск воспроизведения анимации, аудио и видео
     * регистрация любых BroadcastReceiver или других процессов, которые вы освободили/приостановили в onPause()
     * выполнение любых других инициализаций, которые должны происходит после того, как Activity становится активной (камера).
     *
     * Тут должен быть максимально легкий и быстрый код, чтобы приложение оставалось отзывчивым
     */

    override fun onResume() {
        super.onResume()
        Log.d("M_MainActivity", "onResume")
    }

    /**
     * Метод onPause() вызывается после сворачивания текущей активности или перехода к новой
     * От onPause() можно перейти к вызову либо onResume(), либо onStop()/
     *
     * остановка анимации, аудио и видео
     * сохранение состояния пользовательского ввода (легкие процессы)
     * сохранение в DB, если данные должны быть доступны в новой Activity
     * остановка сервисов, подписок, BroadcastReceiver
     *
     * Тут должен быть максимально легкий и быстрый код, чтобы приложение оставалось отзывчивым
     */

    override fun onPause() {
        super.onPause()
        benderImage.clearAnimation()
        Log.d("M_MainActivity", "onPause")
    }

    /**
     * Метод onStop() вызывается когда Activity становится невидимой для пользователя.
     * Это может произойти при её уничтожении или, если была запущена другая Activity (существующая или новая),
     * перекрывшая окно текущей Activity.
     *
     * запись в базу данных
     * приостановка сложной анимации
     * приостановка потоков, отслеживания показаний датчиков, запросов к GPS, таймеров, сервисов или других процессов,
     * которые нужны исключительно для обновления пользовательского интерфейса
     *
     * Не вызывается при вызове метода finish() у Activity
     */

    override fun onStop() {
        super.onStop()
        Log.d("M_MainActivity", "onStop")
    }

    /**
     * Метод вызывается по окончании работы Activity, при вызове метода finish() или в случае,
     * когда системы уничтожает этот экземпляр активности для освобождения ресурсов.
     */

    override fun onDestroy() {
        super.onDestroy()
        Log.d("M_MainActivity", "onDestroy")
    }

    /**
     *  Этот метод сохраняет состояние представления в Bundle
     *  Для API Level < 28 (Android P) этот метод будет выполняться до onStop(), и нет никаких гарантий, что
     *  это произойдет до или после onPause(один вариант произойдет точно)
     *  Для API Level >= 28 будет вызван после onStop()
     *  Не будет вызван, если Activity будет явно закрыто пользователем при нажатии на системную клавишу back
     */

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putString("STATUS", benderObj.status.name)
        outState?.putString("QUESTION", benderObj.question.name)
        Log.d("M_MainActivity", "onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.iv_send) {
            doIt()
            if(isKeyboardOpen()) hideKeyboard()
        }
    }

    private fun doIt() {
        val prevColor = benderObj.status.color
        val (phrase, color) = benderObj.listenAnswer(messageEt.text.toString())
        messageEt.setText("")
        val (r, g, b) = color
        benderImage.startAnimation(mEnlargeAnimation)
        animate(Color.rgb(prevColor.first, prevColor.second, prevColor.third), Color.rgb(r, g, b))
        textTxt.text = phrase
    }

    private fun animate(color1: Int, color2: Int) {
        val bgAnim = ValueAnimator.ofObject(ArgbEvaluator(), color1, color2)
        bgAnim.duration = 600
        bgAnim.addUpdateListener(this)
        bgAnim.start()
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
        benderImage.setColorFilter(animation?.animatedValue as Int, PorterDuff.Mode.MULTIPLY)
        sendBtn.setColorFilter(animation.animatedValue as Int)
    }
}