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

import java.sql.Timestamp;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "test")
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
        reservation.setReservationStart(new Timestamp(10000));
        reservation.setReservationEnd(new Timestamp(20000));
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
        mvc.perform(get("/reservations")).andExpect(status().isOk());
        mvc.perform(get("/reservations?date=1970-01-01T00:00:15")).andExpect(status().isOk());
        mvc.perform(get("/reservations?date=1970-01-01T00:00:25")).andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(authorities = "USER")
    void testAcquire() throws Exception {
        AcquireDto acquireDto = getAcquireDto();
        acquireDto.setStart(new Timestamp(25000));
        acquireDto.setEnd(new Timestamp(35000));
        mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isOk());

        acquireDto.setStart(new Timestamp(25000));
        acquireDto.setEnd(new Timestamp(35000));
        mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isConflict());

        acquireDto.setStart(new Timestamp(22000));
        acquireDto.setEnd(new Timestamp(30000));
        mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isConflict());

        acquireDto.setStart(new Timestamp(30000));
        acquireDto.setEnd(new Timestamp(40000));
        mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isConflict());

        acquireDto.setStart(new Timestamp(20000));
        acquireDto.setEnd(new Timestamp(40000));
        mvc.perform(post("/resources/acquire").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(acquireDto))).andExpect(status().isConflict());

        acquireDto.setStart(new Timestamp(27000));
        acquireDto.setEnd(new Timestamp(33000));
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
        mvc.perform(post("/login").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userAuthDto))).andExpect(status().isNotFound());
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
        acquireDto.setStart(new Timestamp(25000));
        acquireDto.setEnd(new Timestamp(35000));
        return acquireDto;
    }
}
