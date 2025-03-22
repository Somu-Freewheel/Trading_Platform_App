package com.example.trading_app.service;

import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.Wallet;
import com.example.trading_app.repository.WalletServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class WalletServiceImplTest {
    @InjectMocks
    private WalletServiceImpl walletService;

    @Mock
    private WalletServiceRepository walletServiceRepository;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testWalletToWalletTransfer_Success() {
        // Given
        User sender = new User();
        sender.setId(1L);

        Wallet senderWallet = new Wallet();
        senderWallet.setId(1L);
        senderWallet.setUser(sender);
        senderWallet.setBalance(new BigDecimal("1000"));

        Wallet receiverWallet = new Wallet();
        receiverWallet.setId(2L);
        receiverWallet.setBalance(new BigDecimal("500"));

        Long amount = 200L;

        // Mock repository response for senderWallet
        when(walletServiceRepository.findByUserId(sender.getId())).thenReturn(senderWallet);
        when(walletServiceRepository.save(any(Wallet.class))).thenAnswer(i -> i.getArguments()[0]);

        // When
        Wallet updatedSenderWallet = walletService.walletToWalletTransfer(sender, receiverWallet, amount);

        // Then
        assertEquals(new BigDecimal("800"), updatedSenderWallet.getBalance());
        assertEquals(new BigDecimal("700"), receiverWallet.getBalance());

        verify(walletServiceRepository).save(senderWallet);
        verify(walletServiceRepository).save(receiverWallet);
    }

    @Test
    void testWalletToWalletTransfer_InsufficientBalance() {
        // Given
        User sender = new User();
        sender.setId(1L);

        Wallet senderWallet = new Wallet();
        senderWallet.setId(1L);
        senderWallet.setUser(sender);
        senderWallet.setBalance(new BigDecimal("100"));

        Wallet receiverWallet = new Wallet();
        receiverWallet.setId(2L);
        receiverWallet.setBalance(new BigDecimal("500"));

        Long amount =200L;

        // Mock repository response for senderWallet
        when(walletServiceRepository.findByUserId(sender.getId())).thenReturn(senderWallet);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            walletService.walletToWalletTransfer(sender, receiverWallet, amount);
        });

        assertEquals("Insufficient Balance", exception.getMessage());

        // Verify that save() was never called because of insufficient funds
        verify(walletServiceRepository, never()).save(any());
    }

}
