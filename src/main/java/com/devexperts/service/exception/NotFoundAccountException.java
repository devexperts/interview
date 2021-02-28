package com.devexperts.service.exception;

import com.devexperts.account.Account;

public class NotFoundAccountException extends RuntimeException {
    public NotFoundAccountException(Account account) {
        super("Not found account " + account + " at AccountService");
    }

    public NotFoundAccountException(Long id) {
        super("Not found account with id = " + id + " at AccountService");
    }
}
