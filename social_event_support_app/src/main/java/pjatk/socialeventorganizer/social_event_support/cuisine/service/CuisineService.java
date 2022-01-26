package pjatk.socialeventorganizer.social_event_support.cuisine.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
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
        return cuisineRepository.findAll(Sort.by("name"));
    }

    public Cuisine create(CuisineDto dto) {
        if (exists(dto.getName())) {
            throw new IllegalArgumentException("Cuisine with name " + dto.getName() + " already exists");
        }
        final Cuisine cuisine = CuisineMapper.fromDto(dto);

        cuisineRepository.save(cuisine);

        return cuisine;
    }

    public Cuisine getByName(String name) {
        return cuisineRepository.findByName(name);
    }

    private boolean exists(String name) {
        return cuisineRepository.existsByName(name);
    }

}
