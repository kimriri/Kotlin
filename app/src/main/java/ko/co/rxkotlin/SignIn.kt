package ko.co.rxkotlin

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.combineLatest
import io.reactivex.subjects.BehaviorSubject

class SignIn : AppCompatActivity() {

    private lateinit var edSignInId: EditText
    private lateinit var edSignInPw: EditText
    private lateinit var tvSignInId: TextView
    private lateinit var tvSignInPw: TextView
    private lateinit var btnSignInConfirm: Button
    private val behaviorSubject = BehaviorSubject.createDefault(0L)

    private val compositeDisposable = CompositeDisposable()
    private val mIDBehaviorSubject = BehaviorSubject.createDefault("")
    private val mPWBehaviorSubject = BehaviorSubject.createDefault("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        edSignInId = findViewById(R.id.ed_sign_in_ID)
        edSignInPw = findViewById(R.id.ed_sign_in_PW)
        tvSignInId = findViewById(R.id.tv_sign_in_ID)
        tvSignInPw = findViewById(R.id.tv_sign_in_PW)
        btnSignInConfirm = findViewById(R.id.btn_sign_in_Confirm)
        edSignInId.doOnTextChanged { text, _, _, _ -> mIDBehaviorSubject.onNext(text.toString()) }
        edSignInPw.doOnTextChanged { text, _, _, _ -> mPWBehaviorSubject.onNext(text.toString()) }


        behaviorSubject.buffer(2, 1)
            .map { it[0] to it[1] }
            .subscribe {
                if (it.second - it.first < 2000L) {
                    super.onBackPressed()
                } else {
                    Toast.makeText(this, "한번더 누르면 앱을 종료 합니다. ", Toast.LENGTH_SHORT).show()
                }
            }.addTo(compositeDisposable)
        edSignInId.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvSignInId.visibility = View.INVISIBLE
            }
        })

        edSignInPw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                tvSignInPw.visibility = View.INVISIBLE
            }
        })

        listOf(mIDBehaviorSubject, mPWBehaviorSubject).combineLatest {
            it.fold(true, { t1, t2 -> t1 && t2.isNotEmpty() })

        }.subscribe {
            if (it == false) {
                btnSignInConfirm.setBackgroundResource(R.drawable.togglebtn_sign_in_round_f)
            } else {
                btnSignInConfirm.setBackgroundResource(R.drawable.togglebtn_sign_in_round)
            }
            btnSignInConfirm.isEnabled = it
        }.addTo(compositeDisposable)

    }


    override fun onBackPressed() {
        behaviorSubject.onNext(System.currentTimeMillis())
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
