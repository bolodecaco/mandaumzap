package com.server.demo.services;

import static org.instancio.Select.field;

import com.server.demo.dtos.RequestUserDTO;
import com.server.demo.dtos.UserDTO;
import com.server.demo.enums.PlanType;
import com.server.demo.mappers.UserMapper;
import com.server.demo.models.Plan;
import com.server.demo.models.User;
import com.server.demo.repositories.PlanRepository;
import com.server.demo.repositories.UserRepository;
import org.instancio.Instancio;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Criar usuário com dados válidos")
    void createUserWithValidData() {  
        Plan freePlan = Instancio.create(Plan.class);
        RequestUserDTO requestDTO = Instancio.create(RequestUserDTO.class);
        User user = Instancio.of(User.class)
            .set(field(User::getPlan), freePlan)
            .create();
        UserDTO responseDTO = Instancio.create(UserDTO.class);

        when(planRepository.findByType(PlanType.FREE)).thenReturn(Optional.of(freePlan));
        when(userMapper.toEntity(requestDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toDTO(user)).thenReturn(responseDTO);

        UserDTO data = userService.createUser(requestDTO);

        assertEquals(responseDTO, data);
        verify(planRepository).findByType(PlanType.FREE);
        verify(userMapper).toEntity(requestDTO);
        verify(userRepository).save(user);
        verify(userMapper).toDTO(user);
    }

    @Test
    @DisplayName("Buscar usuário por ID com ID válido")
    void getUserByIdWithValidId() {
        UUID id = UUID.randomUUID();
        User user = Instancio.create(User.class);
        UserDTO responseDTO = Instancio.create(UserDTO.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(responseDTO);

        UserDTO data = userService.getUserById(id);

        assertEquals(responseDTO, data);
        verify(userRepository).findById(id);
        verify(userMapper).toDTO(user);
    }

    @Test
    @DisplayName("Buscar usuário por ID com ID inválido deve falhar")
    void getUserByIdWithInvalidId() {
        UUID id = UUID.randomUUID();
        
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(id));
        verify(userRepository).findById(id);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Listar todos os usuários")
    void getAllUsers() {
        List<User> users = List.of(Instancio.create(User.class));
        List<UserDTO> responseDTOs = List.of(Instancio.create(UserDTO.class));

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTOList(users)).thenReturn(responseDTOs);

        List<UserDTO> result = userService.getAllUsers();

        assertEquals(responseDTOs, result);
        verify(userRepository).findAll();
        verify(userMapper).toDTOList(users);
    }

    @Test
    @DisplayName("Deletar usuário com ID válido")
    void deleteUserWithValidId() {
        UUID id = UUID.randomUUID();

        userService.deleteUser(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    @DisplayName("Atualizar usuário com dados válidos")
    void updateUserWithValidData() {
        UUID id = UUID.randomUUID();
        RequestUserDTO requestDTO = Instancio.create(RequestUserDTO.class);
        User existingUser = Instancio.create(User.class);
        User updatedUser = Instancio.create(User.class);
        UserDTO responseDTO = Instancio.create(UserDTO.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(updatedUser);
        when(userMapper.toDTO(updatedUser)).thenReturn(responseDTO);

        UserDTO result = userService.updateUser(id, requestDTO);

        assertEquals(responseDTO, result);
        verify(userRepository).findById(id);
        verify(userRepository).save(existingUser);
        verify(userMapper).toDTO(updatedUser);
    }

    @Test
    @DisplayName("Atualizar usuário com ID inválido deve falhar")
    void updateUserWithInvalidId() {
        UUID id = UUID.randomUUID();
        RequestUserDTO requestDTO = Instancio.create(RequestUserDTO.class);

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(id, requestDTO));
        verify(userRepository).findById(id);
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Buscar usuário por ID com ID válido")
    void findByIdWithValidId() {
        UUID id = UUID.randomUUID();
        User expectedUser = Instancio.create(User.class);

        when(userRepository.findById(id)).thenReturn(Optional.of(expectedUser));

        User result = userService.findById(id);

        assertEquals(expectedUser, result);
        verify(userRepository).findById(id);
        verifyNoInteractions(userMapper);
    }

    @Test
    @DisplayName("Buscar usuário por ID com ID inválido deve falhar")
    void findByIdWithInvalidId() {
        UUID id = UUID.randomUUID();
        
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.findById(id));
        verify(userRepository).findById(id);
        verifyNoInteractions(userMapper);
    }
}