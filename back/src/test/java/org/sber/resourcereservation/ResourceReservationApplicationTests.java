package org.sber.resourcereservation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sber.resourcereservation.dto.*;
import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.Role;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.repository.ReservationRepository;
import org.sber.resourcereservation.repository.ResourceRepository;
import org.sber.resourcereservation.repository.RoleRepository;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "dev")
class ResourceReservationApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ResourceRepository resourceRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    void contextLoads() {
    }

    @BeforeEach
    void createAll() {
        Role role = new Role("USER");
        roleRepository.save(role);

        User user = new User();
        user.setName("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setAuthorities(Set.of(role));
        userRepository.save(user);

        Resource resource = new Resource();
        resource.setName("resource");
        resourceRepository.save(resource);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setResource(resource);
        reservation.setReservationStart(new Date(100));
        reservation.setReservationStart(new Date(200));
        reservationRepository.save(reservation);
    }

    @AfterEach
    void deleteAll() {
        reservationRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        resourceRepository.deleteAll();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    void testAuthWithAdminForActuator() throws Exception {
        mvc.perform(get("/actuator")).andExpect(status().isOk());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testAuthWithUserForActuator() throws Exception {
        mvc.perform(get("/actuator")).andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testAuthWithAnonymousUse() throws Exception {
        mvc.perform(get("/actuator")).andExpect(status().isUnauthorized());
        mvc.perform(get("/users/user/reservations")).andExpect(status().isUnauthorized());
        mvc.perform(get("/resources/resource/reservations")).andExpect(status().isUnauthorized());
        mvc.perform(get("/reservations/0")).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testUsersEndpoint() throws Exception {
        mvc.perform(get("/users/user/reservations")).andExpect(status().isOk());
        mvc.perform(get("/users/unknown/reservations")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testResourcesEndpoints() throws Exception {
        mvc.perform(get("/resources/resource/reservations")).andExpect(status().isOk());
        mvc.perform(get("/resources/unknown/reservations")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testReservationsEndpoints() throws Exception {
        Long id = reservationRepository.findAll().get(0).getId();
        mvc.perform(get("/reservations/" + id)).andExpect(status().isOk());
        mvc.perform(get("/reservations/0")).andExpect(status().isNotFound());
        mvc.perform(get("/reservations/t")).andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testAcquire() throws Exception {
        AcquireDto acquireDto = getAcquireDto();
        mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isOk());

        acquireDto.setStart(LocalDateTime.of(0, 1, 1, 0, 25));
        acquireDto.setEnd(LocalDateTime.of(0, 1, 1, 0, 35));
        mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isConflict());

        acquireDto.setStart(LocalDateTime.of(0, 1, 1, 0, 35));
        acquireDto.setEnd(LocalDateTime.of(0, 1, 1, 0, 45));
        mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isConflict());

        acquireDto.setStart(LocalDateTime.of(0, 1, 1, 0, 20));
        acquireDto.setEnd(LocalDateTime.of(0, 1, 1, 0, 50));
        mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isConflict());

        acquireDto.setStart(LocalDateTime.of(0, 1, 1, 0, 32));
        acquireDto.setEnd(LocalDateTime.of(0, 1, 1, 0, 38));
        mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testRelease() throws Exception {
        AcquireDto acquireDto = getAcquireDto();
        var result = mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isOk()).andReturn();
        Id id = objectMapper.readValue(result.getResponse().getContentAsString(), Id.class);
        mvc.perform(post("/resources/release/" + id.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isOk());
        mvc.perform(post("/resources/release/" + id.getId()).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isNotFound());
    }

    @Test
    void testLogin() throws Exception {
        UserAuthDto userAuthDto = new UserAuthDto();
        userAuthDto.setName("user");
        userAuthDto.setPassword("user");
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userAuthDto))).andExpect(status().isOk());

        userAuthDto.setPassword("user1");
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userAuthDto))).andExpect(status().isUnauthorized());

        userAuthDto.setName("user1");
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userAuthDto))).andExpect(status().isUnauthorized());
    }

    @Test
    void testSignup() throws Exception {
        UserAuthDto userAuthDto = new UserAuthDto();
        userAuthDto.setName("user1");
        userAuthDto.setPassword("user1");
        mvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userAuthDto))).andExpect(status().isOk());

        userAuthDto.setName("user1");
        mvc.perform(post("/signup").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userAuthDto))).andExpect(status().isConflict());
    }

    private AcquireDto getAcquireDto() {
        UserDto userDto = new UserDto();
        userDto.setName("user");
        ResourceDto resourceDto = new ResourceDto();
        resourceDto.setName("resource");
        AcquireDto acquireDto = new AcquireDto();
        acquireDto.setResource(resourceDto);
        acquireDto.setUser(userDto);
        acquireDto.setStart(LocalDateTime.of(0, 1, 1, 0, 30));
        acquireDto.setEnd(LocalDateTime.of(0, 1, 1, 0, 40));
        return acquireDto;
    }
}
