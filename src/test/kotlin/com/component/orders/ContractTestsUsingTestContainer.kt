package com.component.orders

import com.github.dockerjava.api.model.ExposedPort
import com.github.dockerjava.api.model.PortBinding
import com.github.dockerjava.api.model.Ports
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.containers.BindMode
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@EnabledIf(value = "isNonCIOrLinux", disabledReason = "Run only on Linux in CI; all platforms allowed locally")
class ContractTestsUsingTestContainer {
    companion object {
        private const val APPLICATION_HOST = "localhost"
        private const val APPLICATION_PORT = 8080
        private const val HTTP_STUB_PORT = 9000

        @JvmStatic
        fun isNonCIOrLinux(): Boolean = System.getenv("CI") != "true" || System.getProperty("os.name").lowercase().contains("linux")

        @Container
        private val stubContainer: GenericContainer<*> =
            GenericContainer("specmatic/specmatic")
                .withCommand(
                    "virtualize",
                    "--examples=examples",
                    "--port=$HTTP_STUB_PORT",
                ).withCreateContainerCmdModifier { cmd ->
                    cmd.hostConfig?.withPortBindings(
                        PortBinding(Ports.Binding.bindPort(HTTP_STUB_PORT), ExposedPort(HTTP_STUB_PORT)),
                    )
                }.withExposedPorts(HTTP_STUB_PORT)
                .withFileSystemBind(
                    "./wsdls",
                    "/usr/src/app/wsdls",
                    BindMode.READ_ONLY,
                )
                .withFileSystemBind(
                    "./specmatic.yaml",
                    "/usr/src/app/specmatic.yaml",
                    BindMode.READ_ONLY,
                ).waitingFor(Wait.forHttp("/actuator/health").forStatusCode(200))
                .withLogConsumer { print(it.utf8String) }

        private val testContainer: GenericContainer<*> =
            GenericContainer("specmatic/specmatic")
                .withCommand(
                    "test",
                    "--host=$APPLICATION_HOST",
                    "--port=$APPLICATION_PORT",
                )
                .withFileSystemBind(
                    "./wsdls",
                    "/usr/src/app/wsdls",
                    BindMode.READ_ONLY,
                )
                .withFileSystemBind(
                    "./specmatic.yaml",
                    "/usr/src/app/specmatic.yaml",
                    BindMode.READ_ONLY,
                ).withFileSystemBind(
                    "./build/reports/specmatic",
                    "/usr/src/app/build/reports/specmatic",
                    BindMode.READ_WRITE,
                ).waitingFor(Wait.forLogMessage(".*Tests run:.*", 1))
                .withNetworkMode("host")
                .withLogConsumer { print(it.utf8String) }
    }

    @Test
    fun specmaticContractTest() {
        testContainer.start()
        val hasSucceeded = testContainer.logs.contains("Failures: 0")
        assertThat(hasSucceeded).isTrue()
    }
}
