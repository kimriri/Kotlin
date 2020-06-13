package ko.co.rxkotlin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.combineLatest
import io.reactivex.subjects.BehaviorSubject

class SignIn : AppCompatActivity() {

    private lateinit var edSignInId: EditText
    private lateinit var edSignInPw: EditText
    private lateinit var btnSignInConfirm: Button


    private val compositeDisposable = CompositeDisposable()
    private val mIDBehaviorSubject = BehaviorSubject.createDefault("")
    private val mPWBehaviorSubject = BehaviorSubject.createDefault("")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        edSignInId = findViewById(R.id.sign_in_ID)
        edSignInPw = findViewById(R.id.sign_in_PW)
        btnSignInConfirm = findViewById(R.id.btn_sign_in_Confirm)
        edSignInId.doOnTextChanged { text, _, _, _ -> mIDBehaviorSubject.onNext(text.toString()) }
        edSignInPw.doOnTextChanged { text, _, _, _ -> mPWBehaviorSubject.onNext(text.toString()) }
        listOf(mIDBehaviorSubject, mPWBehaviorSubject).combineLatest {
            it.fold(true, { t1, t2 -> t1 && t2.isNotEmpty() })
        }.subscribe {
            if (it == false) {
                btnSignInConfirm.setBackgroundResource(R.drawable.togglebtn_sign_in_round_f)
                // Toast.makeText(this,"아이디와 비밀번호를 모두 입력해 주세요.",Toast.LENGTH_SHORT).show()

            } else {
                btnSignInConfirm.setBackgroundResource(R.drawable.togglebtn_sign_in_round)
            }
            btnSignInConfirm.isEnabled = it
        }.addTo(compositeDisposable)
    }


    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
