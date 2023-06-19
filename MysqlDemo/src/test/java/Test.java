
import com.open.mysql.dao.PersonMapper;
import com.open.mysql.model.Person;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.sql.*;

/**
 * Created by 14978 on 2017/6/25.
 */
public class Test {
    private long begin = 34212001;//起始id
    private long end = begin+100000;//每次循环插入的数据量
    private String url = "jdbc:mysql://localhost:3306/mysql_demo?useServerPrepStmts=false&rewriteBatchedStatements=true&useUnicode=true&amp;characterEncoding=UTF-8";
    private String user = "root";
    private String password = "123456";


    /**
     * 2.3 采用JDBC批处理（开启事务、无事务）
     */
    @org.junit.Test
    public void insertBigData() {
        //定义连接、statement对象
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            //加载jdbc驱动
            Class.forName("com.mysql.jdbc.Driver");
            //连接mysql
            conn = DriverManager.getConnection(url, user, password);
            //编写sql
            String sql = "INSERT INTO person VALUES (?,?,?,?,?,?,?)";
            //预编译sql
            pstm = conn.prepareStatement(sql);
            //开始总计时
            long bTime1 = System.currentTimeMillis();

            //循环10次，每次一万数据，一共10万
            for(int i=0;i<10;i++) {
                //将自动提交关闭
                conn.setAutoCommit(false);
                //开启分段计时，计1W数据耗时
                long bTime = System.currentTimeMillis();
                //开始循环
                while (begin < end) {
                    //赋值
                    pstm.setLong(1, begin);
                    pstm.setString(2, RandomValue.getChineseName());
                    pstm.setString(3, RandomValue.name_sex);
                    pstm.setInt(4, RandomValue.getNum(1, 100));
                    pstm.setString(5, RandomValue.getEmail(4, 15));
                    pstm.setString(6, RandomValue.getTel());
                    pstm.setString(7, RandomValue.getRoad());
                    //添加到同一个批处理中
                    pstm.addBatch();
                    begin++;
                }
                //执行批处理
                pstm.executeBatch();
                //提交事务
                conn.commit();
                //边界值自增10W
                end += 100000;
                //关闭分段计时
                long eTime = System.currentTimeMillis();
                //输出
                System.out.println("成功插入10W条数据耗时："+(eTime-bTime));
            }
            //关闭总计时
            long eTime1 = System.currentTimeMillis();
            //输出
            System.out.println("插入100W数据共耗时："+(eTime1-bTime1));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Mybatis 轻量级框架插入（无事务）
     *
     * Mybatis是一个轻量级框架，它比hibernate轻便、效率高。
     *
     * 但是处理大批量的数据插入操作时，需要过程中实现一个ORM的转换，本次测试存在实例，以及未开启事务，导致mybatis效率很一般。
     *
     * 这里实验内容是：
     *
     * 利用Spring框架生成mapper实例、创建人物实例对象
     * 循环更改该实例对象属性、并插入。
     */
    @org.junit.Test
    public void insertBigData2()
    {
        //加载Spring，以及得到PersonMapper实例对象。
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        PersonMapper pMapper = (PersonMapper) context.getBean("personMapper");
        //创建一个人实例
        Person person = new Person();
        //计开始时间
        long bTime = System.currentTimeMillis();
        //开始循环，循环次数1W次。
        while(begin<end){
            //为person赋值
            person.setId(begin);
            person.setName(RandomValue.getChineseName());
            person.setSex(RandomValue.name_sex);
            person.setAge(RandomValue.getNum(1, 100));
            person.setEmail(RandomValue.getEmail(4,15));
            person.setTel(RandomValue.getTel());
            person.setAddress(RandomValue.getRoad());
            //执行插入语句
            pMapper.insert(person);
            begin++;
        }
        //计结束时间
        long eTime = System.currentTimeMillis();
        System.out.println("插入1W条数据耗时："+(eTime-bTime));
    }


    /**
     * 2.2 采用JDBC直接处理（开启事务、关闭事务）
     * 采用JDBC直接处理的策略，这里的实验内容分为开启事务、未开启事务是两种，过程均如下：
     *
     * 利用PreparedStatment预编译
     * 循环，插入对应数据，并存入
     * 事务对于插入数据有多大的影响呢？ 看下面的实验结果:
     */
    @org.junit.Test
    public void insertBigData3() {
        //定义连接、statement对象
        Connection conn = null;
        PreparedStatement pstm = null;
        try {
            //加载jdbc驱动
            Class.forName("com.mysql.jdbc.Driver");
            //连接mysql
            conn = DriverManager.getConnection(url, user, password);
            //编写sql
            String sql = "INSERT INTO person VALUES (?,?,?,?,?,?,?)";
            //预编译sql
            pstm = conn.prepareStatement(sql);
            //开始总计时
            long bTime1 = System.currentTimeMillis();

            //循环10次，每次一万数据，一共10万
            for(int i=0;i<10;i++) {
//                //将自动提交关闭
//                conn.setAutoCommit(false);
                //开启分段计时，计1W数据耗时
                long bTime = System.currentTimeMillis();
                //开始循环
                while (begin < end) {
                    //赋值
                    pstm.setLong(1, begin);
                    pstm.setString(2, RandomValue.getChineseName());
                    pstm.setString(3, RandomValue.name_sex);
                    pstm.setInt(4, RandomValue.getNum(1, 100));
                    pstm.setString(5, RandomValue.getEmail(4, 15));
                    pstm.setString(6, RandomValue.getTel());
                    pstm.setString(7, RandomValue.getRoad());
                    //执行sql
                    pstm.execute();
                    begin++;
                }
//                //提交事务
//                conn.commit();
                //边界值自增10W
                end += 10000;
                //关闭分段计时
                long eTime = System.currentTimeMillis();
                //输出
                System.out.println("成功插入1W条数据耗时："+(eTime-bTime));
            }
            //关闭总计时
            long eTime1 = System.currentTimeMillis();
            //输出
            System.out.println("插入10W数据共耗时："+(eTime1-bTime1));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}

