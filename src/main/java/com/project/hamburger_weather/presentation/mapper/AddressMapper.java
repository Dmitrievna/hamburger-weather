package com.project.hamburger_weather.presentation.mapper;

import org.springframework.stereotype.Component;
import com.project.hamburger_weather.domain.model.Address;
import com.project.hamburger_weather.presentation.dto.AddressDto;

@Component
public class AddressMapper {

    public Address toDomain(AddressDto a) {
        return new Address(
                a.street(), a.num(), a.city(), a.plz(), a.country()
        );
    }

}
