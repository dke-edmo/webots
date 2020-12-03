package Webots;

import Utility.ByteArrayConverter;
import com.cyberbotics.webots.controller.Emitter;
import com.cyberbotics.webots.controller.Receiver;

import java.io.Serializable;

/**
 * Object communicator allows to exchange serializable Java objects between robots.
 *
 * Make sure that robots agree on the channels.
 *
 * @link https://www.cyberbotics.com/doc/reference/emitter?tab-language=java
 * @link https://www.cyberbotics.com/doc/reference/receiver?tab-language=java
 *
 * @author Tomasz Darmetko
 *
 * @param <E> The object that can be emitted.
 * @param <R> The object that can be received.
 */
public class ObjectCommunicator<E extends Serializable, R extends  Serializable> {

    private final Emitter emitter;
    private final Receiver receiver;

    public ObjectCommunicator(Emitter emitter, Receiver receiver, int timeStep) {
        this.emitter = emitter;
        this.receiver = receiver;
        receiver.enable(timeStep);
    }

    public void emit(E object) {
        emitter.send(ByteArrayConverter.toByteArray(object));
    }

    public boolean hasNext() {
        return receiver.getQueueLength() != 0;
    }

    public R receive() {
        if(receiver.getQueueLength() != 0) {
            byte[] byteArray = receiver.getData();
            //noinspection unchecked
            R object = (R)ByteArrayConverter.fromByteArray(byteArray);
            receiver.nextPacket();
            return object;
        } else {
            throw new RuntimeException(
                "Nothing to receive! Please, check with hasNext() before receiving."
            );
        }
    }

}
