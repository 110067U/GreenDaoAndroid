package example.isuru.com.greendaoforandroidstudio.backend.repositories;

import android.content.Context;

import java.util.List;

import greendao.messageReceivers;
import greendao.messageReceiversDao;
import example.isuru.com.greendaoforandroidstudio.BaseApplication;


public class MessageReceiversRepository {

    public static void insertOrUpdate(Context context, messageReceivers message_receivers) {
        getMessage_ReceiversDao(context).insertOrReplace(message_receivers);
    }

    public static void clearMessage_Receivers(Context context) {
        getMessage_ReceiversDao(context).deleteAll();
    }

    public static void deleteMessage_ParentWithId(Context context, long id) {
        getMessage_ReceiversDao(context).delete(getMessage_ParentForId(context, id));
    }

    public static List<messageReceivers> getAllMessage_Receivers(Context context) {
        return getMessage_ReceiversDao(context).loadAll();
    }

    public static messageReceivers getMessage_ParentForId(Context context, long id) {
        return getMessage_ReceiversDao(context).load(id);
    }

    private static messageReceiversDao getMessage_ReceiversDao(Context c) {
        return ((BaseApplication) c.getApplicationContext()).getDaoSession().getMessageReceiversDao();
    }
}
