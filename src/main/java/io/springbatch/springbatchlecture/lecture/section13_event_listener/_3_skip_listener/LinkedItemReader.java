package io.springbatch.springbatchlecture.lecture.section13_event_listener._3_skip_listener;

import java.util.LinkedList;
import java.util.List;

import org.springframework.aop.support.AopUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import jakarta.annotation.Nullable;

public class LinkedItemReader<T> implements ItemReader<T> {

	private List<T> list;

	public LinkedItemReader(List<T> list) {
		if (AopUtils.isAopProxy(list)) {
			this.list = list;
		} else {
			this.list = new LinkedList<>(list);
		}
	}

	@Nullable
	@Override
	public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (!list.isEmpty()) {
			T remove = (T)list.remove(0);
			if ((Integer)remove == 3) {
				throw new CustomSkipException("read skipped : " + remove);
			}
			return remove;
		}
		return null;
	}
}
