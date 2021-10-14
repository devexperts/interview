package com.devexperts.service;

import com.devexperts.account.Account;
import com.devexperts.account.AccountKey;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class AccountServiceImpl implements AccountService {

    private final Map<AccountKey, Account> accounts = new ConcurrentHashMap<>();
    private final ReentrantLock lock = new ReentrantLock();

    @Override
    public void clear() {
        accounts.clear();
    }

    @Override
    public void createAccount(Account account) {
        if (account.getAccountKey() == null) {
            throw new NullPointerException();
        }
        if (accounts.containsKey(account.getAccountKey())) {
            throw new IllegalArgumentException("This key is already used.");
        }
        accounts.put(account.getAccountKey(), account);
    }

    @Override
    public Account getAccount(long id) {
        return accounts.getOrDefault(AccountKey.valueOf(id), null);
    }

    @Override
    public void transfer(Account source, Account target, double amount) {
        if (amount < 0 || source == target)
            throw new IllegalArgumentException("Amount can't be negative OR Source can't be same as target.");

        lock.lock();
        try {
            double sourceBalance = source.getBalance();
            double targetBalance = target.getBalance();

            if (sourceBalance < amount)
                throw new IllegalStateException("Source balance is insufficient.");

            source.setBalance(sourceBalance - amount);
            target.setBalance(targetBalance + amount);

        } finally {
            lock.unlock();
        }
    }
}
