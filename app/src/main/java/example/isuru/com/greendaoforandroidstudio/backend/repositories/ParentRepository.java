package example.isuru.com.greendaoforandroidstudio.backend.repositories;

import android.content.Context;

import java.util.List;

import greendao.Parent;
import greendao.ParentDao;
import example.isuru.com.greendaoforandroidstudio.BaseApplication;

public class ParentRepository {

    public static void insertOrUpdate(Context context, Parent message) {
        getParentDao(context).insertOrReplace(message);
    }

    public static void clearParents(Context context) {
        getParentDao(context).deleteAll();
    }

    public static void deleteParentWithId(Context context, long id) {
        getParentDao(context).delete(getParentForId(context, id));
    }

    public static List<Parent> getAllParents(Context context) {
        return getParentDao(context).loadAll();
    }

    public static Parent getParentForId(Context context, long id) {
        return getParentDao(context).load(id);
    }

    private static ParentDao getParentDao(Context c) {
        return ((BaseApplication) c.getApplicationContext()).getDaoSession().getParentDao();
    }
}
