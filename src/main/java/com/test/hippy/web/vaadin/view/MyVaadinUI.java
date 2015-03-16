package com.test.hippy.web.vaadin.view;

import org.vaadin.spring.annotation.VaadinUI;

import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

@VaadinUI
public class MyVaadinUI extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        setContent(new Label("Hello! I'm the root UI!"));
    }
}