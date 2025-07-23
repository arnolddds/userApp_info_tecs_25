package com.sobolev.userapp_infotecs_25
import com.sobolev.userapp_infotecs_25.domain.entities.Location
import com.sobolev.userapp_infotecs_25.domain.entities.Login
import com.sobolev.userapp_infotecs_25.domain.entities.Name
import com.sobolev.userapp_infotecs_25.domain.entities.Picture
import com.sobolev.userapp_infotecs_25.domain.entities.Registration
import com.sobolev.userapp_infotecs_25.domain.entities.Street
import com.sobolev.userapp_infotecs_25.domain.entities.User
import com.sobolev.userapp_infotecs_25.domain.usecases.GetUserUseCase
import com.sobolev.userapp_infotecs_25.presentation.ui.screens.details.UserDetailCommand
import com.sobolev.userapp_infotecs_25.presentation.ui.screens.details.UserDetailState
import com.sobolev.userapp_infotecs_25.presentation.ui.screens.details.UserDetailViewModel
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.net.UnknownHostException

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {

    private val getUserUseCase: GetUserUseCase = mockk()
    private lateinit var viewModel: UserDetailViewModel

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
    fun `user loaded successfully updates state to Checking`() = runTest {
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

        coEvery { getUserUseCase(1) } returns mockUser

        viewModel = UserDetailViewModel(
            userId = 1,
            getUserUseCase = getUserUseCase
        )


        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state is UserDetailState.Checking)
        assertEquals(mockUser, (state as UserDetailState.Checking).user)
    }

    @Test
    fun `network error updates state to Error with no internet message`() = runTest {
        coEvery { getUserUseCase(1) } throws UnknownHostException()

        viewModel = UserDetailViewModel(
            userId = 1,
            getUserUseCase = getUserUseCase
        )


        advanceUntilIdle()

        val state = viewModel.state.value
        assert(state is UserDetailState.Error)
        assertEquals("No internet", (state as UserDetailState.Error).message)
    }

    @Test
    fun `processCommand Back updates state to Finished`() = runTest {
        coEvery { getUserUseCase(1) } returns mockk()

        viewModel = UserDetailViewModel(
            userId = 1,
            getUserUseCase = getUserUseCase
        )

        advanceUntilIdle()

        viewModel.processCommand(UserDetailCommand.Back)

        val state = viewModel.state.value
        assert(state is UserDetailState.Finished)
    }
}
