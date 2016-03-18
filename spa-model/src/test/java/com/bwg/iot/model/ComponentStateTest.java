package com.bwg.iot.model;

import com.bwg.iot.model.Component.ComponentType;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.Is.is;

public class ComponentStateTest {

    private ComponentState componentState;

    @Before
    public void setUp() {
        componentState = new ComponentState();
    }

    @Test
    public void itDeterminesPortness() throws Exception {
        componentState.setComponentType(ComponentType.GATEWAY.name());
        assertThat(componentState.requiresPort(), is(equalTo(false)));

        componentState.setComponentType(ComponentType.PUMP.name());
        assertThat(componentState.requiresPort(), is(equalTo(true)));
    }

}
