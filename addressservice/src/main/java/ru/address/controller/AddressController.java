package ru.address.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.address.service.AddressService;
import ru.rate.model.Address;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "${rest.api}/${rest.version}")
public class AddressController {
    private final AddressService addressService;

    @PostMapping("/getNearestAddresses")
    public List<String> getNearestAddresses(@RequestBody List<Address> addressString) {
        var currentAddress = addressString.get(addressString.size() - 1);
        log.info("currentAddress:{}", currentAddress);
        addressString.remove(addressString.size() - 1);
        return addressService.getNearestAddresses(currentAddress, addressString);
    }

    @GetMapping("/getAddress/{coordinates}")
    public Address getAddressByCoordinate(@PathVariable("coordinates") String coordinates) {
        var data = coordinates.split("[+ ]");
        double lat = Double.parseDouble(data[0]);
        double lon = Double.parseDouble(data[1]);
        return addressService.getAddressByCoordinates(lon, lat);
    }
}