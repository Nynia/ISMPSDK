package com.example.ridiculous.ismpsdk;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.logging.Handler;

/**
 * Created by Ridiculous on 2016/6/21.
 */
public class VercodeDialog extends Dialog {
    public VercodeDialog(Context context, int theme) {
        super(context, theme);
    }

    public VercodeDialog(Context context) {
        super(context);
    }

    public static class Builder {
        private Context context;
        private View contentView;
        private String getButtonText;
        private String postButtonText;
        private View phoneNumView;
        private View VerCodeView;
        // 对话框按钮监听事件
        private DialogInterface.OnClickListener getButtonClickListener;

        private DialogInterface.OnClickListener postButtonClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public View getPhoneNumView() {
            return this.phoneNumView;
        }
        public View getVerCodeView() {
            return this.VerCodeView;
        }
        /**
         * 设置自定义的对话框内容
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }
        /**
         * 设置back按钮的事件和文本
         * @param backButtonText
         * @param listener
         * @return
         */
        public Builder setGetButton(String text, DialogInterface.OnClickListener listener) {
            this.getButtonText = text;
            this.getButtonClickListener = listener;
            return this;
        }

        /**
         * 设置确定按钮事件和文本
         * @param negativeButtonText
         * @param listener
         * @return
         */
        public Builder setPostButton(String text, DialogInterface.OnClickListener listener) {
            this.postButtonText = text;
            this.postButtonClickListener = listener;
            return this;
        }

        /**
         * 创建自定义的对话框
         */
        public VercodeDialog create() {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            // 实例化自定义的对话框主题
            final VercodeDialog dialog = new VercodeDialog(context, R.style.Dialog);

            View layout = inflater.inflate(R.layout.vercode_dialog, null);
            phoneNumView = layout.findViewById(R.id.phone_num);
            VerCodeView = layout.findViewById(R.id.ver_code);

            dialog.addContentView(layout,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            final Button getButton = ((Button) layout.findViewById(R.id.getcode));

            getButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
            });

            final Button postButton = ((Button) layout.findViewById(R.id.postcode));

            postButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    postButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                }
            });
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
