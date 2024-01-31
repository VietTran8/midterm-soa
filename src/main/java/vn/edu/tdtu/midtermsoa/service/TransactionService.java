package vn.edu.tdtu.midtermsoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.tdtu.midtermsoa.model.Transaction;
import vn.edu.tdtu.midtermsoa.model.User;
import vn.edu.tdtu.midtermsoa.repository.TransactionRepository;
import vn.edu.tdtu.midtermsoa.repository.UserRepository;

import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public boolean beginTransaction(String username){
        Optional<User> foundUser = userRepository.findByUsername(username);
        if(foundUser.isPresent()){
            User user = foundUser.get();
            if(user.isInTransaction()){
                return false;
            }
            user.setInTransaction(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean closeTransaction(String username){
        Optional<User> foundUser = userRepository.findByUsername(username);
        if(foundUser.isPresent()){
            User user = foundUser.get();
            if(!user.isInTransaction()){
                return false;
            }
            user.setInTransaction(false);
            Logger.getLogger("UserTransaction - [Close]").info(String.valueOf(user.isInTransaction()));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public Transaction saveTransaction(Transaction transaction){
        return transactionRepository.save(transaction);
    }
    public Transaction getTransactionById(long id){
        return transactionRepository.findById(id).orElse(null);
    }
}
