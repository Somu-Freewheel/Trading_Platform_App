package com.example.trading_app.service;
import com.example.trading_app.Entity.Order;
import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.Wallet;
import com.example.trading_app.repository.WalletServiceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Optional;
import static com.example.trading_app.Utils.OtpUtils.validateUser;
@Slf4j
@Service
public class WalletServiceImpl implements WalletService{
    @Autowired
    private WalletServiceRepository walletServiceRepository;

    @Override
    public Wallet getUserWallet(User user) {
        validateUser(user);
        Wallet wallet = walletServiceRepository.findByUserId(user.getId());
        if(wallet==null){
            wallet = new Wallet();
            wallet.setUser(user);
        }
        return wallet;
    }


    @Override
    public Wallet addBalance(Wallet wallet, Long money) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));
        wallet.setBalance(newBalance );
        return wallet;
    }

    @Override
    public Wallet findWalletById(Long id) {
        Optional<Wallet>wallet = Optional.ofNullable(walletServiceRepository.findByUserId(id));
        if(wallet.isPresent()){
            return  wallet.get();
        }
        throw new RuntimeException("wallet not found");
    }

    @Override
    public Wallet walletToWalletTransfer(User sender, Wallet receiverWallet, Long amount) {
        validateUser(sender);
        Wallet senderWallet = getUserWallet(sender);
        /**
         * compareTo return :
         * -1  if sender's balance is less than amount.
         * 0 if equal.
         * 1 if greater.
         */
        if(senderWallet.getBalance().compareTo(BigDecimal.valueOf(amount))<0){
            throw new RuntimeException("Insufficient Balance");
        }
        BigDecimal senderBalance = senderWallet.getBalance().subtract(BigDecimal.valueOf(amount));
        senderWallet.setBalance(senderBalance);
        walletServiceRepository.save(senderWallet);
        BigDecimal recieverBalance = receiverWallet.getBalance()
                .add(BigDecimal.valueOf(amount));
        receiverWallet.setBalance(recieverBalance);
        walletServiceRepository.save(receiverWallet);
        log.info("Transferred {} from user {} to wallet {}", amount, sender.getId(), receiverWallet.getId());
        return senderWallet;
    }

    @Override
    public Wallet payOrderPayment(Order order, User user) {
        Wallet wallet = getUserWallet(user);

        return null;
    }
}
