/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private static final String VIEWS_OWNER_CREATE_FORM = "users/createOwnerForm";

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @GetMapping(value = "/new")
    public String initCreationForm(Map<String, Object> model) {
        User user = new User();
        model.put("user", user);
        return VIEWS_OWNER_CREATE_FORM;
    }

    @GetMapping(value = "/{userId}/edit")
    public String initUpdateOwnerForm(@PathVariable("userId") int userId, Model model) {
        User user= this.userService.getById(userId);
        model.addAttribute(user);
        return VIEWS_OWNER_CREATE_FORM;
    }

    @PostMapping(value = "/new")
    public String processCreationForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return VIEWS_OWNER_CREATE_FORM;
        } else {
            userService.saveUser(user);
            return "redirect:/users";
        }
    }
}
