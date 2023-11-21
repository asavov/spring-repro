package com.example.alexdemo;

import com.example.alexdemo.AlexDemoApplicationTests.MockWebServerConfiguration;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true"
        // If this is uncommented and @Import is commented then it WORKs
        // , classes = {App.class, MockWebServerConfiguration.class}
)
@Import(MockWebServerConfiguration.class)
class AlexDemoApplicationTests {

    @SpringBootApplication
    static class App {

        static final String NAME = "webClient";

        @Bean(NAME)
        String originalWebClient() {
            System.out.println(">> WebClient: original");

            return "WebClient: original";
        }
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class MockWebServerConfiguration {

        @Bean
        String otherWebClient_IsCalled() {

            System.out.println(">> WebClient: other");

            return "WebClient: other";
        }

        // THIS IS NEVER CALLED !!!
        @Bean(App.NAME)
        String overrideWebClient_IsNotCalled() {

            System.out.println(">> WebClient: override");

            return "WebClient: override";
        }
    }

    @Test
    void test(@Qualifier(App.NAME) String webClient) {

        Assertions.assertThat(webClient).isEqualTo("WebClient: override");
    }
}