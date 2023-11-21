package com.flytrap.rssreader.global.exception;

import com.flytrap.rssreader.global.model.DefaultDomain;
import java.util.NoSuchElementException;

public class NoSuchDomainException extends ApplicationException {

    public NoSuchDomainException(DefaultDomain domain) {
        super(domain.getDomainCodeWithId(), String.format("No such domain = " + domain.getDomainCodeWithId()));
    }
}
