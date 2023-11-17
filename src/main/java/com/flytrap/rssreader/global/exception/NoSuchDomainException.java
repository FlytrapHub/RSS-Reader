package com.flytrap.rssreader.global.exception;

import com.flytrap.rssreader.global.model.DefaultDomain;
import java.util.NoSuchElementException;

public class NoSuchDomainException extends NoSuchElementException {

    public NoSuchDomainException(DefaultDomain domain) {
        super(String.format("No such domain = " + domain.getDomainCodeWithId()));
    }
}