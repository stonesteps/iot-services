package com.bwg.iot.job_scheduling.job;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * Given single item, it returns it just once in read method.
 */
public final class SingleItemReader<T> implements ItemReader<T> {

    private final T item;
    private boolean read = false;

    public SingleItemReader(final T item) {
        this.item = item;
    }

    @Override
    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        T tmp = null;
        if (!read) {
            read = true;
            tmp = item;
        }
        return tmp;
    }
}
