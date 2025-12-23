package com.example.week8.unittest

import com.example.repository.WeatherRepository
import com.example.room.WeatherEntity
import com.example.state.WeatherUiState
import com.example.viewmodel.WeatherViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    private lateinit var viewModel: WeatherViewModel
    private lateinit var repository: WeatherRepository
    private val dispatcher = StandardTestDispatcher()

    private val fakeWeather = listOf(
        WeatherEntity(1, "London", 15.0, 70, "Clouds"),
        WeatherEntity(2, "New York", 20.0, 65, "Rain")
    )

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = mockk(relaxed = true)
        viewModel = WeatherViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Loading and no selection`() {
        val s = viewModel.state
        assertEquals(WeatherUiState.Loading, s.uiState)
        assertTrue(s.weather.isNullOrEmpty())
        assertNull(s.selectedWeather)
        assertNull(s.error)
    }

    @Test
    fun `refreshHomePage sets uiState to Success when repository succeeds`() = runTest {
        coEvery { repository.getWeatherList() } returns fakeWeather

        viewModel.refreshHomePage()
        dispatcher.scheduler.advanceUntilIdle()

        val s = viewModel.state
        val ui = s.uiState as WeatherUiState.Success
        assertEquals(fakeWeather, ui.data)
        assertEquals(fakeWeather, s.weather)
        assertNull(s.error)
    }

    @Test
    fun `refreshHomePage sets uiState to Error when repository fails`() = runTest {
        coEvery { repository.getWeatherList() } throws IOException("Network error")

        viewModel.refreshHomePage()
        dispatcher.scheduler.advanceUntilIdle()

        val s = viewModel.state
        val ui = s.uiState as WeatherUiState.Error
        assertEquals("Network error", ui.exception.message)
        assertEquals("Network error", s.error?.message)
    }

    @Test
    fun `refreshHomePage handles empty weather list`() = runTest {
        coEvery { repository.getWeatherList() } returns emptyList()

        viewModel.refreshHomePage()
        dispatcher.scheduler.advanceUntilIdle()

        val ui = viewModel.state.uiState as WeatherUiState.Success
        assertTrue(ui.data.isEmpty())
    }

    @Test
    fun `selectWeather updates selectedWeather`() {
        val item = fakeWeather.first()
        viewModel.selectWeather(item)
        assertEquals(item, viewModel.state.selectedWeather)
    }

    @Test
    fun `retry triggers refreshHomePage and updates to Success`() = runTest {
        coEvery { repository.getWeatherList() } returns fakeWeather

        viewModel.retry()
        dispatcher.scheduler.advanceUntilIdle()

        val ui = viewModel.state.uiState as WeatherUiState.Success
        assertEquals(fakeWeather, ui.data)
    }

    @Test
    fun `refreshDetailsPage does nothing when no weather selected`() = runTest {
        viewModel.refreshDetailsPage()
        dispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.state.selectedWeather)
    }

    @Test
    fun `refreshDetailsPage updates selectedWeather when repository returns data`() = runTest {
        val initial = WeatherEntity(1, "Berlin-old", 9.0, 75, "Clouds")
        viewModel.selectWeather(initial)

        val updated = WeatherEntity(1, "Berlin", 10.0, 80, "Snow")
        coEvery { repository.getWeatherById(1) } returns updated

        viewModel.refreshDetailsPage()
        dispatcher.scheduler.advanceUntilIdle()

        assertEquals(updated, viewModel.state.selectedWeather)
    }
}
