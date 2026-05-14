package com.parcezza.backend.repository;

import com.parcezza.backend.domain.Address;
import com.parcezza.backend.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);
}
