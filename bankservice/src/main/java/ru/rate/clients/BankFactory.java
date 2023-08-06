package ru.rate.clients;


import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class BankFactory {

    private final ApplicationContext applicationContext;

    private BankFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Bank getBank(String codeBank) {
        if ("alfa".equals(codeBank)) {
            return applicationContext.getBean(AlfaBank.class);
        }
        if ("sber".equals(codeBank)) {
            return applicationContext.getBean(SberBank.class);
        }
        throw new IllegalArgumentException("unknown param:" + codeBank);
    }
}
