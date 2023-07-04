package com.increff.invoiceapp.spring;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@Configuration
@ComponentScan("com.increff.invoiceapp")
@PropertySources({ //
        @PropertySource(value = "file:./invoiceapp.properties", ignoreResourceNotFound = true) //
})
public class SpringConfig {
}