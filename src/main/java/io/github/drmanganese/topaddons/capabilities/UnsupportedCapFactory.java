package io.github.drmanganese.topaddons.capabilities;

import java.util.concurrent.Callable;

public class UnsupportedCapFactory<T> implements Callable<T> {

    @Override
    public T call() {
        throw new UnsupportedOperationException();
    }
}
