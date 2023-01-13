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
package org.springframework.samples.nt4h.user;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.nt4h.statistic.Statistic;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    // Constantes.
    private static final String VIEW_USER_CREATE_OR_UPDATE_FORM = "users/createUserForm";
    private static final String VIEW_USER_LIST = "users/usersGameList";
    private static final String VIEW_USER_DETAILS = "users/userDetails";
    private static final String PAGE_WELCOME = "redirect:/welcome";
    private static final String PAGE_USER_DETAILS = "redirect:/users/details";
    private static final String VIEW_USER_STATISTICS = "users/userStatistics";
    private static final Integer PAGE_SIZE = 5;

    // Servicios.
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;



    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder) {
        dataBinder.setDisallowedFields("id");
    }

    @ModelAttribute("loggedUser")
    public User getLoggedUser() {
        return this.userService.getLoggedUser();
    }

    // Obtener todos los usuarios.
    @GetMapping
    public String getUsers(@RequestParam(defaultValue = "0") int page, ModelMap model) {
        page = page < 0 ? 0 : page;
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        List<User> users = userService.getAllUsers();
        Page<User> usersPage = userService.getAllUsers(pageable);
        if (!users.isEmpty() && usersPage.isEmpty()) {
            page = users.size() / PAGE_SIZE;
            pageable = PageRequest.of(page, PAGE_SIZE);
            usersPage = userService.getAllUsers(pageable);
        }
        model.put("isNext", usersPage.hasNext());
        model.addAttribute("users", usersPage.getContent());
        model.put("page", page);
        return VIEW_USER_LIST;
    }

    @GetMapping("/details")
    public String showOwner() {
        return VIEW_USER_DETAILS;
    }

    // Crear usuario
    @GetMapping(value = "/new")
    public String initCreationForm() {
        return VIEW_USER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/new")
    public String processCreationForm(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return VIEW_USER_CREATE_OR_UPDATE_FORM;
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.saveUser(user);
            return PAGE_WELCOME;
        }
    }

    //Editar usuario
    @GetMapping(value = "/edit")
    public String initUpdateUserForm() {return VIEW_USER_CREATE_OR_UPDATE_FORM;
    }

    @PostMapping(value = "/edit")
    public String processUpdateUserForm(@Valid User user, BindingResult result) {
        User oldUser = this.userService.getLoggedUser();
        if (result.hasErrors()) return VIEW_USER_CREATE_OR_UPDATE_FORM;
        else {
            User newUser = user.toBuilder().enable(oldUser.getEnable()).tier(oldUser.getTier()).authority(oldUser.getAuthority()).build();
            newUser.setId(oldUser.getId());
            userService.saveUser(newUser);
            return PAGE_USER_DETAILS;
        }
    }

    @GetMapping(value = "/delete")
    public String processDeleteUser() {
        User loggedUser = userService.getLoggedUser();
        SecurityContextHolder.clearContext();
        this.userService.deleteUser(loggedUser);
        return PAGE_WELCOME;
    }

    @GetMapping("/statistics")
    public String showUserStatistics(ModelMap model) {
        Statistic userStatistic = getLoggedUser().getStatistic();
        model.put("statistic", userStatistic);
        return VIEW_USER_STATISTICS;
    }

}
