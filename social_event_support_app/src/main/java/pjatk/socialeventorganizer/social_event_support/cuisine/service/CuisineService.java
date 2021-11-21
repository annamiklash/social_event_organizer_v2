package pjatk.socialeventorganizer.social_event_support.cuisine.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pjatk.socialeventorganizer.social_event_support.cuisine.mapper.CuisineMapper;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.Cuisine;
import pjatk.socialeventorganizer.social_event_support.cuisine.model.dto.CuisineDto;
import pjatk.socialeventorganizer.social_event_support.cuisine.repository.CuisineRepository;
import pjatk.socialeventorganizer.social_event_support.exceptions.IllegalArgumentException;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CuisineService {

    private final CuisineRepository cuisineRepository;

    public List<Cuisine> list() {
        return cuisineRepository.findAll();
    }

    public Cuisine create(CuisineDto dto) {
        if (exists(dto.getName())) {
            throw new IllegalArgumentException("Cuisine with name " + dto.getName() + " already exists");
        }
        final Cuisine cuisine = CuisineMapper.fromDto(dto);

        save(cuisine);

        return cuisine;
    }

    private boolean exists(String name) {
        return cuisineRepository.existsByName(name);
    }

    private void save(Cuisine cuisine) {
        cuisineRepository.save(cuisine);
    }
}
