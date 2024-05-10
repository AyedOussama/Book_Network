package com.ayed.booknetwork.Email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {

    ACTIVATE_ACCOUNT("activate_account") // Email template name for account activation
    ;


    private final String name;
    EmailTemplateName(String name) {
        this.name = name;
    }
}
