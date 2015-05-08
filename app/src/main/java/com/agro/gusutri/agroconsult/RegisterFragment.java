package com.agro.gusutri.agroconsult;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.agro.gusutri.agroconsult.model.Dao;
import com.agro.gusutri.agroconsult.Service.Service;
import com.agro.gusutri.agroconsult.model.User;

/**
 * Created by dragos on 4/7/15.
 */
public class RegisterFragment extends Fragment {

    Service service = Service.getInstance();
    RegisterTask mRegisterTask=null;
    ProgressBar mProgress;
LinearLayout layoutFieldsView,layoutButtonsView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        final EditText txtName = (EditText) rootView.findViewById(R.id.register_txt_name);
        final EditText txtPassword = (EditText) rootView.findViewById(R.id.register_txt_password);
        final EditText txtEmail = (EditText) rootView.findViewById(R.id.register_txt_email);
        mProgress=(ProgressBar) rootView.findViewById(R.id.register_progress);
        layoutFieldsView=(LinearLayout) rootView.findViewById(R.id.register_linear_fields);
        layoutButtonsView=(LinearLayout) rootView.findViewById(R.id.register_linear_buttons);

        Button btnOk = (Button) rootView.findViewById(R.id.register_btn_ok);
        Button btnCancel = (Button) rootView.findViewById(R.id.register_btn_cancel);

        String email = getArguments().getString(Dao.EMAIL);
        txtEmail.setText(email);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = txtName.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                View focusView=null;
                if (service.isEmailValid(email)) {
                    if (service.isPasswordValid(password)) {
                        User user = new User(-1, name, email);
                        showProgress(true);
                        mRegisterTask = new RegisterTask(user, password, getActivity());
                        mRegisterTask.execute();
                    } else {
                        txtPassword.setError(getString(R.string.error_invalid_password));
                        focusView=txtPassword;
                    }
                } else {
                    txtEmail.setError(getString(R.string.error_invalid_email));
                    focusView=txtEmail;
                }
                if (focusView!=null)
                    focusView.requestFocus();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    private void showProgress(boolean show){
        mProgress.setVisibility(show ? View.VISIBLE : View.GONE);
        layoutButtonsView.setVisibility(show ? View.GONE : View.VISIBLE);
        layoutFieldsView.setEnabled(show? true:false);
    }

    private class RegisterTask extends AsyncTask<Void, Void, Boolean> {
        private User mUser;
        private String mPassword;
        private Context mContext;

        public RegisterTask(User user, String password, Context context) {
            mUser = user;
            mPassword = password;
            mContext = context;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            Dao dao = Dao.getInstance();
            mUser = dao.registerUser(mUser, mPassword);
            return mUser.getId() > 0;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            showProgress(false);
            mRegisterTask=null;
            if (success) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(Dao.USER, mUser);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
            } else {
                Toast.makeText(mContext, R.string.error_register_fail, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {
            showProgress(false);
            mRegisterTask=null;
        }
    }
}
