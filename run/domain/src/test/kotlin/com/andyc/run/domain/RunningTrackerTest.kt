@file:OptIn(ExperimentalCoroutinesApi::class)

package com.andyc.run.domain

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isBetween
import assertk.assertions.isEqualTo
import com.andyc.core.domain.location.Location
import com.andyc.core.domain.location.LocationWithAltitude
import com.andyc.core.test.LocationObserverFake
import com.andyc.core.test.MainCoroutineExtension
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.math.roundToInt

class RunningTrackerTest {

    companion object {
        @JvmField
        @RegisterExtension
        val mainCoroutineExtension = MainCoroutineExtension()
    }

    private lateinit var runningTracker: RunningTracker
    private lateinit var locationObserverFake: LocationObserverFake

    private lateinit var testDispatcher: TestDispatcher
    private lateinit var testScope: CoroutineScope

    @BeforeEach
    fun setUp() {
        locationObserverFake = LocationObserverFake()

        testDispatcher = mainCoroutineExtension.testDispatcher
        testScope = CoroutineScope(testDispatcher)

        runningTracker = RunningTracker(
            locationObserver = locationObserverFake,
            applicationScope = testScope
        )
    }

    @Test
    fun testCombiningRunData() = runTest {
        runningTracker.runData.test {
            skipItems(1)

            runningTracker.startObservingLocation()
            runningTracker.setIsTracking(true)

            val location1 = LocationWithAltitude(Location(1.0, 1.0), altitude = 1.0)
            locationObserverFake.trackLocation(location1)
            val emission1 = awaitItem()

            val location2 = LocationWithAltitude(Location(1.0455246, 1.067889675), altitude = 1.0112312)
            locationObserverFake.trackLocation(location2)
            val emission2 = awaitItem()

            testScope.cancel()

            assertThat(emission1.locations[0][0].location).isEqualTo(location1)
            assertThat(emission2.locations[0][1].location).isEqualTo(location2)

            val expectedDistance = 9088
            val tolerance = 0.03
            val lowerBound = expectedDistance * (1 - tolerance).roundToInt()
            val upperBound = expectedDistance * (1 + tolerance).roundToInt()
            assertThat(emission2.distanceMeters).isBetween(lowerBound, upperBound)

            assertThat(emission2.locations.first()).hasSize(2)
        }
    }
}