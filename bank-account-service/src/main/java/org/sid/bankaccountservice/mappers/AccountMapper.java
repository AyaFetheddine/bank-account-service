package org.sid.bankaccountservice.mappers;

import org.sid.bankaccountservice.dto.BankAccountRequestDTO;
import org.sid.bankaccountservice.dto.BankAccountResponseDTO;
import org.sid.bankaccountservice.entities.BankAccount;
import org.sid.bankaccountservice.entities.Customer;
import org.sid.bankaccountservice.repositories.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Component
public class AccountMapper {
    private CustomerRepository customerRepository;

    public AccountMapper(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public BankAccountResponseDTO fromBankAccount(BankAccount bankAccount) {
        BankAccountResponseDTO bankAccountResponseDTO = new BankAccountResponseDTO();
        BeanUtils.copyProperties(bankAccount, bankAccountResponseDTO);
        return bankAccountResponseDTO;
    }

    public BankAccount fromBankAccountRequestDTO(BankAccountRequestDTO bankAccountRequestDTO) {
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(bankAccountRequestDTO, bankAccount);
        bankAccount.setId(UUID.randomUUID().toString());
        bankAccount.setCreatedAt(new Date());

        // Handle customer assignment if customerId is provided
        if (bankAccountRequestDTO.getCustomerId() != null) {
            Customer customer = customerRepository.findById(bankAccountRequestDTO.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer with id " + bankAccountRequestDTO.getCustomerId() + " not found"));
            bankAccount.setCustomer(customer);
        }

        return bankAccount;
    }
}
