package com.example.weatherapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weatherapp.R;


public class EditFormDIalogFragment extends DialogFragment implements TextView.OnEditorActionListener, View.OnClickListener {

    private EditText mNameEditText;
    private EditText mEmailEditText;


    public EditFormDIalogFragment() {
        // 创建 DialogFragment 需要一个空的构造器
        // 构造器必须不带任何参数
        // 使用 newInstance 作为构造工厂创建对象，而不是构造器
    }
    /****************************创建DialogFragment最小化配置**************************************/
    public static EditFormDIalogFragment newInstance(String title){
        EditFormDIalogFragment frag = new EditFormDIalogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_form_dialog, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 从 view 获取 EditText
        mNameEditText = (EditText)view.findViewById(R.id.txt_your_name);
        mEmailEditText = (EditText)view.findViewById(R.id.txt_email);
        // 从 bundle 获取参数并且设置 title
        String title = getArguments().getString("title","Enter Name");
        // 显示一个模拟的键盘
        mNameEditText.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // mEditText 设置监听事件
        mNameEditText.setOnEditorActionListener(this);
        mEmailEditText.setOnEditorActionListener(this);

        // 设置按键事件
        Button update_submit = (Button)view.findViewById(R.id.update_submit);
        Button update_cancel = (Button)view.findViewById(R.id.update_cancel);
        update_submit.setOnClickListener(this);
        update_cancel.setOnClickListener(this);
    }
    /********************************************************************************/

    /*****************************将数据传给活动*************************************/
    // 创建带有一个方法的接口
    public interface EditNameDialogListener{
        void onFinishEditDialog(Bundle message);
    }
    // 点击按钮事件监听器
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.update_submit:{
                Bundle b = new Bundle();
                b.putString("name",mNameEditText.getText().toString());
                b.putString("email",mEmailEditText.getText().toString());

                EditNameDialogListener listener = (EditNameDialogListener) getActivity();
                listener.onFinishEditDialog(b);
                // 关闭 Dialog
                dismiss();
            }
                break;
            default:
                break;
        }
    }

    // 更改文字事件监听器
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if(EditorInfo.IME_ACTION_DONE == actionId){
            /**
            // getActivity() 使得这个Fragment获得了父活动的实例
            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            listener.onFinishEditDialog(mNameEditText.getText().toString());
            // 关闭 Dialog
            dismiss();
             */
            return true;
        }
        return false;
    }
    /***************************************************************************/
}
