import com.mongodb.BasicDBObject;
import com.mongodb.client.model.IndexModel;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.open.mongodb.template.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @version 1.0
 * @Author cmy
 * @Date 2023/10/7 9:16
 * @desc MongoTemplate连接与使用
 */
@SpringBootTest
public class MongoTemplateTest {
    /**
     * Mongo模板
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 添加操作-insert
     */
    @Test
    public void contextLoads() {
        //使用@Document("User")定义User对象
        User user = new User();
        user.setAge(20);
        user.setName("test");
        user.setEmail("123@qq.com");
        //导入User文档
        User user1 = mongoTemplate.insert(user);
        System.out.println(user1);
    }

    /**
     * 查询所有记录-findAll
     */
    @Test
    public void findAll(){
        List<User> all = mongoTemplate.findAll(User.class);
        System.out.println(all);
    }

    /**
     * 根据id查询-findById
     */
    @Test
    public void findId(){
        User user = mongoTemplate.findById("6520c49db4920626509949f5", User.class);
        System.out.println(user);
    }

    /**
     * 条件查询-find
     */
    @Test
    public void findUserList(){
        //Query类提供了一种构建查询条件的方式，它继承了MongoDB的DBObject类，因此可以像操作Map一样操作Query对象。开发人员可以通过添加Criteria或砥砺Query对象的属性和值来构建查询条件。
        //Criteria类是用于构建查询条件的接口，它提供了一组静态方法，用于构建不同类型的查询条件。例如，比较运算符、逻辑运算符、文本搜索等。开发人员可以通过Criteria类的方法构建各种类型的查询条件。

        /**
         * Criteria类常用用法：
         * is(Object value)：等于（=）
         * eq(String key, Object value)：等于（=）
         * ne(String key, Object value)：不等于（!=）
         * lt(String key, Object value)：小于（<）
         * lte(String key, Object value)：小于等于（<=）
         * gt(String key, Object value)：大于（>）
         * gte(String key, Object value)：大于等于（>=）
         */
        Query query = new Query(Criteria.where("name").is("test").and("age").is(20));
        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    /**
     * 模糊条件查询-Pattern.compile
     */
    @Test
    public void findLikeUserList(){
        //name like test
        String name = "est";
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        /*1、在使用Pattern.compile函数时，可以加入控制正则表达式的匹配行为的参数：
        Pattern.compile(String regex, int flag)
        2、regex设置匹配规则
        3、Pattern.CASE_INSENSITIVE,这个标志能让表达式忽略大小写进行匹配。*/
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        //创建一个query对象（用来封装所有条件对象)，再创建一个criteria对象（用来构建条件）
        Query query = new Query(//构建查询条件
                Criteria.where("name").regex(pattern));
        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    /**
     * 分页查询（带条件）-count\find\skip\limit
     */
    @Test
    public void pageLikeUserList(){
        int pageNo = 1;//设置当前页
        int pageSize = 3;//设置每页显示的记录数

        //条件构建
        String name = "est";
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        //1、在使用Pattern.compile函数时，可以加入控制正则表达式的匹配行为的参数：
        //Pattern Pattern.compile(String regex, int flag)
        //2、regex设置匹配规则
        //3、Pattern.CASE_INSENSITIVE,这个标志能让表达式忽略大小写进行匹配。
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        //创建一个query对象（用来封装所有条件对象)，再创建一个criteria对象（用来构建条件）
        Query query = new Query(//构建查询条件
                Criteria.where("name").regex(pattern));

        //分页构建
        //查询数来集合（表）中的总记录数
        long count = mongoTemplate.count(query, User.class);
        //设置条件当前页和每页大小，skip跳过，limit限制
        List<User> users = mongoTemplate.find(
                query.skip((pageNo - 1) * pageSize).limit(pageSize), User.class);
        System.out.println(count);
        System.out.println(users);
    }

    /**
     * 修改操作-upsert
     */
    @Test
    public void  updateUser(){
        //根据id查询
        User user = mongoTemplate.findById("6520c49db4920626509949f5", User.class);
        //修改值
        user.setName("test_02");
        user.setAge(2);
        user.setEmail("test_02@qq.com");

        //设置修改条件
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        //设置修改内容
        Update update = new Update();
        update.set("name",user.getName());
        update.set("age",user.getAge());
        update.set("email",user.getEmail());
        //调用mongoTemplate的修改方法实现修改
        UpdateResult upsert = mongoTemplate.upsert(query, update, User.class);
        long modifiedCount = upsert.getModifiedCount();//获取到修改受影响的行数
        System.out.println("受影响的条数："+modifiedCount);
    }


    /**
     * 删除条件-remove
     */
    @Test
    public void deleteUser(){
        Query query = new Query(Criteria.where("_id").is("6520c49db4920626509949f5"));
        DeleteResult remove = mongoTemplate.remove(query, User.class);
        long deletedCount = remove.getDeletedCount();
        System.out.println("删除的条数："+deletedCount);
    }

    /**
     * 批量添加操作百万-insertList
     */
    @Test
    public void insertList() {
        long start = System.currentTimeMillis();
        System.out.println("批量添加操作100万数据开始时间"+start);

        //不存在此集合则创建集合,无任何索引
        if (!mongoTemplate.collectionExists("users")){
            mongoTemplate.createCollection("users");
        }

        //使用@Document("User")定义User对象
        List<User> users = new LinkedList<>();
        for (int i = 0; i < 1000000; i++) {
            User user = new User();
            user.setAge(i);
            user.setName("test"+i);
            user.setEmail("123@qq.com"+i);
            users.add(user);
            System.out.println(user);
        }

        //导入Users文档，同时创建users文档
        int size = mongoTemplate.insert(users, "users").size();
        System.out.println(size);
        long end = System.currentTimeMillis();
        System.out.println("批量添加操作100万数据耗时"+(end-start));
    }

    /**
     * 批量添加操作千万-insertList
     */
    @Test
    void saveBatch() {
        long start = System.currentTimeMillis();
        int oneNum = 5000;
        List<User> insertDataList = new ArrayList(oneNum);
        int totalnum = 1000 * 10000;
        for (int i = 0; i < totalnum; i++) {
            User user = new User();
            user.setAge(i);
            user.setName("test" + i);
            user.setEmail("123@qq.com" + i);
            insertDataList.add(user);
            //当导入列表中个数大于等于oneNum时，执行批处理
            if (insertDataList.size() >= oneNum) {
                BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "users");
                operations.insert(insertDataList);
                operations.execute();
                insertDataList = new ArrayList(oneNum);
            }
            if (i != 0 && i % 1000000 == 0) {
                System.out.println("导入" + i + "条用户数据用时：" + (System.currentTimeMillis() - start) + "毫秒");
            }
        }
        if (!insertDataList.isEmpty()) {
            BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "users");
            operations.insert(insertDataList);
            operations.execute();
        }
        System.out.println("1000万用户用时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

    /**
     * 批量更新操作千万-updateList
     */
    @Test
    public void updateBatch() {
        long start = System.currentTimeMillis();
        System.out.println("批量更新操作100万数据开始时间"+start);

        List<User> userList = mongoTemplate.find(new Query().limit(1000000),User.class,"users");
        //List<User> userList = mongoTemplate.findAll(User.class,"users");
        System.out.println(userList.size());

        //todo 方案1 updateOne(Query query, Update update)
        //BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "users");
        //for (int i = 0; i < all.size(); i++) {
        //    Update update = new Update();
        //    update.set("name",all.get(i).getName()+1);
        //    update.set("age",all.get(i).getAge()+1);
        //    update.set("email",all.get(i).getEmail()+1);
        //    operations.updateOne(Query.query(Criteria.where("id").is(all.get(i).getId())), update);
        //}
        ////直接等待1000万到齐进行批处理，会卡顿出现java.lang.OutOfMemoryError: GC overhead limit exceeded
        //operations.execute();

        //todo 方案2 updateMulti(List<Pair<Query, Update>> updates)
        BulkOperations operations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, "users");
        List<Pair<Query, Update>> updateList = new ArrayList<>(userList.size());
        userList.forEach(f->{
            Query query = new Query(new
                    Criteria("id").is(f.getId()));
            Update update = new Update();
            update.set("name", f.getName()+"update");
            Pair<Query, Update> updatePair = Pair.of(query, update);
            updateList.add(updatePair);
        });
        operations.updateMulti(updateList);
        //500万卡顿出现java.lang.OutOfMemoryError: GC overhead limit exceeded,解决上内存分批100万级批量处理
        System.out.println("更新100万用户用时：" + (System.currentTimeMillis() - start) + "毫秒");
    }

    /**
     * 创建集合并设置索引
     * 背景：一般建立索引是用在搜索上，在存入大量数据后，再去建立索引。或者在集合初始化时建立索引。
     */
    @Test
    public void CreateCollectionIndex(){
        if (mongoTemplate.collectionExists("usersIndex")){
            return;
        }
        //索引集合
        List<IndexModel> indexModels=new ArrayList<IndexModel>();
        //一个index可以为1个字段，也可以是多字段组合
        BasicDBObject index1=new BasicDBObject();
        index1.put("name",1);
        indexModels.add(new IndexModel(index1));
        //组合索引
        BasicDBObject index2=new BasicDBObject();
        index2.put("name",1);
        index2.put("age",1);
        indexModels.add(new IndexModel(index2));
        //创建
        List<String> stringList = mongoTemplate.createCollection("usersIndex").createIndexes(indexModels);
        System.out.println("建立集合的索引列表"+stringList);
    }

}
