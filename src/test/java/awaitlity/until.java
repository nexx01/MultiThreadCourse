package awaitlity;

import org.awaitility.Awaitility;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class until {

    AddUserMessage awailableRock = new AddUserMessage("Awailable Rock");

    @Test
    void defaultTimeout() {
        Callable<Boolean> newUserAdded =
                () -> new Repository().publish(awailableRock);

        //by default 10 sec
        await().until(newUserAdded);
    }

    @Test
    void customTimeout() {

        Callable<Boolean> newUserAdded =
                () -> new Repository().publish(awailableRock);


        assertThrows(ConditionTimeoutException.class,
                ()->await().atMost(2, TimeUnit.SECONDS).until(newUserAdded));
    }

    @Test
    void reusingCondition() throws InterruptedException {
        var repository = new Repository();
        repository.publish(new AddUserMessage("Some message"));

        Callable<Integer> userRepositorySize = () -> repository.size();
        await().until(userRepositorySize, equalTo(1));

        repository.publish(new AddUserMessage("Some message2"));
        repository.publish(new AddUserMessage("Some message3"));

        await().until(userRepositorySize, equalTo(3));
    }





}

class Repository {

    List<String> messages = new ArrayList<>();

    public Boolean publish(AddUserMessage message) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return messages.add(message.getMessage());
    }

    public Integer size() {
        return messages.size();
    }
}


class AddUserMessage{

    String message;

    public AddUserMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

