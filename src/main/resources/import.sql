delete from TECHNICAL_CHECK;
delete from RENTS;
delete from VEHICLES;
delete from USERS;
delete from CHARGING_POLICIES;

-- insert customers
insert into USERS (id, name, email, password, phone, street, city, zipcode, AFM, type, surname, number, expirationDate, holderName) values (1000, 'ΙΩΑΝΝΗΣ','evangellou@gmail.com','johnjohn','6941603677','ΛΕΥΚΑΔΟΣ 22','ΑΘΗΝΑ','35896','166008282','Customer','ΕΥΑΓΓΕΛΟΥ','7894665213797564','2027-11-26','ΙΩΑΝΝΗΣ ΕΥΑΓΓΕΛΟΥ');
insert into USERS (id, name, email, password, phone, street, city, zipcode, AFM, type, surname, number, expirationDate, holderName) values (1001, 'ΝΙΚΟΣ','nick7@yahoo.gr','olympiakos','6924567813','ΜΕΘΟΝΗΣ 6','ΠΕΙΡΑΙΑΣ','18545','054893175','Customer','ΠΑΠΑΔΗΜΗΤΡΙΟΥ','1645923557481658','2026-08-05','ΝΙΚΟΣ ΠΑΠΑΔΗΜΗΤΡΙΟΥ');

-- insert charging policies

insert into CHARGING_POLICIES (id) values (1500);
insert into CHARGING_POLICIES (id) values (1501);
-- insert into sub tables of charging policies

-- insert companies

insert into USERS (id, name, email, password, phone, street, city, zipcode, AFM, type, IBAN, income_amount, income_currency, damage_cost_amount, damage_cost_currency, policy_id) values (2000, 'AVIS','avis@gmail.com', 'password123', '2104578965','ΠΑΤΗΣΙΩΝ 37','ΑΘΗΝΑ','12478','163498317','Company','GR2514526358789654', 0, 'EUR', 0, 'EUR', 1500);
insert into USERS (id, name, email, password, phone, street, city, zipcode, AFM, type, IBAN, income_amount, income_currency, damage_cost_amount, damage_cost_currency, policy_id) values (2001, 'SPEED','speed@gmail.com', 'ilovecookies', '2644125415','ΛΕΥΚΩΣΙΑΣ 66','ΠΑΤΡΑ','34785','999641227','Company','GR3687254378963625', 0, 'EUR', 0, 'EUR', 1501);


-- insert vehicles

insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3000, 'TOYOTA', 'YARIS', 2015, 100000, 'YMB-6325', 'Hatchback', 'Available', 30, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3001, 'VOLKSWAGEN', 'T-ROC', 2016, 80000, 'PMT-3013', 'SUV', 'Available', 50, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3002, 'RENAULT', 'MEGANE', 2018, 50000, 'KIK-2160', 'Sedan', 'Available', 40, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3003, 'MAZDA', 'MX-5', 2016, 30000, 'NAT-1234', 'Cabrio', 'Available', 50, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3004, 'MINI', 'ONE D', 2016, 240000, 'ZZE-7852', 'Mini', 'Available', 40, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3005, 'FIAT', '500 S', 2014, 92000, 'HIK-9459', 'Hatchback', 'Available', 30, 'EUR', 0, 0, 2000);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3006, 'OPEL', 'CORSA', 2018, 64000, 'PIP-4556', 'Hatchback', 'Available', 60, 'EUR', 0, 0, 2001);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3007, 'AUDI', 'A7', 2021, 100000, 'MMA-8745', 'Sedan', 'Available', 70, 'EUR', 0, 0, 2001);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3008, 'NISSAN', 'QASHQAI AUTOMATIC', 2023, 50000, 'ZIK-6834', 'SUV', 'Available', 100, 'EUR', 0, 0, 2001);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3009,'TOYOTA', 'C-HR', 2022, 49000, 'PAP-3333', 'SUV', 'Available', 80, 'EUR', 0, 0, 2001);
insert into VEHICLES (id, manufacturer, model, year_of_model, miles, plate_number, vehicle_type, vehicle_state, amount, currency, count_damages, count_rents, company_id) values (3010,'VOLKSWAGEN', 'POLO', 2018, 73000, 'NIK-9012', 'Hatchback', 'Available', 50, 'EUR', 0, 0, 2001);

-- insert technical checks

insert into TECHNICAL_CHECK (id, rent_id, type, damageType) values (5000, null, 'Impl', 'NoDamage');
insert into TECHNICAL_CHECK (id, rent_id, type, damageType) values (5001, null, 'Impl', 'Scratches');

-- insert rents

insert into RENTS(id, customer_id, startDate, endDate, vehicle_id, technical_check_id, rentState, mileageCost_amount, mileageCost_currency, fixedCost_amount, fixedCost_currency, damageCost_amount, damageCost_currency,  totalCost_amount, totalCost_currency) values (4000, 1000, '2023-12-05', '2023-12-10', 3000, 5000, 'Finished', 119.70000457763672, 'EUR', 180.0, 'EUR', 0, 'EUR', 299.7000045776367, 'EUR');
insert into RENTS(id, customer_id, startDate, endDate, vehicle_id, technical_check_id, rentState, mileageCost_amount, mileageCost_currency, fixedCost_amount, fixedCost_currency, damageCost_amount, damageCost_currency,  totalCost_amount, totalCost_currency) values (4001, 1001, '2023-12-15', '2023-12-25', 3004, 5001, 'Finished', 39.599998474121094, 'EUR', 440.0, 'EUR', 90.0, 'EUR', 569.5999984741211, 'EUR');



