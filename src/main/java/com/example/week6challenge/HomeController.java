package com.example.week6challenge;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CarRepository carRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String home(Model model){
        model.addAttribute("categorys", categoryRepository.findAll());
        model.addAttribute("cars", carRepository.findAll());

        return "index";
    }

//    Category
    @GetMapping("/addcategory")
    public String categoryForm(Model model){
        model.addAttribute("category", new Category());
        return "categoryform";
    }

    @PostMapping("/process_category")
    public String processCategoryForm(@Valid Category category, BindingResult result){
        if (result.hasErrors()){
            return "categoryform";
        }
        categoryRepository.save(category);

        return "redirect:/categorylist";
    }

    @RequestMapping("/categorylist")
    public String categoryList(Model model){
        model.addAttribute("categorys", categoryRepository.findAll());

        return "categorylist";
    }

    @RequestMapping("/detail_category/{id}")
    public String showCategory(@PathVariable("id") long id, Model model){
        model.addAttribute("category", categoryRepository.findById(id).get());

        return "showcategory";
    }

    @RequestMapping("/update_category/{id}")
    public String updateCategory(@PathVariable("id") long id, Model model){
        model.addAttribute("category", categoryRepository.findById(id).get());
        return "categoryform";
    }

    @RequestMapping("/delete_category/{id}")
    public String delCategory(@PathVariable("id") long id){
        categoryRepository.deleteById(id);
        return "index";
    }

// Car
    @GetMapping("/addcar")
    public String carForm(Model model){
        model.addAttribute("categorys", categoryRepository.findAll());
        model.addAttribute("car", new Car());

        return "carform";
    }

    @PostMapping("/process_car")
    public String processCarForm(@Valid Car car, BindingResult result, @RequestParam("file")MultipartFile file ){
        if (result.hasErrors()){
            return "carform";
        }
        if (file.isEmpty()){
            return "redirect:/addcar";
        }
        try {
            Map uploadResult = cloudc.upload(file.getBytes(), ObjectUtils.asMap("resourcetype", "auto"));
            car.setImage(uploadResult.get("url").toString());
        } catch (IOException e){
            e.printStackTrace();
            return "redirect:/addcar";
        }

        carRepository.save(car);

        return "redirect:/carlist";
    }

    @RequestMapping("/carlist")
    public String carList(Model model){
        model.addAttribute("cars", carRepository.findAll());

        return "carlist";
    }

    @RequestMapping("/detail_car/{id}")
    public String showCar(@PathVariable("id") long id, Model model){
        model.addAttribute("car", carRepository.findById(id).get());

        return "showcar";
    }

    @RequestMapping("/update_car/{id}")
    public String updateCar(@PathVariable("id") long id, Model model){
        model.addAttribute("car", carRepository.findById(id).get());
        model.addAttribute("categorys",categoryRepository.findAll());
        return "carform";
    }

    @RequestMapping("/delete_car/{id}")
    public String delCar(@PathVariable("id") long id){
        carRepository.deleteById(id);
        return "index";
    }


}
