package com.yeongsoo.githubsample.model.local;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.yeongsoo.githubsample.model.data.SearchUserItemContents;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yeongsookim on 2018-05-16.
 *
 * GitHub 사용자 검색 데이터를 DB에서 저장 및 관리될 수 있게 구현된 클래스
 */

public class GithubFavoriteDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "GITHUB_FAVORITE";
    private static final int DB_VERSION = 1;

    private static final String COLUMN_IDX = "_ID";
    private static final String COLUMN_ID = "LOGIN_ID";
    private static final String COLUMN_AVATAR_URL = "AVATAR_URL";
    private static final String COLUMN_HOME_URL = "HOME_URL";

    private static GithubFavoriteDbHelper mInstance;
    private static SQLiteDatabase mWritableDb;
    private static SQLiteDatabase mReadableDb;

    private Context mContext;

    public static GithubFavoriteDbHelper getInstance(Context context, SQLiteDatabase.CursorFactory factory) {
        if (mInstance == null) {
            mInstance = new GithubFavoriteDbHelper(context, factory);
            mWritableDb = mInstance.getWritableDatabase();
            mReadableDb = mInstance.getReadableDatabase();
        }

        return mInstance;
    }

    public GithubFavoriteDbHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory, DB_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuffer queryBuilder = new StringBuffer();
        queryBuilder.append("CREATE TABLE ").append(DB_NAME)
                .append(" (")
                .append(COLUMN_IDX).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(COLUMN_ID).append(" TEXT,")
                .append(COLUMN_AVATAR_URL).append(" TEXT,")
                .append(COLUMN_HOME_URL).append(" TEXT")
                .append(")");

        db.execSQL(queryBuilder.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    void insertData(final SearchUserItemContents vo) {
        StringBuffer queryBuilder = new StringBuffer();
        queryBuilder.append("INSERT INTO ").append(DB_NAME)
                .append(" (")
                .append(COLUMN_ID).append(", ")
                .append(COLUMN_AVATAR_URL).append(", ")
                .append(COLUMN_HOME_URL)
                .append(")")
                .append("VALUES (?, ?, ?)");

        mWritableDb.execSQL(queryBuilder.toString(),
                new Object[] {vo.getLoginId(), vo.getAvatarUrl(), vo.getUserHomeUrl()});
    }

    //Login ID필드 기준으로 정렬해서 보여주는 것으로 한다.
    List<FavoriteUserItemVo> queryData(final String queryLoginId, boolean isCompleteAccordance, final int count, final int skip) {
        StringBuffer queryBuilder = new StringBuffer();
        queryBuilder.append("SELECT ")
                .append(COLUMN_IDX).append(", ")
                .append(COLUMN_ID).append(", ")
                .append(COLUMN_AVATAR_URL).append(", ")
                .append(COLUMN_HOME_URL)
                .append(" FROM ").append(DB_NAME);

        if (!TextUtils.isEmpty(queryLoginId)) {
            queryBuilder.append(" WHERE ").append(COLUMN_ID)
                    .append(" LIKE ");

            if (isCompleteAccordance) {
                queryBuilder.append("'").append(queryLoginId).append("'");
            } else {
                queryBuilder.append("'%").append(queryLoginId).append("%'");
            }
        }

        queryBuilder.append(" ORDER BY ").append(COLUMN_ID).append(" ASC")
                .append(" LIMIT ").append(count)
                .append(" OFFSET ").append(skip);


        Cursor cursor = mReadableDb.rawQuery(queryBuilder.toString(), null);

        List<FavoriteUserItemVo> selectedData = new ArrayList<>();
        FavoriteUserItemVo user = null;

        while (cursor.moveToNext()) {
            user = new FavoriteUserItemVo(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3));
            selectedData.add(user);
        }

        cursor.close();

        return selectedData;
    }

    //해당 경우에는 업데이트가 필요 없다. 추가/삭제 기능만
    void updateData() {

    }

    void deleteData(String loginId) {
        StringBuffer queryBuilder = new StringBuffer();
        queryBuilder.append("DELETE ")
                .append(" FROM ").append(DB_NAME)
                .append(" WHERE ").append(COLUMN_ID).append("='")
                .append(loginId).append("';");

        mWritableDb.execSQL(queryBuilder.toString());
    }

    int getConditionalCount(final String queryLoginId, boolean isCompleteAccordance) {
        int count;
        StringBuffer queryBuilder = new StringBuffer();
        queryBuilder.append("SELECT ")
                .append(COLUMN_ID)
                .append(" FROM ").append(DB_NAME);

        if (!TextUtils.isEmpty(queryLoginId)) {
            queryBuilder.append(" WHERE ").append(COLUMN_ID)
                    .append(" LIKE ");

            if (isCompleteAccordance) {
                queryBuilder.append("'").append(queryLoginId).append("'");
            } else {
                queryBuilder.append("'%").append(queryLoginId).append("%'");
            }
        }

        Cursor cursor = mReadableDb.rawQuery(queryBuilder.toString(), null);
        cursor.moveToFirst();
        count = cursor.getCount();
        cursor.close();

        return count;
    }

    int getTotalCount() {
        int count;
        StringBuffer queryBuilder = new StringBuffer();
        queryBuilder.append("SELECT *")
                .append(" FROM ").append(DB_NAME);

        Cursor cursor = mReadableDb.rawQuery(queryBuilder.toString(), null);
        cursor.moveToFirst();
        count = cursor.getCount();
        cursor.close();

        return count;
    }


    public void finish() {
        try {
            if (mWritableDb != null && mWritableDb.isOpen()) {
                mWritableDb.close();
            }

            if (mReadableDb != null && mReadableDb.isOpen()) {
                mReadableDb.close();
            }

            close();
        } catch (Exception e) {
        } finally {
            mInstance = null;
        }
    }
}
