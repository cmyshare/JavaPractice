package 精灵云;

import java.util.HashMap;

/**
 * 设计缓存 Redis LRU
 * 运用所掌握的数据结构，设计和实现一个 LRU (Least Recently Used，最近最少使用) 缓存机制 。
 * @author cmy
 * @version 1.0
 * @date 2022/12/19 22:18
 * @description 哈希表+双向链表。
 *
 * 每次新增节点时，往链表头部放入。需要删除时，删除链表尾节点。
 *
 * 每次插入新的数据放在链表头部，同样，如果某个数据被访问，那么也将其移到链表头部，
 * 这样链表尾节点就是最久未使用的，需要删除时直接将链表尾节点删除即可
 *
 * get()和put()的时间复杂度均是O(1)。空间复杂度是O(n)，其中n为缓存的键数。
 *
 * 执行用时：141ms，击败79.91%。消耗内存：55.7MB，击败96.89%。
 *
 * 参考：
 * https://blog.csdn.net/Ego12138/article/details/120356926
 * https://blog.csdn.net/shangsongwww/article/details/102609619
 */
public class RedisLRU {
    /**
     * 内部类
     */
    class LRUCache1 {
        /**
         * 定义双向链表，结点
         */
        private class Node {
            private int key;
            private int value;
            //前缀指针
            private Node pre;
            //后缀指针
            private Node next;

            public Node() {
            }

            public Node(int key, int value) {
                this.key = key;
                this.value = value;
            }
        }

        /**
         * 虚拟头节点
         */
        private Node dummyHead = new Node();
        /**
         * 虚拟尾节点
         */
        private Node dummyTail = new Node();
        /**
         * 缓存容量
         */
        private int capacity;
        /**
         * 双向链表长度
         */
        private int size;
        /**
         * 定义缓存结点键值对
         */
        private HashMap<Integer, Node> hashMap = new HashMap<>();

        /**
         * 双向链表操作：将节点添加到虚拟头节点之后
         * @param node
         */
        private void add(Node node) {
            //获取原来第一个结点
            Node originHead = dummyHead.next;
            //虚拟头节点next指向新增结点
            dummyHead.next = node;
            //新增结点pre指向虚拟头节点
            node.pre = dummyHead;
            //新增结点next指向原来的第一个结点
            node.next = originHead;
            //原来的第一个结点的pre指向新增结点
            originHead.pre = node;
        }

        /**
         * 双向链表操作：删除某个节点
         * @param node
         */
        private void del(Node node) {
            //获取删除结点的pre结点
            Node preNode = node.pre;
            //获取删除结点的next结点
            Node nextNode = node.next;
            //删除结点的pre结点的next指向删除结点的next结点
            preNode.next = nextNode;
            //删除结点的next结点的pre指向删除结点的pre结点
            nextNode.pre = preNode;
            //当前结点指向null
            node.pre = null;
            node.next = null;
        }

        /**
         * 初始化LRU缓存
         * @param capacity
         */
        public LRUCache1(int capacity) {
            //虚拟头节点后缀指针指向虚拟尾节点
            dummyHead.next = dummyTail;
            //虚拟尾节点前缀缀指针指向虚拟头节点
            dummyTail.pre = dummyHead;
            //缓存容量
            this.capacity = capacity;
            //双向链表长度为0
            size = 0;
        }

        /**
         * 获取值get方法
         *
         * get操作，从哈希表中取指定key的数据，如果不存在结点，直接返回-1，如果存在，则将其移到后面，最后返回该值。
         * @param key
         * @return
         */
        public int get(int key) {
            //获取缓存hashMap的结点
            Node node = hashMap.get(key);
            if (null == node) {
                return -1;
            }
            //删除访问结点
            del(node);
            //将访问结点添加到虚拟头节点之后
            add(node);
            return node.value;
        }

        /**
         * 添加值put方法
         *
         * put操作，先判断哈希表中是否已经有该key的数据了，如果有，直接给他赋新值并移到最后；
         * 如果不存在，说明要插入新的了，插入前先判断容器是否已经满了，如果没满，则直接插入到末尾，
         * 如果满了，则将头节点（最久未使用）移除，然后将新节点插入末尾。
         * @param key
         * @param value
         */
        public void put(int key, int value) {
            Node node = hashMap.get(key);
            //先判断哈希表中是否已经有该key的数据了，如果有，直接给他赋新值并移到最后；
            if (null != node) {
                node.value = value;
                //删除访问结点
                del(node);
                //将访问结点添加到虚拟头节点之后
                add(node);
            } else {
                //如果链表长度大于缓存容量就删除不常用链表尾节点
                if (size < capacity) {
                    size++;
                } else {
                    //删除链表尾节点
                    Node delNode = dummyTail.pre;
                    hashMap.remove(delNode.key);
                    del(delNode);
                }
                //将新结点添加到虚拟头节点之后
                Node newNode = new Node(key, value);
                add(newNode);
                hashMap.put(key, newNode);
            }
        }
    }
}
