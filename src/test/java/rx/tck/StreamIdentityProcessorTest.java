package rx.tck;

import org.junit.Test;
import org.reactivestreams.Processor;
import org.reactivestreams.Publisher;
import org.reactivestreams.tck.IdentityProcessorVerification;
import org.reactivestreams.tck.TestEnvironment;
import rx.Observable;
import rx.RxReactiveStreams;
import rx.functions.Action1;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class StreamIdentityProcessorTest extends IdentityProcessorVerification<Integer> {

    public StreamIdentityProcessorTest() {
        super(new TestEnvironment(2500, true), 3500);
    }

    @Override
    public Processor<Integer, Integer> createIdentityProcessor(int bufferSize) {
        // TODO
        return null;
    }

    @Override
    public Publisher<Integer> createHelperPublisher(long elements) {
        if (elements != Long.MAX_VALUE && elements > 0) {
            List<Integer> list = new ArrayList<Integer>();
            for (int i = 1; i <= elements; i++) {
                list.add(i);
            }

            return RxReactiveStreams.toPublisher(Observable.from(list));
        } else {
           return RxReactiveStreams.toPublisher(
               Observable
                   .interval(0, TimeUnit.SECONDS)
                   .map(new Func1<Long, Integer>() {
                       @Override
                       public Integer call(Long interval) {
                           return Math.abs(new Random().nextInt());
                       }
                   })
           );
        }
    }

    @Override
    public Publisher<Integer> createErrorStatePublisher() {
        return RxReactiveStreams.toPublisher(Observable.<Integer>error(new Exception("oops")));
    }

    @Test
    public void foo() {
        Observable
            .interval(1, TimeUnit.SECONDS)
            .map(new Func1<Long, Integer>() {
                @Override
                public Integer call(Long interval) {
                    return Math.abs(new Random().nextInt());
                }
            })
            .toBlocking().forEach(new Action1<Integer>() {
            @Override
            public void call(Integer aLong) {
                System.out.println(aLong);
            }
        });
    }

}
