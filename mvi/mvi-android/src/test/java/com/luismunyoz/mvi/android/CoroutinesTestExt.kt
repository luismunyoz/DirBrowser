package com.luismunyoz.mvi.android

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestResult
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain

/**
 * Method to run tests. Because of differences in testing hot flows and cold flows/common suspending code, there is an
 * ability to specify [TestDispatcher]. [UnconfinedTestDispatcher] should be used for cold flows and common suspending code
 * because when using it child coroutines launched at the top level are entered eagerly. [StandardTestDispatcher] should be
 * used in case you're testing hot flow emissions and they are made inside [launch] block since coroutines go through a
 * dispatch until the first suspension and you can wait for the value in suspending manner.
 *
 * @param testDispatcher [TestDispatcher] to use for this test. [Dispatchers.Main] will be substituted with it.
 * @param test body of the test.
 *
 * @see <a href="https://github.com/Kotlin/kotlinx.coroutines/tree/master/kotlinx-coroutines-test">Test API reference</a>
 */
fun runTestWithDispatcher(
    testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
    test: suspend TestScope.() -> Unit,
): TestResult {
    Dispatchers.setMain(testDispatcher)

    return kotlinx.coroutines.test.runTest {
        test()
        Dispatchers.resetMain()
    }
}
