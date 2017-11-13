package net.kaijie.campus_ui;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by pc on 2017/11/13.
 */

public class MagicTextLengthWatcher implements TextWatcher {

    private int maxLength; // 儲存最大的字串長度
    private int currentEnd = 0; // 儲存目前字串改變的結束位置

    public  MagicTextLengthWatcher(final int maxLength) {
        setMaxLength(maxLength);
    }
    public final void setMaxLength(final int maxLength){
        if(maxLength >= 0) {
            this.maxLength=maxLength;
        }
        else {
            this.maxLength=0;
        }
    }
    public int getMaxLength(){
        return this.maxLength;
    }
    @Override
    public void beforeTextChanged(final CharSequence s,final int start,final int count,final int after) {

    }

    @Override
    public void onTextChanged(final CharSequence s,final int start,final int before,final int count) {
        currentEnd=start+count;
    }

    @Override
    public void afterTextChanged(final Editable s) {
        while (calculateLength(s) >maxLength){
            currentEnd--;
            s.delete(currentEnd,currentEnd+1);
        }
    }

    protected int calculateLength(final CharSequence c){
        int len = 0 ;
        final int l=c.length();
        for(int i =0 ; i<l;i++){
            final char tmp = c.charAt(i);
                if(tmp >= 0x20 && tmp<=0x7E){
                    len++;
                }
                else {
                    len +=2;
                }
            }
            return  len;
        }
}
