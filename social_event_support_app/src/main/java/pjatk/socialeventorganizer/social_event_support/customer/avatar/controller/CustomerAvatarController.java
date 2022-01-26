package pjatk.socialeventorganizer.social_event_support.customer.avatar.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pjatk.socialeventorganizer.social_event_support.customer.avatar.service.CustomerAvatarService;

@Slf4j
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("api/customer/avatar")
public class CustomerAvatarController {

    private final CustomerAvatarService customerAvatarService;


    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            path = "upload",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> upload(@RequestParam long customerId, @RequestParam("file") MultipartFile file) {
        customerAvatarService.upload(customerId, file);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    @RequestMapping(
            method = RequestMethod.DELETE)
    public ResponseEntity<Void> delete(@RequestParam long id) {
        customerAvatarService.deleteById(id);
        return ResponseEntity.ok().build();

    }
}
