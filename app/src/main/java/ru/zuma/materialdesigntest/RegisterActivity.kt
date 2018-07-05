package ru.zuma.materialdesigntest

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.support.v7.app.AppCompatActivity
import android.app.LoaderManager.LoaderCallbacks
import android.content.CursorLoader
import android.content.Loader
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast

import java.util.ArrayList

import kotlinx.android.synthetic.main.activity_register.*
import ru.zuma.materialdesigntest.rest.AuthService
import ru.zuma.materialdesigntest.rest.model.User
import android.app.Activity
import android.preference.PreferenceManager
import android.view.inputmethod.InputMethodManager
import ru.zuma.materialdesigntest.db.PREF_COOKIES


/**
 * A login screen that offers login via email/passwd.
 */
class RegisterActivity : AppCompatActivity(), LoaderCallbacks<Cursor> {
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    var isRetroFinish: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        // Set up the login form.
        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        email_sign_in_button.setOnClickListener { attemptLogin() }

        val adapter = ArrayAdapter.createFromResource(this,
                R.array.user_types_string, R.layout.user_type_spinner_item)
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Apply the adapter to the spinner
        userTypeSpinner.adapter = adapter
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (!isRetroFinish) {
            return
        }

        // Reset errors.
        email.error = null
        password.error = null
        password2.error = null
        name.error = null

        // Store values at the time of the login attempt.
        val emailStr = email.text.toString()
        val passwordStr = password.text.toString()
        val passwordStr2 = password2.text.toString()
        val nameStr = name.text.toString()
        val typeStr = resources.getStringArray(R.array.user_types)[userTypeSpinner.selectedItemId.toInt()]

        var cancel = false
        var focusView: View? = null

        // Check for a valid passwd, if the user entered one.
        if (TextUtils.isEmpty(passwordStr) || !isPasswordValid(passwordStr)) {
            password.error = getString(R.string.error_invalid_password)
            focusView = password
            cancel = true
        }

        if (!TextUtils.equals(passwordStr, passwordStr2)) {
            password2.error = getString(R.string.error_incorrect_password2)
            focusView = password2
            cancel = true
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailStr)) {
            email.error = getString(R.string.error_field_required)
            focusView = email
            cancel = true
        } else if (!isEmailValid(emailStr)) {
            email.error = getString(R.string.error_invalid_email)
            focusView = email
            cancel = true
        }

        // Check for a valid name.
        if (TextUtils.isEmpty(nameStr)) {
            name.error = getString(R.string.error_field_required)
        } else if (!isNameValid(nameStr)) {
            name.error = getString(R.string.error_invalid_name)
            focusView = name
            cancel = true
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView?.requestFocus()
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm!!.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0)

            showProgress(true)

            val user: User = User(nameStr, passwordStr, emailStr, typeStr)
            AuthService.registerCommi(user, onAuthSuccess = {
                Log.d(this.javaClass.simpleName, "Register finished, new cookie: $it")

                PreferenceManager.getDefaultSharedPreferences(this)
                        .edit()
                        .putString(PREF_COOKIES, it)
                        .apply()

                isRetroFinish = true
                setResult(Activity.RESULT_OK)
                finish()
            }, onAuthFailure = { m, t ->
                var toastMsg: String

                if (t != null) {
                    Log.e(this.javaClass.simpleName, "", t)
                    toastMsg = t.message!!
                } else if (m != null) {
                    Log.e(this.javaClass.simpleName, m)
                    toastMsg = m
                } else {
                    Log.e(this.javaClass.simpleName, "Unknown error")
                    toastMsg = "Unknown error"
                }

                Toast.makeText(
                        this,
                        toastMsg,
                        Toast.LENGTH_LONG
                ).show()

                isRetroFinish = true
                showProgress(false)
            })
            isRetroFinish = false
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 0 else 1).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_form.visibility = if (show) View.GONE else View.VISIBLE
                        }
                    })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                    .setDuration(shortAnimTime)
                    .alpha((if (show) 1 else 0).toFloat())
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            login_progress.visibility = if (show) View.VISIBLE else View.GONE
                        }
                    })
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_form.visibility = if (show) View.GONE else View.VISIBLE
        }
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<Cursor> {
        return CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE + " = ?", arrayOf(ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE),

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC")
    }

    override fun onLoadFinished(cursorLoader: Loader<Cursor>, cursor: Cursor) {
        val emails = ArrayList<String>()
        cursor.moveToFirst()
        while (!cursor.isAfterLast) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS))
            cursor.moveToNext()
        }

        addEmailsToAutoComplete(emails)
    }

    override fun onLoaderReset(cursorLoader: Loader<Cursor>) {

    }

    private fun addEmailsToAutoComplete(emailAddressCollection: List<String>) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        val adapter = ArrayAdapter(this@RegisterActivity,
                android.R.layout.simple_dropdown_item_1line, emailAddressCollection)

        email.setAdapter(adapter)
    }

    object ProfileQuery {
        val PROJECTION = arrayOf(
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY)
        val ADDRESS = 0
        val IS_PRIMARY = 1
    }


}
