package ru.practicum.shareit.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private UserMapper mapper;

    @Test
    public void findById() {
        User user = new User(1L, "User", "user@user.com");
        UserDto userDto = mapper.toUserDto(user);

        UserServiceImpl userService = new UserServiceImpl(mockUserRepository, mapper);

        Mockito
                .when(mockUserRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user));
        UserDto userTest = userService.getById(1L);
        Assertions.assertEquals(user.getName(), userTest.getName());
    }





   /* @Test
    public void testFindPublicationYear() {
        BookService bookService = new BookService();
        bookService.setBookDao(mockBookDao);
        Mockito
                .when(mockBookDao.findPublicationDate(Mockito.anyInt()))
                .thenReturn("1894-12-07");

        int publicationYear = bookService.findPublicationYear(5);
        Assertions.assertEquals(1894, publicationYear);
    }*/

   /* @Autowired
    UsersService usersService;

    @MockBean
    UserRepository userRepositoryMock;
*/
   /* @Test
    public void getAllUsersTest(){
        List<User> usersFromMock=new ArrayList<>();
        Mockito.doReturn(usersFromMock).when(userRepositoryMock.findAll());

        List<User> users = usersService.getAllUsers();

        Assert.assertEquals(users,usersFromMock);
    }*/


}