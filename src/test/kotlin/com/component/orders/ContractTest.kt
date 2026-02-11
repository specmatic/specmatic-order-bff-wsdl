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
        private const val WSDL_STUB_HOST = "localhost"
        private const val WSDL_STUB_PORT = 9000

        @JvmStatic
        @BeforeAll
        fun setUp() {
            wsdlStub = createStub(host = WSDL_STUB_HOST, port = WSDL_STUB_PORT)
        }

        @JvmStatic
        @AfterAll
        fun tearDown() {
            wsdlStub?.close()
        }
    }
}

