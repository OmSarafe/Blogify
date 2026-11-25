package com.blogify.entry.services;

import com.blogify.entry.dtos.userDtos.UserResponseDtos;
import com.blogify.entry.enteties.User;
import com.blogify.entry.repositories.UserRepositories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepositories userRepositories;

    @Mock
    PostService postService;

    @Spy
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    UserService userService;

    private User newUser(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(username + "@example.com");
        return user;
    }

    @Test
    void createStoresHashedPassword() {
        User user = newUser("alice", "secret123");
        when(userRepositories.findByUsername("alice")).thenReturn(Optional.empty());
        when(userRepositories.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.create(user);

        assertNotEquals("secret123", saved.getPassword());
        assertTrue(passwordEncoder.matches("secret123", saved.getPassword()));
    }

    @Test
    void createRejectsDuplicateUsername() {
        User user = newUser("alice", "secret123");
        when(userRepositories.findByUsername("alice")).thenReturn(Optional.of(user));

        assertThrows(ResponseStatusException.class, () -> userService.create(user));
        verify(userRepositories, never()).save(any());
    }

    @Test
    void updateHashesNewPassword() {
        User existing = newUser("alice", passwordEncoder.encode("oldpass1"));
        when(userRepositories.findById(1)).thenReturn(Optional.of(existing));
        when(userRepositories.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User changes = new User();
        changes.setPassword("newpass1");
        User updated = userService.update(1, changes);

        assertTrue(passwordEncoder.matches("newpass1", updated.getPassword()));
    }

    @Test
    void updateWithoutPasswordKeepsExistingHash() {
        String oldHash = passwordEncoder.encode("oldpass1");
        User existing = newUser("alice", oldHash);
        when(userRepositories.findById(1)).thenReturn(Optional.of(existing));
        when(userRepositories.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User changes = new User();
        changes.setEmail("new@example.com");
        User updated = userService.update(1, changes);

        assertEquals(oldHash, updated.getPassword());
        assertEquals("new@example.com", updated.getEmail());
    }

    @Test
    void responseDtoHasNoPasswordField() {
        assertThrows(NoSuchFieldException.class, () -> UserResponseDtos.class.getDeclaredField("password"));
    }
}
