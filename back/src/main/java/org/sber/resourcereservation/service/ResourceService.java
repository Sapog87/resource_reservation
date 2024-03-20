package org.sber.resourcereservation.service;

import jakarta.servlet.http.HttpServletRequest;
import org.sber.resourcereservation.entity.Reservation;
import org.sber.resourcereservation.entity.Resource;
import org.sber.resourcereservation.entity.User;
import org.sber.resourcereservation.exception.*;
import org.sber.resourcereservation.repository.ResourceRepository;
import org.sber.resourcereservation.repository.UserRepository;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * Сервисный класс для работы с ресурсами.
 */
@Service
public class ResourceService {
    private final ReservationService reservationService;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;

    public ResourceService(ReservationService reservationService, ResourceRepository resourceRepository, UserRepository userRepository) {
        this.reservationService = reservationService;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
    }

    /**
     * Создает резервирование для ресурса и пользователя на указанный период.
     *
     * @param user     Пользователь, который осуществляет резервирование.
     * @param resource Ресурс, который требуется зарезервировать.
     * @param start    Начальное время резерва.
     * @param end      Конечное время резерва.
     * @param request  HTTP запрос пользователя.
     * @return Идентификатор созданного резерва.
     * @throws InvalidPeriodException    Если указанный период неправильный (конечное время раньше начального) или занят другим резервом.
     * @throws UserNotFoundException     Если пользователь не найден.
     * @throws InvalidUserException      Если пользователь не авторизован или имя пользователя не совпадает с именем в запросе.
     * @throws ResourceNotFoundException Если ресурс не найден.
     */
    public Long acquire(User user, Resource resource, Timestamp start, Timestamp end, HttpServletRequest request) {
        User u = validateUser(user, request);
        Resource r = valiadateResource(resource);

        if (start.after(end))
            throw new InvalidPeriodException("Duration can't be negative");

        Reservation reservation = reservationService.makeReservation(start, end, r, u);

        return reservation.getId();
    }

    private User validateUser(User user, HttpServletRequest request) {
        String userName = user.getName();
        String userPrincipal = request.getUserPrincipal().getName();
        if (!Objects.equals(userName, userPrincipal)) {
            throw new InvalidUserException("Name of authorized user must be equal to user in request");
        }
        User u = userRepository.findByName(userName);
        if (Objects.isNull(u))
            throw new UserNotFoundException("No user with such name");
        return u;
    }

    private Resource valiadateResource(Resource resource) {
        String resourceName = resource.getName();
        Resource r = resourceRepository.findByName(resourceName);
        if (Objects.isNull(r))
            throw new ResourceNotFoundException("No resource with such name");
        return r;
    }

    /**
     * Получает список всех ресурсов.
     *
     * @return Список всех ресурсов.
     * @throws ReservationNotFoundException Если не найдены ресурсы.
     */
    public List<Resource> all() {
        List<Resource> resources = resourceRepository.findAll();
        if (resources.isEmpty()) {
            throw new ReservationNotFoundException("No resources was found");
        }
        return resources;
    }

    /**
     * Создает новый ресурс.
     *
     * @param resource Новый ресурс для создания.
     * @return True, если ресурс успешно создан, иначе false.
     * @throws ResourceAlreadyExistException Если ресурс с таким именем уже существует.
     */
    @Retryable(retryFor = SQLException.class)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public Boolean create(Resource resource) {
        if (Objects.nonNull(resourceRepository.findByName(resource.getName()))) {
            throw new ResourceAlreadyExistException("Resource with such name already exist");
        }
        resourceRepository.save(resource);
        return true;
    }
}
