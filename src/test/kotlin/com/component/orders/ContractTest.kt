package com.component.orders


import io.specmatic.stub.ContractStub
import io.specmatic.stub.createStub
import io.specmatic.test.SpecmaticContractTest
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ContractTests : SpecmaticContractTest {
    companion object {
        private var wsdlStub: ContractStub? = null

        @JvmStatic
        @BeforeAll
        fun setUp() {
            wsdlStub = createStub()
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            wsdlStub?.close()
        }
    }
}

