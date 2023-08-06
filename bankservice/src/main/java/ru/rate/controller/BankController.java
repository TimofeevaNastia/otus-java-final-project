package ru.rate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.rate.model.*;
import ru.rate.service.AddressValidationException;
import ru.rate.service.BankService;

import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "${rest.api}/${rest.version}")
public class BankController {

    public final BankService bankService;

    @GetMapping("/currencyRate/{currency}")
    public BankRateResponse getCurrencyRate(@PathVariable("currency") String currency) {
        log.info("getCurrencyRate, currency:{}", currency);
        var rate = bankService.getCurrencyRate(currency);
        log.info("rate:{}", rate);
        return rate;
    }

    @GetMapping("/bank/{bankName}/{currency}")
    public BankRate getBankData(@PathVariable("bankName") String bankName, @PathVariable("currency") String currency) {
        log.info("getBankData, bankName:{} , currency:{}", bankName, currency);
        return bankService.getBankData(bankName, currency);
    }


    @PostMapping("/bank/addresses")
    public BankServiceResponse getOffices(@RequestBody BankAddressRequest request) {
        log.info("getOffices, request:{}", request);
        List<BankAddresses> bankAddresses = new ArrayList<>();
        try {
            var addresses = bankService.getNearestBankAddresses(request);
            addresses.forEach(x -> bankAddresses.add(new BankAddresses().setAddressString(x)));
            if (addresses.size() == 0) {
                var message = """
                        Не удалось найти отделения банка для указанного города.
                        Попробуйте снова или выберите действие в панели снизу.
                                """;
                return new BankServiceResponse().setError(new ErrorBody()
                        .setText(message));
            }
        } catch (AddressValidationException e) {
            var response = new BankServiceResponse().setError(new ErrorBody()
                    .setText(e.getMessage()).setDescription(Arrays.toString(e.getStackTrace())));
            e.printStackTrace();
            return response;
        } catch (Exception e) {
            var response = new BankServiceResponse().setError(new ErrorBody()
                    .setText("Произошла ошибка при анализе адреса.\nДолжен быть указан адрес в формате: город, улица, дом").setDescription(Arrays.toString(e.getStackTrace())));
            e.printStackTrace();
            return response;
        }
        var response = new BankServiceResponse().setData(new Data()
                .setBankServiceAddressesResponse(new BankAddressResponse()
                        .setAddresses(bankAddresses)
                        .setCodeBank(request.getCodeBank())
                        .setNameBank(request.getNameBank())));
        log.info("addresses:{}", response.getData().getBankServiceAddressesResponse());
        return response;
    }
}