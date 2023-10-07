//package com.open.mongodb;
//
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoCursor;
//import com.mongodb.client.MongoDatabase;
//import org.bson.Document;
//
///**
// * @version 1.0
// * @Author cmy
// * @Date 2023/6/20 18:13
// * @desc Mongo客户端连接与使用
// */
//public class MongoClientTest {
//
//    private static final String MONGO_HOST = "127.0.0.1";
//
//    private static final Integer MONGO_PORT = 27017;
//
//    private static final String MONGO_DB = "MongoDB";
//
//
//    /**
//     * 注意：
//     * mongo-java-driver和spring-boot-starter-data-mongodb版本冲突
//     * 错误描述：
//     * java.lang.NoSuchMethodError: com.mongodb.client.MongoCollection.insertOne(Ljava/lang/Object;)Lcom/mongodb/client/result/InsertOneResult;
//     * 错误原因：
//     * pom.xml 文件存在两个不同版本的依赖，版本低的可能无法正常操作MongoDB数据库
//     *
//     * @param args
//     */
//    public static void main(String args[]) {
//        try {
//            // 连接到 mongodb 服务
//            MongoClient mongoClient = new MongoClient(MONGO_HOST, MONGO_PORT);
//
//            // 连接到数据库
//            MongoDatabase mongoDatabase = mongoClient.getDatabase(MONGO_DB);
//            System.out.println("Connect to database successfully");
//
//            // 创建Collection
//            mongoDatabase.createCollection("test");
//            System.out.println("create collection");
//
//            // 获取collection
//            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
//
//            // 插入document
//            Document doc = new Document("name", "MongoDB")
//                    .append("type", "database")
//                    .append("count", 1)
//                    .append("info", new Document("x", 203).append("y", 102));
//            collection.insertOne(doc);
//
//            // 统计count
//            System.out.println(collection.countDocuments());
//
//            // query - first
//            Document myDoc = collection.find().first();
//            System.out.println(myDoc.toJson());
//
//            // query - loop all
//            MongoCursor<Document> cursor = collection.find().iterator();
//            try {
//                while (cursor.hasNext()) {
//                    System.out.println(cursor.next().toJson());
//                }
//            } finally {
//                cursor.close();
//            }
//
//        } catch (Exception e) {
//            System.err.println(e.getClass().getName() + ": " + e.getMessage());
//        }
//    }
//}
