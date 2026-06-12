package com.example.trading_app.service;
import com.example.trading_app.Entity.Order;
import com.example.trading_app.Entity.User;
import com.example.trading_app.Entity.Wallet;
import com.example.trading_app.domain.OrderType;
import com.example.trading_app.repository.WalletServiceRepository;
import jakarta.transaction.Transactional;
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
    @Transactional
    public Wallet getUserWallet(User user) {
        System.out.println("===== WALLET SERVICE METHOD EXECUTED =====");

        validateUser(user);

        Wallet wallet = walletServiceRepository.findByUser_Id(user.getId());

        if (wallet == null) {

            System.out.println("Wallet not found. Creating new wallet.");

            wallet = new Wallet();
            wallet.setUser(user);
            wallet.setBalance(BigDecimal.ZERO);

            wallet = walletServiceRepository.save(wallet);

            System.out.println("Wallet saved: " + wallet);

        } else if (wallet.getBalance() == null) {

            wallet.setBalance(BigDecimal.ZERO);
            wallet = walletServiceRepository.save(wallet);

        }

        return wallet;
    }


    @Override
    public Wallet addBalance(Wallet wallet, Long money) {
        BigDecimal balance = wallet.getBalance();
        BigDecimal newBalance = balance.add(BigDecimal.valueOf(money));
        wallet.setBalance(newBalance );
        return walletServiceRepository.save(wallet);
    }

    @Override
    public Wallet findWalletById(Long id) {
        Optional<Wallet>wallet = Optional.ofNullable(walletServiceRepository.findByUser_Id(id));
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
        if(order.getOrderType().equals(OrderType.BUY)){
            if(wallet.getBalance().compareTo(order.getPrice()) < 0){
                throw new RuntimeException("Insufficient funds for this transaction");
            }
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            wallet.setBalance(newBalance);
        }else{
            BigDecimal newBalance = wallet.getBalance().subtract(order.getPrice());
            wallet.setBalance(newBalance);
        }
        walletServiceRepository.save(wallet);
        return wallet;
    }
}
