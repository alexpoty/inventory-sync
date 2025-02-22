package com.alexpoty.inventory_sync;

import org.springframework.boot.SpringApplication;

public class TestInventorySyncApplication {

	public static void main(String[] args) {
		SpringApplication.from(InventorySyncApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
