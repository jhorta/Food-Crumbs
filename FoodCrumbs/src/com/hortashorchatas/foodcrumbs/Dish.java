package com.hortashorchatas.foodcrumbs;

public class Dish {
	private String business_id;
	private String name;
	private String rating;
	private String price;
	
	public Dish(String business_id, String name, String rating, String price) {
		this.setBusiness_id(business_id);
		this.setName(name);
		this.setRating(rating);
		this.setPrice(price);
	}

	public String getBusiness_id() {
		return business_id;
	}

	public void setBusiness_id(String business_id) {
		this.business_id = business_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}
}
