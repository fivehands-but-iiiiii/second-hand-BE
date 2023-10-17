package com.team5.secondhand;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Profile("test")
@Component
public class DataInitializer {

    private static final String OFF_FOREIGN_CONSTRAINTS = "SET foreign_key_checks = false";
    private static final String ON_FOREIGN_CONSTRAINTS = "SET foreign_key_checks = true";
    private static final String TRUNCATE_SQL_FORMAT = "TRUNCATE %s";

    private static final List<String> truncationDMLs = new ArrayList<>();

    @PersistenceContext
    private EntityManager em;

    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public void truncate(ExtensionContext context) {
        em.createNativeQuery(OFF_FOREIGN_CONSTRAINTS).executeUpdate();
        truncationDMLs.stream()
                .map(em::createNativeQuery)
                .forEach(Query::executeUpdate);
        em.createNativeQuery(ON_FOREIGN_CONSTRAINTS).executeUpdate();
    }

    @PostConstruct
    private void init() {
        List<String> tableNames = em.createNativeQuery("SHOW TABLES").getResultList();
        tableNames.forEach(tableName -> truncationDMLs.add(String.format(TRUNCATE_SQL_FORMAT, tableName)));
    }
}
