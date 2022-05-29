package async;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicStampedReference;

class AtomicStampedReferenceTest {

    @Test
    void name6() {
        String initialRef = "initial value referenced";
        int initialStamp = 0;

        AtomicStampedReference<String> atomicStringReference =
                new AtomicStampedReference<String>(
                        initialRef, initialStamp
                );

        String newRef = "new value referenced";
        int newStamp = initialStamp + 1;

        boolean exchanged = atomicStringReference
                .compareAndSet(
                        initialRef, newRef,
                        initialStamp, newStamp);
        System.out.println("exchanged1: " + exchanged); //true

        exchanged = atomicStringReference
                .compareAndSet(
                        initialRef, "new string",
                        newStamp, newStamp + 1);
        System.out.println("exchanged2: " + exchanged); //false

        exchanged = atomicStringReference
                .compareAndSet(
                        newRef, "new string",
                        initialStamp, newStamp + 1);
        System.out.println("exchanged3: " + exchanged); //false

        exchanged = atomicStringReference
                .compareAndSet(
                        newRef, "new string",
                        newStamp, newStamp + 1);
        System.out.println("exchanged4: " + exchanged); //true
    }
}