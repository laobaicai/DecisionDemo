package com.test.demo.bean;

import android.database.Cursor;

import com.test.demo.constants.BaseColumns;

/**
 * Created by caicai on 2017/8/29.
 */

public class BaseBean {
    public BaseBean(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID));
        createTime = cursor.getLong(cursor.getColumnIndex(BaseColumns.CREATE_TIME));
        modifyTime = cursor.getLong(cursor.getColumnIndex(BaseColumns.MODIFY_TIME));
    }

    private long id;
    private long createTime;
    private long modifyTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
    }
}
