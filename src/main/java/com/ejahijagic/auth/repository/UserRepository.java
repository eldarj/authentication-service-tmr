
package com.ejahijagic.auth.repository;

import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import static com.ejahijagic.auth.jooq.tables.Users.USERS;
import java.time.Instant;

@Repository
public class UserRepository {
    private final DSLContext dsl;

    public UserRepository(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void saveOrUpdateLogin(String phone) {
        if (dsl.fetchExists(dsl.selectFrom(USERS).where(USERS.PHONE_NUMBER.eq(phone)))) {
            dsl.update(USERS).set(USERS.LAST_LOGIN, Instant.now()).where(USERS.PHONE_NUMBER.eq(phone)).execute();
        } else {
            dsl.insertInto(USERS).set(USERS.PHONE_NUMBER, phone).set(USERS.LAST_LOGIN, Instant.now()).execute();
        }
    }
}
