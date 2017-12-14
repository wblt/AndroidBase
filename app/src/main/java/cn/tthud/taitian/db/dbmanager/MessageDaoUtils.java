package cn.tthud.taitian.db.dbmanager;

import android.content.Context;
import android.util.Log;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cn.tthud.taitian.db.dao.MessageDao;
import cn.tthud.taitian.db.entity.Message;

/**
 * Created by wb on 2017/12/13.
 */

public class MessageDaoUtils {
    private static final String TAG = MeiziDaoUtils.class.getSimpleName();
    private DaoManager mManager;
    public MessageDaoUtils(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成meizi记录的插入，如果表未创建，先创建Meizi表
     *
     * @param
     * @return
     */
    public boolean insertMessage(Message message) {
        boolean flag = false;

        flag = mManager.getDaoSession().getMessageDao().insertOrReplace(message) == -1 ? false : true;
        Log.i(TAG, "insert Message :" + flag + "-->" + message.toString());

        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     * @param meiziList
     * @return
     */
    public boolean insertMultMessage(final List<Message> meiziList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Message message : meiziList) {
                        mManager.getDaoSession().insertOrReplace(message);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<Message> queryAllMessage(){
        return mManager.getDaoSession().loadAll(Message.class);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<Message> queryMessageByQueryBuilder(long id){
        QueryBuilder<Message> queryBuilder = mManager.getDaoSession().queryBuilder(Message.class);
        return queryBuilder.where(MessageDao.Properties.Msg_id.eq(id)).list();
    }
}
