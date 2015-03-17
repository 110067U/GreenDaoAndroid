package example.isuru.com.greendaoforandroidstudio.backend.repositories;

import android.content.Context;

import java.util.List;

import greendao.Message;
import greendao.MessageDao;
import example.isuru.com.greendaoforandroidstudio.BaseApplication;

public class MessageRepository {

    public static void insertOrUpdate(Context context, Message message) {
        getMessageDao(context).insertOrReplace(message);
    }

    public static void clearMessages(Context context) {
        getMessageDao(context).deleteAll();
    }

    public static void deleteMessageWithId(Context context, long id) {
        getMessageDao(context).delete(getMessageForId(context, id));
    }

    public static List<Message> getAllMessages(Context context) {
        return getMessageDao(context).loadAll();
    }

    public static Message getMessageForId(Context context, long id) {
        return getMessageDao(context).load(id);
    }

    private static MessageDao getMessageDao(Context c) {
        return ((BaseApplication) c.getApplicationContext()).getDaoSession().getMessageDao();
    }
}
