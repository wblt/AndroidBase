package cn.tthud.taitian.db.dbmanager;

import android.content.Context;
import android.util.Log;


import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cn.tthud.taitian.db.dao.MeiziDao;
import cn.tthud.taitian.db.entity.Meizi;

/**
 * 完成对某一张数据表的具体操作，ORM操作
 * Created by Mr.sorrow on 2017/5/5.
 */

public class MeiziDaoUtils {
    private static final String TAG = MeiziDaoUtils.class.getSimpleName();
    private DaoManager mManager;

    public MeiziDaoUtils(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成meizi记录的插入，如果表未创建，先创建Meizi表
     *
     * @param meizi
     * @return
     */
    public boolean insertMeizi(Meizi meizi) {
        boolean flag = false;

        flag = mManager.getDaoSession().getMeiziDao().insert(meizi) == -1 ? false : true;
        Log.i(TAG, "insert Meizi :" + flag + "-->" + meizi.toString());

        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     * @param meiziList
     * @return
     */
    public boolean insertMultMeizi(final List<Meizi> meiziList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (Meizi meizi : meiziList) {
                        mManager.getDaoSession().insertOrReplace(meizi);
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
     * 修改一条数据
     * @param meizi
     * @return
     */
    public boolean updateMeizi(Meizi meizi){
        boolean flag = false;
        try {
            mManager.getDaoSession().update(meizi);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param meizi
     * @return
     */
    public boolean deleteMeizi(Meizi meizi){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(meizi);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     * @return
     */
    public boolean deleteAll(){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(Meizi.class);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<Meizi> queryAllMeizi(){
        return mManager.getDaoSession().loadAll(Meizi.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public Meizi queryMeiziById(long key){
        return mManager.getDaoSession().load(Meizi.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<Meizi> queryMeiziByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(Meizi.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<Meizi> queryMeiziByQueryBuilder(long id){
        QueryBuilder<Meizi> queryBuilder = mManager.getDaoSession().queryBuilder(Meizi.class);
        return queryBuilder.where(MeiziDao.Properties._id.eq(id)).list();
    }

    /*  使用方法用例，供参考
    case R.id.insertsingle:
                mMeiziDaoUtils.insertMeizi(new Meizi(null, "Google",
                        "http://7xi8d6.com1.z0.glb.clouddn.com/2017-05-05-18251898_1013302395468665_8734429858911748096_n.jpg"));
                break;
            case R.id.multinsert:
                List<Meizi> meiziList = new ArrayList<>();
                meiziList.add(new Meizi(null, "HuaWei",
                        "http://7xi8d648096_n.jpg"));
                meiziList.add(new Meizi(null, "Apple",
                        "http://7xi8d648096_n.jpg"));
                meiziList.add(new Meizi(null, "MIUI",
                        "http://7xi8d648096_n.jpg"));
                mMeiziDaoUtils.insertMultMeizi(meiziList);
                break;
            case R.id.update:
                Meizi meizi = new Meizi();
                meizi.set_id(1002l);
                meizi.setUrl("http://baidu.jpg");
                mMeiziDaoUtils.updateMeizi(meizi);
                break;
            case R.id.deletesingle:
                Meizi meizi1 = new Meizi();
                meizi1.set_id(1002l);
                mMeiziDaoUtils.deleteMeizi(meizi1);
                break;
            case R.id.deleteMult:
                mMeiziDaoUtils.deleteAll();
                break;
            case R.id.checksingle:
                Log.i(TAG, mMeiziDaoUtils.queryMeiziById(1008l).toString());
                break;
            case R.id.checkmult:
                List<Meizi> meiziList1 = mMeiziDaoUtils.queryAllMeizi();
                for (Meizi meizi2 : meiziList1) {
                    Log.i(TAG, meizi2.toString());
                }
                break;
            case R.id.querybuilder:
//                String sql = "where _id > ?";
//                String[] condition = new String[]{"1008"};
//                List<Meizi> meiziList2 = mMeiziDaoUtils.queryMeiziByNativeSql(sql, condition);
//                for (Meizi meizi2 : meiziList2) {
//                    Log.i(TAG, meizi2.toString());
//                }
                List<Meizi> meiziList2 = mMeiziDaoUtils.queryMeiziByQueryBuilder(1008);
                for (Meizi meizi2 : meiziList2) {
                    Log.i(TAG, meizi2.toString());
                }
                break;
        }
     */
}
