//import org.junit.jupiter.api.RepeatedTest;
//import org.junit.jupiter.api.Test;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.atomic.AtomicReference;
//
////import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNull;
//
//public class SynhronizidCollections {
//
//
//    @RepeatedTest(1000)
//    void synchronizedListToArrayTest() throws InterruptedException {
//        List<String> list = Collections.synchronizedList(new ArrayList<>());
//        Runnable addOperation = () -> {
//            list.addAll(List.of("1", "2", "3", "4", "5", "5"));
//        };
//
////        Runnable delOperation = () -> {
////            for (int i = 0; i <10 ; i++) {
////                list.remove(0);
////            }
////        };
//     //   final AtomicReference<Exception> exception = new AtomicReference<Exception>();
//        Runnable copyOperation = () -> {
//            try {
//                for (int i = 0; i < 10; i++) {
//                    list.toArray(new String[list.size()]);
//                }
//            } catch (Exception ex) {
//             //   exception.set(ex);
//            }
//        };
//
//        Thread thread1 = new Thread(addOperation,"Thread_1");
//        Thread thread3 = new Thread(copyOperation,"Thread_3");
//
//        thread1.start();
//        thread3.start();
//
//        thread1.join();
//        thread3.join();
//
//       // assertNull(exception.get());
//        System.out.println(list);
//    }
//
//
//    @RepeatedTest(1000)
//    void name2() throws InterruptedException {
//
//        List<String> list = Collections.synchronizedList(new ArrayList<>(List.of("1", "2", "3", "4","5","5")));
//
//
//        Runnable addOperation = () -> {
//            list.remove(list.size());
//
//        };
//           final AtomicReference<Exception> exception = new AtomicReference<Exception>();
//
//        Runnable copyOperation = () -> {
//
//            try {
//                String[] values = list.toArray(new String[list.size()]);
//
//            } catch (Exception ex) {
//                  exception.set(ex);
//            }
//
//        };
//
//        Thread thread1 = new Thread(addOperation,"T1");
//        Thread thread2 = new Thread(copyOperation,"T2");
//
//        thread1.start();
//        thread2.start();
//
//        thread1.join();
//        thread2.join();
//
//        assertNull(exception.get());
//    }
//
//
//    public static void main(String[] args) throws InterruptedException {
//        long l = System.currentTimeMillis();
//        final int size = 50_000_000;
//        Object[] objects = new Object[size];
//        for (int i = 0; i < size; ++i) {
//
//            objects[i] = new Object();
//
//        }
//        System.out.println(System.currentTimeMillis()-l);
//    }
//}
