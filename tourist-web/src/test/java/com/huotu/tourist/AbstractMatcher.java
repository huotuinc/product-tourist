package com.huotu.tourist;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

/**
 * Created by Administrator on 2016/12/17.
 */
public abstract class AbstractMatcher<T> implements Matcher<T> {
    @Override
    public void describeMismatch(Object o, Description description) {

    }

    @Override
    public void describeTo(Description description) {

    }

    @Override
    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {

    }
}
