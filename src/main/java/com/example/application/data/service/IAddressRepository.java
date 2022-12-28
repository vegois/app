package com.example.application.data.service;

import com.example.application.data.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IAddressRepository
        extends
            JpaRepository<Address, Long>,
            JpaSpecificationExecutor<Address> {

}
