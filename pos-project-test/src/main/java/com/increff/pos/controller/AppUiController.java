package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}


	@RequestMapping(value = "/ui/brand")
	public ModelAndView brand() {
		return mav("brand.html");
	}

	@RequestMapping(value = "/ui/product")
	public ModelAndView product() {
		return mav("product.html");
	}

	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/create-order")
	public ModelAndView createOrder() {
		return mav("createOrder.html");
	}

	@RequestMapping(value = "/ui/order")
	public ModelAndView order() {
		return mav("order.html");
	}

	@RequestMapping(value = "/ui/reports/inventory")
	public ModelAndView inventoryReport() {
		return mav("inventoryReport.html");
	}

	@RequestMapping(value = "/ui/reports/brand")
	public ModelAndView brandReport() {
		return mav("brandReport.html");
	}
	@RequestMapping(value = "/ui/reports/daily")
	public ModelAndView schedulerReport() {
		return mav("schedulerReport.html");
	}

	@RequestMapping(value = "/ui/reports/sales")
	public ModelAndView salesReport() {
		return mav("salesReport.html");
	}


}
