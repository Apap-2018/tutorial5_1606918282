package com.apap.tutorial5.controller;

import com.apap.tutorial5.model.*;
import com.apap.tutorial5.service.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CarController {

	@Autowired
	private CarService carService;

	@Autowired
	private DealerService dealerService;
	
	@RequestMapping(value = "/car/add/{dealerId}", method = RequestMethod.GET)
	private String add(@PathVariable(value = "dealerId") Long dealerId, Model model) {
		CarModel car = new CarModel();
		DealerModel dealer = dealerService.getDealerDetailById(dealerId).get();
		car.setDealer(dealer);
		
		model.addAttribute("car", car);
		model.addAttribute("id",car.getDealer().getId());
		model.addAttribute("title", ("Add Car " + dealerId));
		return "addCar";
	}
	
	@RequestMapping(value = "/car/add", method = RequestMethod.POST)
	private String addCarSubmit(@ModelAttribute CarModel car) {
		carService.addCar(car);
		return "add";
	}
	
	@RequestMapping(value = "/car/update/{dealerId}", method = RequestMethod.GET)
	private String update(@PathVariable(value = "dealerId") String id, Model model) {
		DealerModel updated = dealerService.getDealerDetailById(Long.parseLong(id)).orElse(null);
		List<CarModel> cars = carService.allCarDealer(updated);
		model.addAttribute("cars",cars);
		model.addAttribute("title", ("Update Car " + id));
		return "updateCars";
	}
	
	@RequestMapping(value = "/car/update/id={carId}", method = RequestMethod.POST)
	private String updateDealerSubmit(@PathVariable(value = "carId") String id, @ModelAttribute CarModel car) {
		CarModel updated = carService.getCarDetailById(Long.parseLong(id)).orElse(null);
		updated.setBrand(car.getBrand());
		updated.setType(car.getType());
		updated.setAmount(car.getAmount());
		updated.setPrice(car.getPrice());
		carService.addCar(updated);
		return "update";
	}
	
	@RequestMapping(value = "/car/delete", method = RequestMethod.POST)
	private String delete(@ModelAttribute DealerModel dealer, Model model) {
		for(CarModel car : dealer.getListCar()) {
			carService.deleteCar(car);
		}
		return "delete";
		
	}
	
	@RequestMapping(value = "/car/addCar/{dealerId}", method = RequestMethod.POST)

		    public @ResponseBody

		    String addCars(@RequestBody CarList cars, @PathVariable(value = "dealerId") String id){

				List<CarModel> allCar = cars.getCars();
				DealerModel added = dealerService.getDealerDetailById(Long.parseLong(id)).orElse(null);
		
				for (int i = 0; i < allCar.size(); i++) {
					allCar.get(i).setDealer(added);
					carService.addCar(allCar.get(i));
				}
				return "add";
			}
	
}

class CarList {   

    List<CarModel> cars;    

    public List<CarModel> getCars() {
        return cars;
    }

    public void setCars(List<CarModel> cars) {
        this.cars= cars;
    }
    
    public int getLength() {
    	return cars.size();
    }
}

