package 测试;//package 测试;
//
///**
// * @author cmy
// * @version 1.0
// * @date 2022/10/19 18:36
// * @description
// */
//public class baoTest {
//    public static void main(String[] args) {
//        Thread threadA = new Thread(new A());
//        Thread threadB = new Thread(new B());
//        threadA.start();
//        threadB.start();
//    }
//}
//
//class A implements Runnable {
//    @Override
//    public void run() {
//        FooBar fooBar = new FooBar();
//        for (int i = 0; i <100;i++){
//            if(i % 2==1){
//                try {
//                    fooBar.foo();
//                    fooBar.bar();
//                    fooBar.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            fooBar.notify();
//        }
//    }
//}
//
//class B implements Runnable {
//    @Override
//    public void run() {
//        FooBar fooBar = new FooBar();
//        for (int i = 0; i <100;i++){
//            if(i % 2==2){
//                try {
//                    fooBar.foo();
//                    fooBar.bar();
//                    fooBar.foo();
//                    fooBar.bar();
//                    fooBar.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            fooBar.notify();
//        }
//    }
//}
//
