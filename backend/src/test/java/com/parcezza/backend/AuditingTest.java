package com.parcezza.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AuditingTest {

    @Autowired
    private AuditTestRepository repo;

    @Test
    void createdAtIsSet() {
        AuditTestEntity e = new AuditTestEntity();
        e.setName("test");
        e = repo.saveAndFlush(e);

        assertThat(e.getCreatedAt()).isNotNull();
        assertThat(e.getUpdatedAt()).isNotNull();
    }
}
