package org.springframework.samples.nt4h.message;

import com.github.cliftonlabs.json_simple.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.samples.nt4h.user.User;
import org.springframework.samples.nt4h.user.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/messages")
public class MessageResource {
    private final UserService userService;
    private final MessageService messageService;

    @GetMapping("/{username}")
    public ResponseEntity<String> updateMessages(@PathVariable String username) {
        User loggedUser = userService.getLoggedUser();
        JsonObject jsonObject = new JsonObject();
        jsonObject.put("messages", messageService.getMessageBySenderWithReceiver(loggedUser.getUsername(), username));
        return ResponseEntity.ok(jsonObject.toJson());
    }
}
