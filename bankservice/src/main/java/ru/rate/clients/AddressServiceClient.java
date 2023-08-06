package ru.rate.clients;


import ru.rate.model.Address;

import java.math.BigDecimal;
import java.util.List;

public interface AddressServiceClient {

    List<String> getNearestAddresses(Address currentAddress, List<Address> addresses);
    Address getCoordinatesAddresses(BigDecimal lat, BigDecimal lon);
}
