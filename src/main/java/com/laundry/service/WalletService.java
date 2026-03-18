package com.laundry.service;

import com.laundry.entity.User;
import com.laundry.entity.Wallet;
import com.laundry.repository.WalletRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public Wallet getWallet(User user) {

        return walletRepository
                .findByUser(user)
                .orElseGet(() -> walletRepository.save(
                        Wallet.builder()
                                .user(user)
                                .balance(0.0)
                                .build()
                ));
    }

    public void addMoney(User user, Double amount) {

        Wallet wallet = getWallet(user);

        wallet.setBalance(wallet.getBalance() + amount);

        walletRepository.save(wallet);
    }
}