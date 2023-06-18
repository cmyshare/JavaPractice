package 精灵云;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 设计缓存 Redis LRU + 过期时间
 * 面试题：我们知道文件存储可以存储一些数据，我们现在想要利用文件存储的方法，来构建一类类似于redis的持久化存储类。
 * 它可以存储不同类型的对象，并且可以设置过期时间，当过期时间到达时，对象会被自动删除或不可访问。
 * 注意，这里的存储对象期望可以是尽可能支持广泛类型的对象，而不仅仅是特定的类型的对象。
 * 请实现以下的DataSave类的save和load方法以实现我们的目标，并保证unitest方法中的测试通过。（可以添加其他的辅助方法及类）
 * <p>
 * 提示：实现以下问题的方法很多，并没有唯一答案，请尽可能提供简洁的实现。我们重点关注代码的可读性和可维护性及思路。
 * <p>
 * 提交格式：请提供实现的代码，并且提供运行结果的截图。
 *
 * 参考链接：
 * https://blog.csdn.net/u010675669/article/details/86503464
 * https://juejin.cn/post/6984317823849857061
 * https://www.cnblogs.com/javaguide/p/12751779.html
 * https://blog.csdn.net/SDDDLLL/article/details/106113970
 */
public class RedisLRUTimeOut {

    //这个Node对用HashMap中每一个节点
    class Node implements Comparable<Node> {
        private final String key;
        private final Object value;
        private final long expireTime;

        public Node(String key, Object value, long expireTime) {
            this.value = value;
            this.key = key;
            this.expireTime = expireTime;
        }

        //按照过期时间进行排序,过期时间最小的数据排在队列前
        @Override
        public int compareTo(Node o) {
            long r = this.expireTime - o.expireTime;
            if (r > 0) return 1;
            if (r < 0) return -1;
            return 0;
        }
    }

    /**
     * redis LRU实现
     */
    // 用于设置清除过期数据的线程池
    private static final ScheduledExecutorService swapExpiredPool = new ScheduledThreadPoolExecutor(10);

    // 用户存储数据,为了保证线程安全，使用了ConcurrentHashMap
    private final ConcurrentHashMap<String, Node> cache = new ConcurrentHashMap<>(1024);

    // 保存最新的过期数据，过期时间最小的数据排在队列前
    // 推荐：ConcurrentLinkedQueue是一个基于单向链表的无界无锁线程安全的队列，适合在高并发环境下使用，效率比较高。
    private final PriorityQueue<Node> expireQueue = new PriorityQueue<>(1024);

    // 构造方法：只要有缓存了，过期清除线程就开始工作
    public RedisLRUTimeOut() {
        swapExpiredPool.scheduleWithFixedDelay(new ExpiredNode(), 1, 1, TimeUnit.SECONDS);
    }

    /**
     * 过期清除线程方法，当做一个内部类在LRU中。
     */
    class ExpiredNode implements Runnable {
        // 此过程为while(true)，一直进行判断和删除操作
        @Override
        public void run() {
            // 第一步：获取当前的时间
            long now = System.currentTimeMillis();
            while (true) {
                // 第二步：从过期队列弹出队首元素，如果不存在或者不过期就返回
                Node node = expireQueue.peek();
                if (node == null || node.expireTime > now) return;
                // 第三步：过期了那就从缓存中删除，并且还要从队列弹出
                cache.remove(node.key);
                //检索并删除此队列的头部，如果此队列为空，则返回null。
                expireQueue.poll();
            }
        }
    }


    //请实现持久化存储函数（使用文件存储相关方法）

    /**
     * @param key    存储的key
     * @param s      存储的对象
     * @param expire 过期时间，单位秒，如果为0则表示永不过期
     */
    void save(String key, Object s, int expire) {
        /**
         * 你的代码
         */
        if(expire==0){
            /**
             * 如果为0则表示永不过期，给定 2<sup>31</sup>-1 秒
             */
            expire=Integer.MAX_VALUE;
        }
        // 第一步：获取过期时间点
        long expireTime = System.currentTimeMillis() + expire;
        // 第二步：新建一个节点
        Node newNode = new Node(key, s, expireTime);
        // 第三步：cache中有的话就覆盖，没有就添加新的返回null，过期时间队列也要添加
        Node old = cache.put(key, newNode);
        expireQueue.add(newNode);
        // 第四步：如果该key存在数据，还要从过期时间队列删除
        if (old != null) {
            expireQueue.remove(old);
        }
    }

    //请实现持久化数据的取出

    /**
     * @param key 存储的key
     * @return 存储的对象
     */
    Object load(String key) {
        /**
         * 你的代码
         */
        //第一步：从cache直接获取，注意这个cache是一个HashMap
        Node node = cache.get(key);
        //第二步：如果n为空那就返回为null,不为空就返回相应的值
        return node == null ? null : node.value;
    }

    public static void main(String[] args) {
        unitest();
    }

    static void unitest() {
        School sc = new School("wuhan", "wuhan location");
        Clazz c = new Clazz("1", 30, 2, sc);
        Student s = new Student("zhangsan", 18, c);
        Student s0 = new Student("lisi", 22, c);

        System.out.println("***************存储和取出学生对象*****************");

        //存储和取出学生对象
        RedisLRUTimeOut sds = new RedisLRUTimeOut();
        sds.save("student", s, 0);
        Student s2 = (Student) (sds.load("student"));
        System.out.println("age:" + s2.age);
        System.out.println("grade:" + s2.clazz.grade);
        System.out.println("address:" + s2.clazz.school.address);

        System.out.println("***************存储和取出班级对象*****************");

        //存储和取出班级对象
        sds.save("clazz", c, 0);
        Clazz c2 = (Clazz) (sds.load("clazz"));
        System.out.println("grade:" + c2.grade);
        System.out.println("address:" + c2.school.address);

        System.out.println("***************存储和取出学校对象*****************");

        //存储和取出学校对象
        sds.save("school", sc, 0);
        School sc2 = (School) (sds.load("school"));
        System.out.println("address:" + sc2.address);

        System.out.println("***************存储和取出学生列表*****************");

        //存储和取出学生列表
        ArrayList<Student> students = new ArrayList<Student>();
        students.add(s);
        students.add(s0);
        sds.save("students", students, 0);
        ArrayList<Student> students2 = (ArrayList<Student>) (sds.load("students"));
        System.out.println("students size:" + students2.size());
        System.out.println("students1 age:" + students2.get(0).age);

        System.out.println("***************存储和取出学生对象，过期时间为10秒*****************");

        //存储和取出学生对象，过期时间为10秒
        sds.save("school_test", sc, 10);
        School sc3 = (School) (sds.load("school_test"));
        System.out.println("未过期时，school:" + sc3);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        School sc4 = (School) (sds.load("school_test"));
        System.out.println("已过期时，school:" + (sc4 == null));

    }
}


class Student {
    String name;
    int age;
    Clazz clazz;

    public Student(String name, int age, Clazz clazz) {
        this.name = name;
        this.age = age;
        this.clazz = clazz;
    }
}

class Clazz {
    String grade;
    int studentNumbers;
    int teacherNumbers;
    School school;

    public Clazz(String grade, int studentNumbers, int teacherNumbers, School school) {
        this.grade = grade;
        this.studentNumbers = studentNumbers;
        this.teacherNumbers = teacherNumbers;
        this.school = school;
    }
}

class School {
    String name;
    String address;

    public School(String name, String address) {
        this.name = name;
        this.address = address;
    }
}