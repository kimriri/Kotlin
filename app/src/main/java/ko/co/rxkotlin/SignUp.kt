package ko.co.rxkotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.combineLatest
import io.reactivex.subjects.BehaviorSubject

class SignUp : AppCompatActivity() {

    private lateinit var tvSignUpNickname: TextView
    private lateinit var tvSignUpNicknameCheck: TextView
    private lateinit var edSignUpNickname: EditText

    private lateinit var tvSignUpID: TextView
    private lateinit var tvSignUpIDError: TextView
    private lateinit var edSignUpID: EditText

    private lateinit var tvSignUpPW: TextView
    private lateinit var tvSignUpPWError: TextView
    private lateinit var edSignUpPW: EditText

    private lateinit var btnSignUpConfirm: Button
    private val behaviorSubject = BehaviorSubject.createDefault(0L)

    private val compositeDisposable = CompositeDisposable()
    private val mNicknameBehaviorSubject = BehaviorSubject.createDefault("")
    private val mIDBehaviorSubject = BehaviorSubject.createDefault("")
    private val mPWBehaviorSubject = BehaviorSubject.createDefault("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        tvSignUpNickname = findViewById(R.id.tv_sign_up_nickname)
        tvSignUpNicknameCheck = findViewById(R.id.tv_sign_up_nickname_check)
        edSignUpNickname = findViewById(R.id.ed_sign_up_nickname)

        tvSignUpID = findViewById(R.id.tv_sign_up_ID)
        tvSignUpIDError = findViewById(R.id.tv_sign_up_ID_error)
        edSignUpID = findViewById(R.id.ed_sign_up_ID)

        tvSignUpPW = findViewById(R.id.tv_sign_up_PW)
        tvSignUpPWError = findViewById(R.id.tv_sign_up_PW_error)
        edSignUpPW = findViewById(R.id.ed_sign_up_PW)

        btnSignUpConfirm = findViewById(R.id.btn_sign_up_Confirm)
        edSignUpNickname.doOnTextChanged { text, _, _, _ -> mNicknameBehaviorSubject.onNext(text.toString()) }
        edSignUpID.doOnTextChanged { text, _, _, _ -> mIDBehaviorSubject.onNext(text.toString()) }
        edSignUpPW.doOnTextChanged { text, _, _, _ -> mPWBehaviorSubject.onNext(text.toString()) }

        listOf(mNicknameBehaviorSubject, mIDBehaviorSubject, mPWBehaviorSubject).combineLatest {
            it.fold(true, { t1, t2 -> t1 && t2.isNotEmpty() })
        }.subscribe({

            if (it == false) {
                btnSignUpConfirm.setBackgroundResource(R.drawable.togglebtn_round_f)
            } else {
                    btnSignUpConfirm.setBackgroundResource(R.drawable.togglebtn_round)
            }
        }, {
            it.printStackTrace()

        }).addTo(compositeDisposable)

        val edSignUpNickname = edSignUpNickname.textChanges()
            .filter { it.length <= 1 }
            .map { it.isEmpty() }
            .subscribe({

                if (it) {
                    tvSignUpNickname.visibility = View.VISIBLE
                } else {
                    tvSignUpNickname.visibility = View.INVISIBLE
                }
            }, {
                it.printStackTrace()
            })
        compositeDisposable.add(edSignUpNickname)

        val edSignUpID = edSignUpID.textChanges()
            .filter { it.length <= 1 }
            .map { it.isEmpty() }
            .subscribe({
                if (it) {

                    tvSignUpID.visibility = View.VISIBLE
                } else {
                    tvSignUpID.visibility = View.INVISIBLE
                }
            }, {
                it.printStackTrace()
            })
        compositeDisposable.add(edSignUpID)

        val edSignUpPW = edSignUpPW.textChanges()
            .filter { it.length <= 1 }
            .map { it.isEmpty() }
            .subscribe({

                if (it) {
                    tvSignUpPW.visibility = View.VISIBLE
                } else {
                    tvSignUpPW.visibility = View.INVISIBLE
                }
            }, {

                it.printStackTrace()
            })
        compositeDisposable.add(edSignUpPW)

    }


    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
