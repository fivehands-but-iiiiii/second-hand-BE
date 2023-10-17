package com.team5.secondhand;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DataInitializerExtension implements AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext context) throws Exception {
        DataInitializer dataInitializer = SpringExtension.getApplicationContext(context).getBean(DataInitializer.class);
        dataInitializer.truncate(context);
    }
}
