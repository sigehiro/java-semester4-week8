package com.example.Week8SecurityApp.controllers;

import com.example.Week8SecurityApp.models.Dish;
import com.example.Week8SecurityApp.services.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/restaurant/admin")
public class AdminController {

    private final DishService dishService;

    @Autowired
    public AdminController(DishService dishService) {
        this.dishService = dishService;
    }


    //endpoint for add dish page
    @GetMapping("/add")
    public String addDish(Model model) {
        model.addAttribute("dish", new Dish());
        return "add-dish";
    }

    //endpoint the save the dish
    @PostMapping("/save")
    public String saveDishes(@ModelAttribute Dish dish, Model model){

        int statusCode = dishService.saveDish(dish);
        if(statusCode == 0){
            model.addAttribute("message", "Price should be less than 20");
            return "add-dish";
        }
        //http://localhost:8080/restaurant/save
        //Data is input anything in add-dish -> solve <problem -> ID:0 in save page> -> auto increment
        //Subtract “-1” to get the last element of the list, since the list starts at index 0
        // Get the last added dish from the list of saved dishes
        List<Dish> dishes = dishService.getAllDishes();
        Dish lastDishes = dishes.get(dishes.size() - 1);

        //save the data
        //open the menu page with updates data
        // Add data to the model to open the menu with updated data
        model.addAttribute("dishes", lastDishes);
        model.addAttribute("message", lastDishes.getName() + " added successfully");
        return "menu";
    }


    //endpoint to delete a dish
    @GetMapping ("/delete/{id}")
    public String deleteDish(@PathVariable int id, Model model) {
        // Delete the dish by ID and get status code
        int deleteStatusCode = dishService.deleteDishById(id);

        // Check if delete was successful (dish exists)
        if (deleteStatusCode == 1) {
            return "redirect:/restaurant/menu/1?message=Dish deleted successfully!";
        }
        //does delete fail (dish does not exist)
        return "redirect:/restaurant/menu/1?message=Dish does not exist!";
    }


    //endpoint to update a dish
    @GetMapping("/update/{id}")
    public String updateDish(@PathVariable int id, Model model) {
        //get the dish to be updated by its id from the database
        Optional<Dish> optionalDishToUpdate = dishService.getDishById(id);

        if (optionalDishToUpdate.isPresent()) {
            dishService.updateDish(optionalDishToUpdate.get());
            model.addAttribute("dish", optionalDishToUpdate.get());
            return "add-dish";
        }
        return "redirect:/restaurant/menu/1?message=Dish does not exist!";

    }

    @PostMapping("/update")
    public String updateDish(@ModelAttribute Dish dish, Model model) {
        //call the update method in the service class
        dishService.updateDish(dish);
        return "redirect:/restaurant/menu/1?message=Dish updated successfully!";
    }

}
