package com.open.mysqldoc;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.EmptyStackException;
import java.util.Queue;

/**
 * @author cmy
 * @version 1.0
 * @date 2022/7/5 0005 16:05
 * @description 队列和栈
 */
public class QueueStack {

    /**
     * 队列练习：先进先出
     *
     * LinkedList底层用的双向链表，下标操作线性时间，头尾删除常数时间，没有实现同步synchronized。
     * LinkedList同时实现了List接口和Deque接口，即可做队列，也可做栈。
     * 关于栈或队列，首选是ArrayDeque，底层通过循环数组实现，比LinkedList(当作栈或队列使用时)更好性能
     * https://blog.csdn.net/tyh1579152915/article/details/118529597
     */
    public static void QueueTest(){
        //add()和remove()方法在失败的时候会抛出异常(不推荐)
        Queue<String> queue = new ArrayDeque<String>();
        //入队：添加元素
        queue.offer("a");
        queue.offer("b");
        queue.offer("c");
        queue.offer("d");
        queue.offer("e");

        //循环输出_出队
        for(String q : queue){
            System.out.println(q);
        }

        System.out.println("===");

        //出队：返回并删除此队列的头部，如果此队列为空，则返回 null
        System.out.println("poll="+queue.poll());
        for(String q : queue){
            System.out.println(q);
        }

        System.out.println("===");

        //返回不删除此队列的头部，为空不抛异常
        System.out.println("element="+queue.element());
        for(String q : queue){
            System.out.println(q);
        }

        System.out.println("===");

        //返回不删除此队列的头部，为空抛异常
        System.out.println("peek="+queue.peek());
        for(String q : queue){
            System.out.println(q);
        }
    }


    /**
     * 入栈
     * @param st
     * @param a
     */
    static void showpush(Deque<Integer> st, int a) {
        st.push(new Integer(a));
        System.out.println("push(" + a + ")");
        System.out.println("stack: " + st);
    }

    /**
     * 出栈
     * @param st
     */
    static void showpop(Deque<Integer> st) {
        System.out.print("pop -> ");
        Integer a = (Integer) st.pop();
        System.out.println(a);
        System.out.println("stack: " + st);
    }

    /**
     * 栈练习：后进先出
     *
     * 栈的重点应该在于 栈先进后出的思想，以及对 入栈(push)，出栈(pop) 两种操作的运用。
     * 解决相关括号匹配，迷宫求解，表达式求值等问题，都是一个不错的选择。
     * https://blog.csdn.net/qq_42124842/article/details/91420306
     */

    public static void StackTest(){
        ////效率低、线程安全
        //Stack<Integer> st = new Stack<Integer>();
        //效率高、线程不安全
        Deque<Integer> st=new ArrayDeque<Integer>();

        System.out.println("stack: " + st);
        showpush(st, 42);
        showpush(st, 66);
        showpush(st, 99);
        showpop(st);
        showpop(st);
        showpop(st);

        //stack为空抛异常
        try {
            showpop(st);
        } catch (EmptyStackException e) {
            System.out.println("empty stack");
        }
    }

    public static void main(String[] args) {
        //队列
        //QueueTest();
        //栈
        StackTest();
    }
}
