/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.apollo.view;

import org.primefaces.apollo.dto.Car;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name="dtBasicView")
@ViewScoped
public class BasicView implements Serializable {
    
    private List<Car> cars;
    private Car car;

    @PostConstruct
    public void init() {
    	cars = new ArrayList<Car>();
    	for (int i = 1; i < 100; i++) {
    		cars.add(new Car(String.valueOf(i), "Tên cơ sở " + i, "Người đại diện " + i, "Địa chỉ " + i, 
    				"Điện thoại " + i, "https://website" + i + ".com", "email" + i + "@gmail.com"));
    	}
    }
    
    public List<Car> getCars() {
        return cars;
    }
    
    public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}
}
