package com.sobolev.userapp_infotecs_25

import com.sobolev.userapp_infotecs_25.domain.entities.Location
import com.sobolev.userapp_infotecs_25.domain.entities.Login
import com.sobolev.userapp_infotecs_25.domain.entities.Name
import com.sobolev.userapp_infotecs_25.domain.entities.Picture
import com.sobolev.userapp_infotecs_25.domain.entities.Registration
import com.sobolev.userapp_infotecs_25.domain.entities.Street
import com.sobolev.userapp_infotecs_25.domain.entities.User
import com.sobolev.userapp_infotecs_25.domain.usecases.GetAllUsersUseCase
import com.sobolev.userapp_infotecs_25.domain.usecases.RefreshUsersUseCase
import com.sobolev.userapp_infotecs_25.presentation.ui.screens.users.UsersViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {

    private val getAllUsersUseCase: GetAllUsersUseCase = mockk()
    private val refreshUsersUseCase: RefreshUsersUseCase = mockk()
    private lateinit var viewModel: UsersViewModel

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `users loaded successfully updates screenState`() = runTest {
        val mockUser = User(
            id = 1,
            gender = "male",
            name = Name("Mr", "Arnold", "Sobolev"),
            location = Location(
                street = Street(123, "Lenina"),
                city = "Moscow",
                state = "Moscow",
                country = "Russia",
                postcode = "101000"
            ),
            email = "arnold@example.com",
            phone = "12345",
            cell = "54321",
            picture = Picture(
                large = "large_url",
                medium = "medium_url",
                thumbnail = "thumb_url"
            ),
            nat = "RU",
            dob = "1999-01-01",
            login = Login(username = "arnold_login"),
            registered = Registration(date = "2020-01-01", age = 5)
        )

        val flow = flowOf(listOf(mockUser))
        coEvery { getAllUsersUseCase() } returns flow

        viewModel = UsersViewModel(getAllUsersUseCase, refreshUsersUseCase)


        advanceUntilIdle()


        val state = viewModel.screenState.value
        assertFalse(state.isLoading)
        assertEquals(1, state.allUsers.size)
        assertEquals("Arnold Sobolev", state.allUsers.first().fullName)
        assertNull(state.error)
    }

    @Test
    fun `network error emits user-friendly error`() = runTest {

        coEvery { getAllUsersUseCase() } returns flow {
            throw UnknownHostException()
        }

        viewModel = UsersViewModel(getAllUsersUseCase, refreshUsersUseCase)


        advanceUntilIdle()


        val state = viewModel.screenState.value
        assertEquals("No internet connection", state.error)
        assertFalse(state.isLoading)
        assertTrue(state.allUsers.isEmpty())
    }

}
